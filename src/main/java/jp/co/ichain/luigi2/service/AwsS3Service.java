package jp.co.ichain.luigi2.service;

import java.io.IOException;
import java.io.InputStream;
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
import jp.co.ichain.luigi2.resources.ServiceInstancesBaseResources;
import jp.co.ichain.luigi2.vo.DownloadFileVo;
import lombok.val;

/**
 * S3サービス
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-06-30
 * @updatedAt : 2022-09-14
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

  public static final int FB_CLAIMS = 0;

  public static final int ACCOUNT_JOURNAL = 1;

  public static final int RESERVE_PAYMENT = 2;

  /**
   * 経理ダウンロードファイルEnumType
   * 
   * FB_claims:支払用FBデータ account_journal:会計仕訳データ reserve_payment:支払備金データ commission_summary:代理店手数料集計
   * commission_detail:代理店手数料明細
   * 
   * @author : [AOT] g.kim
   * @createdAt : 2021-08-19
   * @updatedAt : 2021-08-19
   */
  public enum FreeDocumentsFileType {

    FB_CLAIMS("FB_claims"), ACCOUNT_JOURNAL("account_journal"), RESERVE_PAYMENT(
        "reserve_payment"), COMMISSION_SUMMARY(
            "commission_summary"), COMMISSION_DETAIL("commission_detail");

    String name;

    FreeDocumentsFileType(String name) {
      this.name = name;

    }
  }

  @Value("${aws.s3.salt}")
  String salt;

  @Value("${luigi2.s3.text.path}")
  String textPath;

  @Autowired
  AwsS3Dao awsS3Dao;

  @Autowired
  DocumentsMapper documentsMapper;

  @Autowired
  ServiceInstancesBaseResources siResources;

  @Autowired
  ServiceInstancesBaseResources serviceInstancesBaseResources;

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
  public void upload(Documents documents, String orgKey, String fileName, Integer tenantId,
      Object ownerCode, Object updatedBy) throws InvalidKeyException, NoSuchAlgorithmException,
      IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException,
      NoSuchPaddingException, InvalidKeySpecException, DecoderException, IOException {

    // file name
    if (orgKey == null) {
      return;
    }

    val encodeFileName = Base64.encodeBase64String(fileName.getBytes("UTF-8"));
    // db insert
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put("document", documents.name);
    dataMap.put("encodeFileName", encodeFileName);
    dataMap.put("documentTitle", fileName);
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

    // file move
    awsS3Dao.move(tenantId, orgKey, documents.name + dataMap.get("id") + "_" + encodeFileName);
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
  public void upload(InputStream inputsteam, String fileName, int tenantId, int year, int month)
      throws IOException {

    StringBuffer sb = new StringBuffer();
    sb.append(textPath).append(year).append("/").append(month).append("/").append(fileName);

    // file upload
    awsS3Dao.upload(tenantId, new String(sb), inputsteam);
  }

  /**
   * S3からファイルをダウンロードする
   * 
   * @author : [AOT] s.paku
   * @createdAt : 2021-06-30
   * @updatedAt : 2021-06-30
   * @param tenantId
   * @param url
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
  public ResponseEntity<Resource> download(Integer tenantId, String url)
      throws InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException,
      BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException,
      InvalidKeySpecException, DecoderException, IOException {
    val s3stream = awsS3Dao.download(tenantId, url);
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
  public List<DownloadFileVo> searchDownloadDocument(Map<String, Object> paramMap)
      throws JsonMappingException, JsonProcessingException, AmazonServiceException,
      SdkClientException, ParseException {

    List<String> fileTags = new ArrayList<String>();

    if (paramMap.get("fbClaims") != null) {
      fileTags.add(FreeDocumentsFileType.FB_CLAIMS.name);
    }

    if (paramMap.get("accountJournal") != null) {
      fileTags.add(FreeDocumentsFileType.ACCOUNT_JOURNAL.name);
    }

    if (paramMap.get("reservePayment") != null) {
      fileTags.add(FreeDocumentsFileType.RESERVE_PAYMENT.name);
    }

    if (paramMap.get("commissionSummary") != null) {
      fileTags.add(FreeDocumentsFileType.COMMISSION_SUMMARY.name);
    }

    if (paramMap.get("commissionDetail") != null) {
      fileTags.add(FreeDocumentsFileType.COMMISSION_DETAIL.name);
    }

    if (fileTags.size() == 0) {
      for (val fileType : FreeDocumentsFileType.values()) {
        fileTags.add(fileType.name);
      }

    }

    return awsS3Dao.searchFile(paramMap, textPath, fileTags);
  }
}
