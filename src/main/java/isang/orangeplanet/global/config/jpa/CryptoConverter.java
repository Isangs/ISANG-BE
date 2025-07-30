package isang.orangeplanet.global.config.jpa;

import isang.orangeplanet.global.utils.CryptoUtils;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.util.StringUtils;

/**
 * CryptoConverter : JPA 암호화 Converter
 */
@Converter
public class CryptoConverter implements AttributeConverter<String, String> {

  /**
   * 평문을 암호화 시키는 메서드
   * @param text : 평문
   * @return : 저장될 암호문 반환
   */
  @Override
  public String convertToDatabaseColumn(String text) {
    return (!StringUtils.hasText(text)) ? null : CryptoUtils.encrypt(text);
  }

  /**
   * 암호화된 데이터를 다시 평문으로 복호화 시키는 메서드
   * @param dbData : 암호화 데이터
   * @return : 평문 반환
   */
  @Override
  public String convertToEntityAttribute(String dbData) {
    return (!StringUtils.hasText(dbData)) ? null : CryptoUtils.decrypt(dbData);
  }
}
