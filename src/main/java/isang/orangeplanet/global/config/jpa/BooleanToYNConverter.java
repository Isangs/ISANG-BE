package isang.orangeplanet.global.config.jpa;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * BooleanToYNConverter : JPA Boolean Converter
 */
@Converter
public class BooleanToYNConverter implements AttributeConverter<Boolean, String> {

  /**
   * DB에 데이터가 저장될때 호출
   * @param attribute : Boolean 데이터
   * @return : 저장될 값 반환
   */
  @Override
  public String convertToDatabaseColumn(Boolean attribute) {
    return (attribute != null && attribute) ? "Y" : "N";
  }

  /**
   * DB 데이터를 추출할때 호출
   * @param dbData : DB 데이터
   * @return : Boolean 반환
   */
  @Override
  public Boolean convertToEntityAttribute(String dbData) {
    return "Y".equals(dbData);
  }
}
