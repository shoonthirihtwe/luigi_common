package jp.co.ichain.luigi2.service;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.ArrayList;
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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import jp.co.ichain.luigi2.dao.AwsS3Dao;
import jp.co.ichain.luigi2.exception.WebDataException;
import jp.co.ichain.luigi2.mapper.DocumentsMapper;
import jp.co.ichain.luigi2.resources.Luigi2ErrorCode;
import jp.co.ichain.luigi2.resources.Luigi2TableInfo;
import jp.co.ichain.luigi2.resources.Luigi2TableInfo.TableInfo;
import jp.co.ichain.luigi2.resources.ServiceInstancesResources;
import jp.co.ichain.luigi2.util.CollectionUtils;
import jp.co.ichain.luigi2.vo.DownloadFileVo;
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

  private static final String FREE_DOCUMENTS = "documents";

  public enum Documents {
    CLAIM("claim_documents/"), NEW_BUSINESS("new_business_documents/"), MAINTENANCE(
        "maintenance_documents/");

    String name;

    Documents(String name) {
      this.name = name;
    }
  }

  public enum FreeDocumentsType {
    Text("text");

    String name;

    FreeDocumentsType(String name) {
      this.name = name;
    }
  }

  public static final int FB_CLAIMS = 0;

  public static final int ACCOUNT_JOURNAL = 1;

  public static final int RESERVE_PAYMENT = 2;

  public enum FreeDocumentsFileType {

    FB_CLAIMS("FB_claims"), ACCOUNT_JOURNAL("account_journal"), RESERVE_PAYMENT("reserve_payment");

    String name;

    FreeDocumentsFileType(String name) {
      this.name = name;

    }
  }

  @Value("${aws.s3.salt}")
  String salt;

  @Autowired
  AwsS3Dao awsS3Dao;

  @Autowired
  DocumentsMapper documentsMapper;

  @Autowired
  ServiceInstancesResources siResources;

  @Autowired
  ServiceInstancesResources serviceInstancesResources;

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
        throw new WebDataException(Luigi2ErrorCode.D0001, documents.name);
    }
    documentsMapper.insertDocuments(dataMap);

    // file upload
    awsS3Dao.upload(documents.name + dataMap.get("id") + "_" + fileName, file.getInputStream());
  }

  /**
   * フリーファイルアップロード
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-07-05
   * @updatedAt : 2021-07-05
   * @param file
   * @param documentsType
   * @param tenantId
   * @param year
   * @param month
   * @throws IOException
   */
  public void upload(MultipartFile file, FreeDocumentsType documentsType, int tenantId, int year,
      int month) throws IOException {

    val serviceMap = serviceInstancesResources.get(tenantId, FREE_DOCUMENTS);
    val folder = serviceMap.get(0).getInherentMap().get(documentsType.name);

    // file name
    val fileName = file.getOriginalFilename();

    StringBuffer sb = new StringBuffer();
    sb.append(folder).append(tenantId).append("/").append(year).append("/").append(month)
        .append("/").append(fileName);

    // file upload
    awsS3Dao.upload(new String(sb), file.getInputStream());
  }

  /**
   * フリーファイルアップロード
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-07-05
   * @updatedAt : 2021-07-05
   * @param fileList
   * @param documentsType
   * @param tenantId
   * @param year
   * @param month
   * @throws IOException
   */
  public void upload(List<MultipartFile> fileList, FreeDocumentsType documentsType, int tenantId,
      int year, int month) throws IOException {
    for (val file : CollectionUtils.safe(fileList)) {
      this.upload(file, documentsType, tenantId, year, month);
    }
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

  /**
   * S3ダウンロードファイル 検索
   *
   * @author : [AOT] g.kim
   * @createdAt : 2021-07-02
   * @updatedAt : 2021-07-02
   * @param documents
   * @param id
   * @return
   * @throws JsonProcessingException
   * @throws JsonMappingException
   * @throws ParseException
   * @throws SdkClientException
   * @throws AmazonServiceException
   */
  @SuppressWarnings("unchecked")
  public List<DownloadFileVo> searchDownloadDocument(FreeDocumentsType documentsType,
      Map<String, Object> paramMap) throws JsonMappingException, JsonProcessingException,
      AmazonServiceException, SdkClientException, ParseException {
    val serviceInstance = siResources.get((Integer) paramMap.get("tenantId"), FREE_DOCUMENTS);
    String documentDir = serviceInstance.get(0).getInherentMap().get(documentsType.name).toString();

    List<String> fileTags = new ArrayList<String>();
    val tagList = (List<String>) paramMap.get("fileTagList");

    for (int i = 0; i < tagList.size(); i++) {
      switch (i) {
        case FB_CLAIMS:
          if (Integer.parseInt(tagList.get(i)) != 0) {
            fileTags.add(FreeDocumentsFileType.FB_CLAIMS.name);
          }
          break;
        case ACCOUNT_JOURNAL:
          if (Integer.parseInt(tagList.get(i)) != 0) {
            fileTags.add(FreeDocumentsFileType.ACCOUNT_JOURNAL.name);
          }
          break;
        case RESERVE_PAYMENT:
          if (Integer.parseInt(tagList.get(i)) != 0) {
            fileTags.add(FreeDocumentsFileType.RESERVE_PAYMENT.name);
          }
          break;
        default:
          throw new WebDataException(Luigi2ErrorCode.D0001, "fileTagList");
      }
    }

    return awsS3Dao.searchFile(paramMap, documentDir, fileTags);
  }


}
