package com.namo.spring.core.infra.common.jwt;

import java.time.LocalDateTime;

import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;

public interface JwtProvider {
    /**
     * 헤더로부터 토큰을 추출하고 유효선을 검증하는 메서드
     *
     * @param header : Authorization 헤더
     * @return 값이 있다면 토큰, 없다면 빈 문자열
     */
    default String resolveToken(String header) {
        if (StringUtils.hasText(header) && header.startsWith(AuthConstants.TOKEN_PREFIX.getValue())) {
            return header.substring(AuthConstants.TOKEN_PREFIX.getValue().length());
        }
        return "";
    }

    /**
     * 토큰을 생성하는 메서드
     *
     * @param claims : 토큰에 담을 정보
     * @return 생성된 토큰
     */
    String createToken(JwtClaims claims);

    /**
     * 토큰을 파싱하여 JwtClaims 객체로 변환하는 메서드
     *
     * @param token : 토큰
     * @return 사용자 정보를 담은 {@link JwtClaims} 객체
     */
    JwtClaims parseJwtClaimsFromToken(String token);

    /**
     * 토큰의 만료일을 반환하는 메서드
     *
     * @param token : 토큰
     * @return 토큰의 만료일
     */
    LocalDateTime getExpiredDate(String token);

    /**
     * 토큰이 만료되었는지 확인하는 메서드
     *
     * @param token : 토큰
     * @return 만료되었다면 true, 아니라면 false
     */
    boolean isTokenExpired(String token);

    /**
     * 토큰에서 클레임을 추출하는 메서드
     *
     * @param token : 토큰
     * @return 사용자 정보를 담은 {@link Claims} 객체
     */
    Claims getClaimsFromToken(String token);
}
