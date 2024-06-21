package com.namo.spring.application.external.api.user.helper;

import java.time.Duration;
import java.time.LocalDateTime;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import com.namo.spring.application.external.global.common.annotation.AccessTokenStrategy;
import com.namo.spring.application.external.global.common.annotation.RefreshTokenStrategy;
import com.namo.spring.application.external.global.common.security.jwt.JwtClaimsParserUtil;
import com.namo.spring.application.external.global.common.security.jwt.Jwts;
import com.namo.spring.application.external.global.common.security.jwt.access.AccessTokenClaim;
import com.namo.spring.application.external.global.common.security.jwt.refresh.RefreshTokenClaim;
import com.namo.spring.application.external.global.common.security.jwt.refresh.RefreshTokenClaimKeys;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.exception.AuthException;
import com.namo.spring.core.infra.common.jwt.JwtClaims;
import com.namo.spring.core.infra.common.jwt.JwtProvider;
import com.namo.spring.db.mysql.domains.user.domain.User;
import com.namo.spring.db.redis.cache.forbidden.ForbiddenTokenService;
import com.namo.spring.db.redis.cache.refresh.RefreshToken;
import com.namo.spring.db.redis.cache.refresh.RefreshTokenService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthHelper {
	private final JwtProvider accessTokenProvider;
	private final JwtProvider refreshTokenProvider;
	private final RefreshTokenService refreshTokenService;
	private final ForbiddenTokenService forbiddenTokenService;

	public JwtAuthHelper(
		@AccessTokenStrategy JwtProvider accessTokenProvider,
		@RefreshTokenStrategy JwtProvider refreshTokenProvider,
		RefreshTokenService refreshTokenService,
		ForbiddenTokenService forbiddenTokenService
	) {
		this.accessTokenProvider = accessTokenProvider;
		this.refreshTokenProvider = refreshTokenProvider;
		this.refreshTokenService = refreshTokenService;
		this.forbiddenTokenService = forbiddenTokenService;
	}

	/**
	 * 사용자 정보를 기본으로 accessToken, refreshToken을 생성한다. <br>
	 * refreshToken은 redis에 저장한다.
	 *
	 * @param user : {@link User}
	 * @return {@link Jwts}
	 */
	public Jwts createToken(User user) {
		String accessToken = accessTokenProvider.createToken(AccessTokenClaim.of(user.getId()));
		String refreshToken = refreshTokenProvider.createToken(RefreshTokenClaim.of(user.getId()));

		refreshTokenService.save(
			RefreshToken.of(
				user.getId(), refreshToken,
				toSeconds(refreshTokenProvider.getExpiredDate(refreshToken))
			)
		);

		return Jwts.of(accessToken, refreshToken);
	}

	/**
	 * 주어진 refreshToken을 사용하여 새로운 accessToken과 refreshToken을 생성합니다.
	 * <p> <br/>
	 * 동작 방식: <br/>
	 * 1. 주어진 refreshToken에서 JWT Claims를 파싱합니다. <br/>
	 * 2. 파싱된 Claims에서 사용자 ID를 추출합니다. <br/>
	 * 3. 새로운 refreshToken을 생성하고, 이를 Redis에 저장합니다. <br/>
	 * 4. 새로운 accessToken을 생성합니다. <br/>
	 *
	 * @param refreshToken: 새로운 토큰을 생성하기 위한 기존 refreshToken
	 * @return 사용자 ID와 새로 생성된 토큰(Jwts)을 Pair로 반환합니다.
	 * @throws AuthException <br/>
	 *                       - {@link ErrorStatus#EXPIRATION_TOKEN} : 토큰이 만료되었을 때 <br/>
	 *                       - {@link ErrorStatus#TAKEN_AWAY_TOKEN} : 토큰이 취소되었을 때
	 */
	public Pair<Long, Jwts> refresh(String refreshToken) {
		JwtClaims claims = refreshTokenProvider.parseJwtClaimsFromToken(refreshToken);

		Long userId = JwtClaimsParserUtil.getClaimValue(claims,
			RefreshTokenClaimKeys.USER_ID.getValue(),
			Long::parseLong);

		RefreshToken newRefreshToken;
		try {
			newRefreshToken = refreshTokenService.refresh(userId, refreshToken,
				refreshTokenProvider.createToken(RefreshTokenClaim.of(userId)));
		} catch (IllegalArgumentException e) {
			throw new AuthException(ErrorStatus.EXPIRATION_TOKEN);
		} catch (IllegalStateException e) {
			throw new AuthException(ErrorStatus.TAKEN_AWAY_TOKEN);
		}

		String newAccessToken = accessTokenProvider.createToken(AccessTokenClaim.of(userId));

		return Pair.of(userId, Jwts.of(newAccessToken, newRefreshToken.getToken()));
	}

	/**
	 * userId에 연결된 accessToken, refreshToken을 삭제합니다.
	 *
	 * @param userId : 사용자 ID
	 * @param accessToken : 삭제할 accessToken
	 * @param refreshToken : 삭제할 refreshToken
	 * @throws AuthException <br/>
	 * 					 - {@link ErrorStatus#EXPIRATION_TOKEN} : 토큰이 만료되었을 때Q
	 */
	public void removeJwtsToken(Long userId, String accessToken, String refreshToken) {
		JwtClaims jwtClaims = null;
		if (refreshToken != null) {
			try {
				jwtClaims = refreshTokenProvider.parseJwtClaimsFromToken(refreshToken);
			} catch (IllegalArgumentException e) {
				log.error("RefreshToken parsing error: {}", e.getMessage());
				throw new AuthException(ErrorStatus.EXPIRATION_TOKEN);
			}
		}

		if (jwtClaims != null) {
			deleteRefreshToken(userId, jwtClaims, refreshToken);
		}

		deleteAccessToken(userId, accessToken);
	}

	/**
	 * 주어진 accessToken이 만료되었는지 확인합니다.
	 *
	 * @param token : 확인할 accessToken
	 * @return 만료되었으면 true, 그렇지 않으면 false
	 */
	// HACK: 2024.06.22. spring security 적용 후 삭제될 임시 메서드입니다. - 루카
	public boolean validateAccessTokenExpired(String token) {
		return accessTokenProvider.isTokenExpired(token);
	}

	/**
	 * 주어진 refreshToken이 만료되었는지 확인합니다.
	 *
	 * @param token : 확인할 refreshToken
	 * @return 만료되었으면 true, 그렇지 않으면 false
	 */
	// HACK: 2024.06.22. spring security 적용 후 삭제될 임시 메서드입니다. - 루카
	public boolean validateRefreshTokenExpired(String token) {
		return refreshTokenProvider.isTokenExpired(token);
	}

	private void deleteRefreshToken(Long userId, JwtClaims jwtClaims, String refreshToken) {
		Long refreshTokenUserId = Long.parseLong(
			jwtClaims.getClaims().get(RefreshTokenClaimKeys.USER_ID.getValue()).toString()
		);

		if (!userId.equals(refreshTokenUserId)) {
			throw new AuthException(ErrorStatus.WITHOUT_OWNER_REFRESH_TOKEN);
		}

		try {
			refreshTokenService.delete(refreshTokenUserId, refreshToken);
		} catch (IllegalArgumentException e) {
			log.error("RefreshToken not found: {}", e.getMessage());
		}
	}

	private void deleteAccessToken(Long userId, String accessToken) {
		LocalDateTime expiredDate = accessTokenProvider.getExpiredDate(accessToken);
		forbiddenTokenService.createForbiddenToken(userId, accessToken, expiredDate);
	}

	private long toSeconds(LocalDateTime expiredDate) {
		return Duration.between(LocalDateTime.now(), expiredDate).getSeconds();
	}

}
