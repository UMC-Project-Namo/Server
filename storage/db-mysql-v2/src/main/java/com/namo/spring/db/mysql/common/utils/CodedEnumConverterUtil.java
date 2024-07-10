package com.namo.spring.db.mysql.common.utils;

import java.util.EnumSet;

import org.springframework.util.StringUtils;

import com.namo.spring.db.mysql.common.converter.CodedEnum;

import lombok.NoArgsConstructor;

/**
 * {@link CodedEnum}을 변환하는 유틸리티 클래스
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class CodedEnumConverterUtil {

	/**
	 * 주어진 코드에 해당하는 Enum 값을 반환합니다.
	 *
	 * <p>이 메서드는 {@code enumClass}에 정의된 모든 Enum 값을 순회하며, 각 Enum 값의 {@code getCode()} 메서드를 호출하여
	 * 주어진 {@code code}와 일치하는 Enum 값를 찾습니다. 일치하는 Enum 값이 있으면 해당 Enum 값를 반환합니다.
	 * 일치하는 Enum 값이 없으면 {@link IllegalArgumentException}을 발생시킵니다.
	 *
	 * @param <T>       Enum 타입
	 * @param enumClass 변환할 Enum class
	 * @param code      변환할 코드
	 * @return 코드에 해당하는 Enum 값
	 * @throws IllegalArgumentException 주어진 코드에 해당하는 Enum 값이 없을 경우
	 */
	public static <T extends Enum<T> & CodedEnum> T toEnum(Class<T> enumClass, String code) {
		if (!StringUtils.hasText(code))
			return null;
		return EnumSet.allOf(enumClass).stream()
			.filter(e -> e.getCode().equals(code))
			.findFirst()
			.orElseThrow(
				() -> new IllegalArgumentException("enum class " + enumClass.getName() + " has no code " + code)
			);
	}

	/**
	 * 주어진 Enum 값의 코드 값을 반환합니다.
	 *
	 * <p>이 메서드는 {@code codedEnum}의 {@code getCode()} 메서드를 호출하여 코드 값을 추출합니다.
	 * {@code codedEnum}이 {@code null}인 경우, {@code null}을 반환합니다.
	 *
	 * @param <T>       Enum 타입
	 * @param codedEnum 코드 값을 추출할 Enum 값
	 * @return Enum 값의 코드 값. {@code codedEnum}이 {@code null}인 경우, {@code null}.
	 */
	public static <T extends Enum<T> & CodedEnum> String toCode(T codedEnum) {
		return codedEnum == null ? null : codedEnum.getCode();
	}
}
