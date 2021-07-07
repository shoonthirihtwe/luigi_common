package jp.co.ichain.luigi2.dao;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import jp.co.ichain.luigi2.exception.WebAwsException;
import jp.co.ichain.luigi2.resources.Luigi2ErrorCode;
import jp.co.ichain.luigi2.vo.DownloadFileVo;
import lombok.val;

/**
 * AWS S3 dao
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-06-30
 * @updatedAt : 2021-06-30
 */
@Service
public class AwsS3Dao {

  private final AmazonS3 s3Client;
  private final String bucketName;

  /**
   * ファイルをS3にアップロード
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-07-01
   * @updatedAt : 2021-07-01
   * @param url
   * @param inputStream
   * @return
   * @throws IOException
   */
  public PutObjectResult upload(String url, InputStream inputStream) throws IOException {
    ObjectMetadata meta = new ObjectMetadata();
    // meta.setContentLength(inputStream.available());
    meta.setContentType(new Tika().detect(inputStream));

    return s3Client.putObject(bucketName, url, inputStream, meta);
  }

  /**
   * S3にからファイルダウンロード
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-07-01
   * @updatedAt : 2021-07-01
   * @param directoryName
   * @param fileName
   * @return
   * @throws AmazonServiceException
   * @throws SdkClientException
   */
  public InputStream download(String url) throws AmazonServiceException, SdkClientException {
    S3Object s3Object = null;
    try {
      s3Object = s3Client.getObject(new GetObjectRequest(bucketName, url));
    } catch (AmazonS3Exception e) {
      throw new WebAwsException(Luigi2ErrorCode.D0002, url);
    }

    return s3Object.getObjectContent();
  }

  /**
   * S3にからファイル削除
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-30
   * @updatedAt : 2021-06-30
   * @param directoryName
   * @param key
   * @throws AmazonServiceException
   * @throws SdkClientException
   */
  public void delete(String url) throws AmazonServiceException, SdkClientException {
    this.s3Client.deleteObject(new DeleteObjectRequest(bucketName, url));
  }

  AwsS3Dao(@Value("${aws.s3.region}") String region, @Value("${aws.s3.bucket}") String bucket,
      AWSCredentialsProvider credentialsProvider) {

    this.s3Client = AmazonS3ClientBuilder.standard().withRegion(region)
        .withCredentials(credentialsProvider).build();
    this.bucketName = bucket;
  }

  /**
   * S3File検索
   *
   * @author : [AOT] g.kim
   * @createdAt : 2021-07-02
   * @updatedAt : 2021-07-02
   * @param directoryName
   * @param paramMap
   * @throws AmazonServiceException
   * @throws SdkClientException
   * @throws ParseException
   */
  public List<DownloadFileVo> searchFile(Map<String, Object> paramMap, String directoryName,
      List<String> fileTags) throws AmazonServiceException, SdkClientException, ParseException {

    try {
      return sortAndPagination(
          getS3ObjectList(directoryName + paramMap.get("tenantId").toString() + "/", paramMap),
          paramMap, fileTags);
    } catch (SdkClientException e) {
      e.printStackTrace();
      throw e;
    }
  }

  /**
   * ファイルリスト整列,フィルタリング, paging
   *
   * @author : [AOT] g.kim
   * @createdAt : 2021-07-02
   * @updatedAt : 2021-07-02
   * @param objSummaries
   * @param paramMap
   * @return DownloadFileVoList
   */
  private List<DownloadFileVo> sortAndPagination(List<S3ObjectSummary> objSummaries,
      Map<String, Object> paramMap, List<String> fileTags) {
    Integer page = (Integer) paramMap.get("page");
    Integer rowCount = (Integer) paramMap.get("rowCount");


    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    List<DownloadFileVo> fileList = objSummaries.stream()
        .filter(s3ObjectSummary -> s3ObjectSummary.getSize() != 0).filter(s3ObjectSummary -> {
          for (val tag : fileTags) {
            if (s3ObjectSummary.getKey().indexOf(tag) != -1) {
              return true;
            }
          }
          return false;
        }).map(s -> {
          String key = s.getKey();
          try {
            return new DownloadFileVo(key, key.substring(key.lastIndexOf("/") + 1),
                format.parse(s.getKey().split("_")[2]), s.getLastModified());
          } catch (ParseException e) {
            e.printStackTrace();
          }
          return new DownloadFileVo(key, key.substring(key.lastIndexOf("/") + 1),
              null, s.getLastModified());
        }).collect(Collectors.toList());


    Collections.sort(fileList, new Comparator<DownloadFileVo>() {
      @Override
      public int compare(DownloadFileVo df1, DownloadFileVo df2) {
          return (int) (df2.getCreatedAt().getTime() - df1.getCreatedAt().getTime());
      }
    });

    paramMap.put("totalCount", fileList.size());
    return fileList.stream().skip(page * rowCount).limit(rowCount).collect(Collectors.toList());

  }

  /**
   * S3Object取得
   *
   * @author : [AOT] g.kim
   * @createdAt : 2021-07-02
   * @updatedAt : 2021-07-02
   * @param preDir
   * @param paramMap
   * @return S3ObjectSummary
   */
  private List<S3ObjectSummary> getS3ObjectList(String preDir, Map<String, Object> paramMap)
      throws ParseException {
    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM");

    val summaryList = new ArrayList<S3ObjectSummary>();
    Date fDate = new Date();
    Date tDate = new Date();
    Calendar cal = Calendar.getInstance();
    if (paramMap.get("from") instanceof String) {
      fDate = new Date(Long.parseLong(paramMap.get("from").toString()));
      cal.setTime(fDate);
    }

    if (paramMap.get("to") instanceof String) {
      tDate = new Date(Long.parseLong(paramMap.get("to").toString()));
    }

    do {
      ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(this.bucketName)
          .withPrefix(preDir + format.format(cal.getTime()));
      ListObjectsV2Result result = s3Client.listObjectsV2(req);
      summaryList.addAll(result.getObjectSummaries());
      cal.add(Calendar.MONTH, 1);

    } while (cal.getTimeInMillis() <= tDate.getTime());

    return summaryList;
  }
}
