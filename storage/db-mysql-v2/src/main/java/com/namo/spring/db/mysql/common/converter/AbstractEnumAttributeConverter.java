package com.namo.spring.db.mysql.common.converter;

import jakarta.persistence.AttributeConverter;

import org.springframework.util.StringUtils;

import com.namo.spring.db.mysql.common.utils.CodedEnumConverterUtil;

import lombok.Getter;

@Getter
public class AbstractEnumAttributeConverter<E extends Enum<E> & CodedEnum> implements AttributeConverter<E, String>{

	/**
	 * 변환 대상 Enum 클래스
	 */
	private final Class<E> targetEnumClass;

	/**
	 * null 허용 여부
	 * <p>
	 * {@code nullable = false}인 경우, 변환 대상이 null일 때 예외를 발생시킨다. <br/>
	 * {@code nullable = true}인 경우, 변환 대상이 null일 때 null을 반환시키며, <br/>
	 * code 반환시 빈 문자열을 반환한다.
	 */
	private final boolean nullable;

	/**
	 * {@code nullable = false}인 경우 오류 메시지에 출력될 Enum 이름
	 */
	private final String enumName;

	public AbstractEnumAttributeConverter(Class<E> targetEnumClass, boolean nullable, String enumName) {
		this.targetEnumClass = targetEnumClass;
		this.nullable = nullable;
		this.enumName = enumName;
	}

	@Override
	public String convertToDatabaseColumn(E attribute) {
		if (!nullable && attribute == null) {
			throw new IllegalArgumentException(String.format("%s을(를) null로 변환할 수 없습니다.", enumName));
		}
		return CodedEnumConverterUtil.toCode(attribute);
	}

	@Override
	public E convertToEntityAttribute(String dbData) {
		if (!nullable && !StringUtils.hasText(dbData)) {
			throw new IllegalArgumentException(String.format("%s(이)가 DB에 null 혹은 Empty로 저장되어 있습니다. DB data = %s", enumName, dbData));
		}
		return CodedEnumConverterUtil.toEnum(targetEnumClass, dbData);
	}
}
