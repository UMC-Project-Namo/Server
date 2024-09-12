package com.namo.spring.application.external.global.common.security.jwt;

import java.util.function.Function;

import com.namo.spring.core.infra.common.jwt.JwtClaims;

public class JwtClaimsParserUtil {

    private JwtClaimsParserUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * JWT Claims에서 주어진 키에 해당하는 값을 반환합니다.
     * <p> <br>
     * 동작 방식: <br>
     * 1. {@link JwtClaims}에서 주어진 키에 해당하는 값을 찾습니다. <br>
     * 2. 찾은 값이 null이 아니고, 주어진 타입과 일치하는지 확인합니다. <br>
     * 3. 값이 주어진 타입과 일치하면, 해당 값을 반환합니다. <br>
     * 4. 값이 없거나 주어진 타입과 일치하지 않으면 null을 반환합니다. <br>
     *
     * @param <T>       반환할 값의 타입
     * @param jwtClaims 검색할 JWT Claims
     * @param key       검색할 키
     * @param type      반환할 값의 클래스
     * @return 키에 해당하는 값. 값이 없거나 주어진 타입과 일치하지 않으면 null을 반환합니다.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getClaimValue(JwtClaims jwtClaims, String key, Class<T> type) {
        Object value = jwtClaims.getClaims().get(key);
        if (value != null && type.isAssignableFrom(value.getClass())) {
            return (T)value;
        }
        return null;
    }

    /**
     * JWT Claims에서 주어진 키에 해당하는 값을 반환합니다.
     * <p> <br>
     * 동작 방식: <br>
     * 1. {@link JwtClaims}에서 주어진 키에 해당하는 값을 찾습니다. <br>
     * 2. 찾은 값이 null이 아니면, 주어진 변환 함수를 적용하여 값을 변환합니다. <br>
     * 3. 변환된 값을 반환합니다. <br>
     * 4. 값이 없으면 null을 반환합니다. <br>
     *
     * @param <T>       반환할 값의 타입
     * @param jwtClaims 검색할 JWT Claims
     * @param key       검색할 키
     * @param converter 값 변환 함수
     * @return 키에 해당하는 값. 값이 없으면 null을 반환합니다.
     */
    public static <T> T getClaimValue(JwtClaims jwtClaims, String key, Function<String, T> converter) {
        Object value = jwtClaims.getClaims().get(key);
        if (value != null) {
            return converter.apply(value.toString());
        }
        return null;
    }
}
