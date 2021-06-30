package jp.co.ichain.luigi2.dao;

import java.io.IOException;
import java.io.InputStream;
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
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import jp.co.ichain.luigi2.exception.WebAwsException;
import jp.co.ichain.luigi2.resources.Luigi2Code;
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
   * @createdAt : 2021-06-30
   * @updatedAt : 2021-06-30
   * @param id
   * @param file
   * @param directoryName
   * @return
   * @throws IOException
   */
  public PutObjectResult upload(String directoryName, String id, InputStream inputStream,
      ObjectMetadata meta) throws IOException {
    val key = directoryName + id;

    return s3Client.putObject(bucketName, key, inputStream, meta);
  }

  /**
   * S3にからファイルダウンロード
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-30
   * @updatedAt : 2021-06-30
   * @param directoryName
   * @param id
   * @return
   * @throws AmazonServiceException
   * @throws SdkClientException
   * @throws Exception
   */
  public S3ObjectInputStream download(String directoryName, String id)
      throws AmazonServiceException, SdkClientException, Exception {
    S3Object s3Object = null;
    try {
      s3Object = s3Client.getObject(new GetObjectRequest(bucketName, directoryName + id));
    } catch (AmazonS3Exception e) {
      throw new WebAwsException(Luigi2Code.D0002, id);
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
  public void deleteAttachFile(String directoryName, String key)
      throws AmazonServiceException, SdkClientException {
    this.s3Client.deleteObject(new DeleteObjectRequest(bucketName, directoryName + key));
  }


  AwsS3Dao(@Value("${aws.s3.region}") String region, @Value("${aws.s3.bucket}") String bucket,
      AWSCredentialsProvider credentialsProvider) {

    this.s3Client = AmazonS3ClientBuilder.standard().withRegion(region)
        .withCredentials(credentialsProvider).build();
    this.bucketName = bucket;
  }
}
