package com.namo.spring.db.redis.cache.refresh;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class RefreshTokenService {
	private final RefreshTokenRepository refreshTokenRepository;

	public void save(RefreshToken refreshToken) {
		refreshTokenRepository.save(refreshToken);
	}

	public RefreshToken refresh(
		Long userId,
		String oldRefreshToken,
		String newRefreshToken
	) throws IllegalArgumentException, IllegalStateException {
		RefreshToken refreshToken = findOrElseThrow(userId);

		validateToken(oldRefreshToken, refreshToken);

		refreshToken.rotation(newRefreshToken);
		refreshTokenRepository.save(refreshToken);

		return refreshToken;
	}

	// TODO: Custom exception handling 적용하기
	private RefreshToken findOrElseThrow(Long userId) {
		return refreshTokenRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("RefreshToken not found"));
	}

	// TODO: Custom exception handling 적용하기
	private void validateToken(String oldRefreshToken, RefreshToken refreshToken) {
		if (isTakenAway(oldRefreshToken, refreshToken.getToken())) {
			refreshTokenRepository.delete(refreshToken);
			throw new IllegalStateException("refresh token mismatched");
		}
	}

	private boolean isTakenAway(String requestRefreshToken, String expectedRefreshToken) {
		return !requestRefreshToken.equals(expectedRefreshToken);
	}

}
