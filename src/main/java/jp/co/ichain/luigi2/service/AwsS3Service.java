package jp.co.ichain.luigi2.service;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.util.IOUtils;
import jp.co.ichain.luigi2.dao.AwsS3Dao;
import jp.co.ichain.luigi2.exception.WebDataException;
import jp.co.ichain.luigi2.mapper.DocumentsMapper;
import jp.co.ichain.luigi2.resources.Luigi2Code;
import jp.co.ichain.luigi2.resources.Luigi2TableInfo;
import jp.co.ichain.luigi2.resources.Luigi2TableInfo.TableInfo;
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

  @Value("${aws.s3.salt}")
  String salt;

  @Autowired
  AwsS3Dao awsS3Dao;

  @Autowired
  DocumentsMapper documentsMapper;

  /**
   * S3にファイルをアップロードする
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-07-01
   * @updatedAt : 2021-07-01
   * @param documents
   * @param file
   * @param tenantId
   * @param ownerCode
   * @param updatedBy
   * @throws InvalidKeyException
   * @throws NoSuchAlgorithmException
   * @throws IllegalBlockSizeException
   * @throws BadPaddingException
   * @throws InvalidAlgorithmParameterException
   * @throws NoSuchPaddingException
   * @throws InvalidKeySpecException
   * @throws DecoderException
   * @throws IOException
   */
  @Transactional(transactionManager = "luigi2TransactionManager", rollbackFor = Exception.class)
  public void upload(Documents documents, MultipartFile file, Integer tenantId, Object ownerCode,
      Object updatedBy) throws InvalidKeyException, NoSuchAlgorithmException,
      IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException,
      NoSuchPaddingException, InvalidKeySpecException, DecoderException, IOException {

    // file name
    val fileName = Base64.encodeBase64String(file.getOriginalFilename().getBytes("UTF-8"));

    // db insert
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put("document", documents.name);
    dataMap.put("fileName", fileName);
    dataMap.put("documentTitle", file.getOriginalFilename());
    dataMap.put("tenantId", tenantId);
    dataMap.put("ownerCode", ownerCode);
    dataMap.put("updatedBy", updatedBy);

    switch (documents) {
      case NEW_BUSINESS:
        dataMap.putAll(Luigi2TableInfo.getLockTable(TableInfo.NewBusinessDocuments));
        break;
      case CLAIM:
        dataMap.putAll(Luigi2TableInfo.getLockTable(TableInfo.ClaimDocuments));
        break;
      case MAINTENANCE:
        dataMap.putAll(Luigi2TableInfo.getLockTable(TableInfo.MaintenanceDocuments));
        break;
      default:
        throw new WebDataException(Luigi2Code.D0001, documents.name);
    }
    documentsMapper.insertDocuments(dataMap);

    // file upload
    awsS3Dao.upload(documents.name + dataMap.get("id") + "_" + fileName, file.getInputStream());
  }

  /**
   * S3にファイルをアップロードする
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-07-01
   * @updatedAt : 2021-07-01
   * @param documents
   * @param fileList
   * @param tenantId
   * @param ownerCode
   * @param updatedBy
   * @throws InvalidKeyException
   * @throws NoSuchAlgorithmException
   * @throws IllegalBlockSizeException
   * @throws BadPaddingException
   * @throws InvalidAlgorithmParameterException
   * @throws NoSuchPaddingException
   * @throws InvalidKeySpecException
   * @throws DecoderException
   * @throws IOException
   */
  public void upload(Documents documents, List<MultipartFile> fileList, Integer tenantId,
      Object ownerCode, Object updatedBy) throws InvalidKeyException, NoSuchAlgorithmException,
      IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException,
      NoSuchPaddingException, InvalidKeySpecException, DecoderException, IOException {
    for (val file : CollectionUtils.safe(fileList)) {
      this.upload(documents, file, tenantId, ownerCode, updatedBy);
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
   * @throws DecoderException
   * @throws InvalidKeySpecException
   * @throws NoSuchPaddingException
   * @throws InvalidAlgorithmParameterException
   * @throws BadPaddingException
   * @throws IllegalBlockSizeException
   * @throws NoSuchAlgorithmException
   * @throws InvalidKeyException
   * @throws IOException
   * @throws AmazonServiceException
   * @throws SdkClientException
   * @throws Exception
   */
  public ResponseEntity<Resource> download(String url)
      throws InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException,
      BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException,
      InvalidKeySpecException, DecoderException, IOException {
    val s3stream = awsS3Dao.download(url);
    val result = new ByteArrayResource(IOUtils.toByteArray(s3stream));
    var fileName = url.split("/")[1];
    fileName = new String(Base64.decodeBase64(
        fileName.substring(fileName.indexOf('_') + 1, fileName.length()).getBytes("UTF-8")));

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s", fileName))
        .contentType(MediaType.valueOf(new Tika().detect(s3stream))).body(result);
  }
}
