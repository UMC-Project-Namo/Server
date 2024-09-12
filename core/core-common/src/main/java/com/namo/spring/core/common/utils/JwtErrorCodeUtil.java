package com.namo.spring.core.common.utils;

import java.security.SignatureException;
import java.util.Map;
import java.util.Optional;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.exception.JwtException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * JwtErrorCodeUtil 클래스는 JWT 관련 예외를 처리하는 유틸리티 클래스입니다. <br/>
 * 이 클래스는 주어진 예외에 대한 적절한 {@link ErrorStatus}를 결정하고, {@link JwtException}을 생성하는 메서드를 제공합니다.
 *
 * @author luke0408
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtErrorCodeUtil {
    // FIXME : 2024.07.02. 추후에 ErrorStatus도 한번 더 추상화 하여 Exception 종류별로 분리해야함 - 루카
    private static final Map<Class<? extends Exception>, ErrorStatus> ERROR_CODE_MAP = Map.of(
            ExpiredJwtException.class, ErrorStatus.EXPIRED_TOKEN,
            MalformedJwtException.class, ErrorStatus.MALFORMED_TOKEN,
            SignatureException.class, ErrorStatus.TAMPERED_TOKEN,
            UnsupportedJwtException.class, ErrorStatus.UNSUPPORTED_JWT_TOKEN
    );

    /**
     * 주어진 예외에 대한 적절한 {@link ErrorStatus}를 결정하는 메서드입니다.
     *
     * @param exception        {@link Exception} : 발생한 예외
     * @param defaultErrorCode {@link ErrorStatus} : 기본 오류 코드
     * @return {@link ErrorStatus}
     */
    public static ErrorStatus determineErrorCode(Exception exception, ErrorStatus defaultErrorCode) {
        if (exception instanceof JwtException jwtException)
            return jwtException.getErrorCode();

        Class<? extends Exception> exceptionClass = exception.getClass();
        return ERROR_CODE_MAP.getOrDefault(exceptionClass, defaultErrorCode);
    }

    /**
     * 예외에 해당하는 {@link JwtException}을 반환합니다.
     * <p>
     * 기본 오류 코드는 500 INTERNAL_SERVER_ERROR 입니다. <br/>
     * 해당 메서드는 {@link #determineErrorCode(Exception, ErrorStatus)} 메서드를 사용합니다.
     *
     * @param exception {@link Exception} : 발생한 예외
     * @return {@link JwtException}
     */
    public static JwtException determineAuthErrorException(Exception exception) {
        return findAuthErrorException(exception).orElseGet(
                () -> {
                    ErrorStatus errorStatus = determineErrorCode(exception, ErrorStatus.INTERNAL_SERVER_ERROR);
                    return new JwtException(errorStatus);
                }
        );
    }

    private static Optional<JwtException> findAuthErrorException(Exception exception) {
        if (exception instanceof JwtException) {
            return Optional.of((JwtException)exception);
        } else if (exception.getCause() instanceof JwtException) {
            return Optional.of((JwtException)exception.getCause());
        }
        return Optional.empty();
    }
}
