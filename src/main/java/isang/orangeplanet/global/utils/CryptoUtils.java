package isang.orangeplanet.global.utils;

import jakarta.annotation.PostConstruct;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * CryptoUtils : 암호화/복호화 관련 유틸 클래스
 */
@Component
public class CryptoUtils {

  @Value("${crypto.private-key}")
  private String nonPrivateKey;

  @Value("${crypto.transformation}")
  private String nonTransformation;

  private static String privateKey;
  private static String transformation;

  @PostConstruct
  public void init() {
    privateKey = this.nonPrivateKey;
    transformation = this.nonTransformation;
  }

  /**
   * 암호화 메서드
   * @param plainText : 평문 데이터
   * @return : 암호화된 데이터 반환
   */
  public static String encrypt(String plainText) {
    try {
      SecretKeySpec secretKey = new SecretKeySpec(privateKey.getBytes(StandardCharsets.UTF_8), "AES");
      IvParameterSpec IV = new IvParameterSpec(privateKey.substring(0, 16).getBytes());

      Cipher cipher = Cipher.getInstance(transformation);
      cipher.init(Cipher.ENCRYPT_MODE, secretKey, IV);
      byte[] encryptByte = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
      return Hex.encodeHexString(encryptByte);
    } catch (Exception e) {
      throw new RuntimeException("암호화 과정에서 오류가 발생했습니다.");
    }
  }

  /**
   * 복호화 메서드
   * @param cipherText : 암호화 데이터
   * @return : 복호화된 평문 데이터 반환
   */
  public static String decrypt(String cipherText) {
    try {
      SecretKeySpec secretKey = new SecretKeySpec(privateKey.getBytes(StandardCharsets.UTF_8), "AES");
      IvParameterSpec IV = new IvParameterSpec(privateKey.substring(0, 16).getBytes());

      Cipher cipher = Cipher.getInstance(transformation);
      cipher.init(Cipher.DECRYPT_MODE, secretKey, IV);
      byte[] decryptByte = Hex.decodeHex(cipherText.toCharArray());
      return new String(cipher.doFinal(decryptByte), StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new RuntimeException("복호화 과정에서 오류가 발생했습니다.");
    }
  }
}
