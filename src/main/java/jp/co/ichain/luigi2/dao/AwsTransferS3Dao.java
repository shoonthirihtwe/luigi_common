package jp.co.ichain.luigi2.dao;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import fi.solita.clamav.ClamAVClient;

/**
 * AWS S3 dao
 *
 * @author : [AOT] s.paku
 * @createdAt : 2022-04-01
 * @updatedAt : 2022-04-01
 */
@Service
public class AwsTransferS3Dao {

  private final AmazonS3 s3Client;
  private final String bucketName;

  /**
   * S3にファイルアップロード
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022-03-31
   * @updatedAt : 2022-03-31
   * @param tenantId
   * @param path
   * @param inputStream
   * @return
   * @throws IOException
   */
  public PutObjectResult upload(String path, InputStream inputStream)
      throws IOException {
    ObjectMetadata meta = new ObjectMetadata();
    // size
    byte[] f = IOUtils.toByteArray(inputStream);
    meta.setContentLength(f.length);
    meta.setContentType(new Tika().detect(inputStream));

    return s3Client.putObject(bucketName, path, new ByteArrayInputStream(f), meta);
  }

  /**
   * S3にからファイルダウンロード
   *
   * @author : [AOT] s.paku
   * @createdAt : 2022-04-01
   * @updatedAt : 2022-04-01
   * @param url
   * @return
   * @throws SdkClientException
   * @throws AmazonServiceException
   * @throws IOException
   */
  public S3ObjectInputStream download(String url)
      throws AmazonServiceException, SdkClientException, IOException {
    return s3Client.getObject(bucketName, url).getObjectContent();
  }

  /**
   * S3 file copy
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022-04-01
   * @updatedAt : 2022-04-01
   * @param key
   * @param copyKey
   */
  public void copy(String key, String copyKey) {
    s3Client.copyObject(bucketName, key, bucketName, copyKey);
  }

  /**
   * S3 file delete
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022-04-01
   * @updatedAt : 2022-04-01
   * @param key
   * @param copyKey
   */
  public void delete(String key) {
    s3Client.deleteObject(bucketName, key);
  }

  /**
   * keyからファイル名抽出
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2022-04-01
   * @updatedAt : 2022-04-01
   * @param key
   */
  public String getFileName(String key) {
    return key.substring(key.lastIndexOf("/") + 1, key.length());
  }

  AwsTransferS3Dao(@Value("${aws.s3.region}") String region,
      @Value("${aws.s3.transfer.bucket}") String bucket,
      AWSCredentialsProvider credentialsProvider, ClamAVClient clamAvClient) {

    this.s3Client = AmazonS3ClientBuilder.standard().withRegion(region)
        .withCredentials(credentialsProvider).build();
    this.bucketName = bucket;
  }
}
