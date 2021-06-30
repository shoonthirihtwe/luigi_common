package jp.co.ichain.luigi2.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import jp.co.ichain.luigi2.dao.AwsS3Dao;
import jp.co.ichain.luigi2.util.CollectionUtils;
import lombok.val;

/**
 * S3サービス
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-06-30
 * @updatedAt : 2021-06-30
 */
@Service
public class AwsS3Service {

  public enum Documents {
    CLAIM("claim_documents/"), NEW_BUSINESS("new_business_documents/"), MAINTENANCE(
        "maintenance_documents/");

    String name;

    Documents(String name) {
      this.name = name;
    }
  }

  @Autowired
  AwsS3Dao awsS3Dao;

  /**
   * S3にファイルをアップロードする
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-30
   * @updatedAt : 2021-06-30
   * @param documents
   * @param id
   * @param file
   * @throws IOException
   */
  public void upload(Documents documents, String id, MultipartFile file) throws IOException {

    InputStream inputStream = file.getInputStream();
    ObjectMetadata meta = new ObjectMetadata();
    meta.setContentLength(inputStream.available());
    meta.setContentType(new Tika().detect(inputStream));

    awsS3Dao.upload(documents.name, id, inputStream, meta);
  }

  /**
   * S3にファイルをアップロードする
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-30
   * @updatedAt : 2021-06-30
   * @param documents
   * @param id
   * @param fileList
   * @throws IOException
   */
  public void upload(Documents documents, String id, List<MultipartFile> fileList)
      throws IOException {
    for (val file : CollectionUtils.safe(fileList)) {
      this.upload(documents, id, file);
    }
  }

  /**
   * S3からファイルをダウンロードする
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-30
   * @updatedAt : 2021-06-30
   * @param documents
   * @param id
   * @return
   * @throws AmazonServiceException
   * @throws SdkClientException
   * @throws Exception
   */
  public ResponseEntity<S3ObjectInputStream> download(String documents, String id)
      throws AmazonServiceException, SdkClientException, Exception {
    val s3stream = awsS3Dao.download(documents, id);

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION,
            String.format("attachment; filename=\"profile.%s\"", new Tika().detect(s3stream)))
        .body(s3stream);
  }
}
