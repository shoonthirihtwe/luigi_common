package jp.co.ichain.luigi2.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import lombok.val;

/**
 * 暗号化ユーティリティー
 * 
 * @author : [AOT] s.paku
 * @createdAt : 2021-05-25
 * @updatedAt : 2021-05-25
 */
public class EncryptUtils {

  static final String DEFAULT_IV = "be1a0ce9c2b375264d06ad3090952f51";
  static final int DEFAULT_ITERATION_COUNT = 1000;
  static final int DEFAULT_KEY_SIZE = 128;

  /**
   * 文字列を暗号化する
   *
   * @author : [AOT] s.park
   * @createdAt : 2021-05-25
   * @updatedAt : 2021-05-25
   * @param source
   * @return
   * @throws NoSuchAlgorithmException
   */
  public static String getSha256(String source) throws NoSuchAlgorithmException {
    val md = MessageDigest.getInstance("SHA-256");
    md.update(source.getBytes());
    return new String(Hex.encodeHex(md.digest()));
  }

  public static String encodeAes256(String salt, String iv, String passphrase, String text,
      int iterationCount, int keySize)
      throws NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException,
      InvalidKeyException, InvalidAlgorithmParameterException, DecoderException,
      NoSuchPaddingException, UnsupportedEncodingException, InvalidKeySpecException {
    val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    val spec = new PBEKeySpec(passphrase.toCharArray(), Hex.decodeHex(salt.toCharArray()),
        iterationCount, keySize);
    val key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(Hex.decodeHex(iv.toCharArray())));

    val encrypted = cipher.doFinal(text.getBytes("UTF-8"));
    return new String(Base64.encodeBase64(encrypted));
  }

  /**
   * キーを元に文字列を暗号化する
   *
   * @author : [AOT] s.park
   * @createdAt : 2021-05-25
   * @updatedAt : 2021-05-25
   * @param key
   * @param str
   * @return
   * @throws NoSuchAlgorithmException
   * @throws IllegalBlockSizeException
   * @throws BadPaddingException
   * @throws InvalidKeyException
   * @throws InvalidAlgorithmParameterException
   * @throws DecoderException
   * @throws NoSuchPaddingException
   * @throws UnsupportedEncodingException
   * @throws InvalidKeySpecException
   */
  public static String encodeAes256(String key, String str)
      throws NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException,
      InvalidKeyException, InvalidAlgorithmParameterException, DecoderException,
      NoSuchPaddingException, UnsupportedEncodingException, InvalidKeySpecException {

    return encodeAes256(key, DEFAULT_IV, key, str, DEFAULT_ITERATION_COUNT, DEFAULT_KEY_SIZE);
  }

  /**
   * 複合化する
   *
   * @author : [AOT] s.park
   * @createdAt : 2021-05-25
   * @updatedAt : 2021-05-25
   * @param salt
   * @param iv
   * @param passphrase
   * @param ciphertext
   * @param iterationCount
   * @param keySize
   * @return
   * @throws NoSuchAlgorithmException
   * @throws IllegalBlockSizeException
   * @throws BadPaddingException
   * @throws InvalidKeyException
   * @throws InvalidAlgorithmParameterException
   * @throws DecoderException
   * @throws NoSuchPaddingException
   * @throws UnsupportedEncodingException
   * @throws InvalidKeySpecException
   */
  public static String decodeAes256(String salt, String iv, String passphrase, String ciphertext,
      int iterationCount, int keySize)
      throws NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException,
      InvalidKeyException, InvalidAlgorithmParameterException, DecoderException,
      NoSuchPaddingException, UnsupportedEncodingException, InvalidKeySpecException {
    val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    val spec = new PBEKeySpec(passphrase.toCharArray(), Hex.decodeHex(salt.toCharArray()),
        iterationCount, keySize);
    val key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(Hex.decodeHex(iv.toCharArray())));
    val decrypted = cipher.doFinal(Base64.decodeBase64(ciphertext.getBytes()));
    return new String(decrypted, "UTF-8");
  }

  /**
   * キーを元に複合化する
   *
   * @author : [AOT] s.park
   * @createdAt : 2021-05-25
   * @updatedAt : 2021-05-25
   * @param key
   * @param str
   * @return
   * @throws NoSuchAlgorithmException
   * @throws IllegalBlockSizeException
   * @throws BadPaddingException
   * @throws InvalidKeyException
   * @throws InvalidAlgorithmParameterException
   * @throws DecoderException
   * @throws NoSuchPaddingException
   * @throws UnsupportedEncodingException
   * @throws InvalidKeySpecException
   */
  public static String decodeAes256(String key, String str)
      throws NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException,
      InvalidKeyException, InvalidAlgorithmParameterException, DecoderException,
      NoSuchPaddingException, UnsupportedEncodingException, InvalidKeySpecException {

    return decodeAes256(key, DEFAULT_IV, key, str, DEFAULT_ITERATION_COUNT, DEFAULT_KEY_SIZE);
  }

}
