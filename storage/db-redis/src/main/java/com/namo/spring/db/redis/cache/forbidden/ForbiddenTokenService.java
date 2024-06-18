package com.namo.spring.db.redis.cache.forbidden;

import java.time.Duration;
import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ForbiddenTokenService {
	private final ForbiddenTokenRepository forbiddenTokenRepository;

	public void createForbiddenToken(
		String accessToken,
		Long userId,
		LocalDateTime expiresAt
	) {
		final LocalDateTime now = LocalDateTime.now();
		final long timeToLive = Duration.between(now, expiresAt).toSeconds();

		ForbiddenToken forbiddenToken = ForbiddenToken.of(accessToken, userId, timeToLive);
		forbiddenTokenRepository.save(forbiddenToken);
	}

	public boolean isForbidden(String accessToken) {
		return forbiddenTokenRepository.existsById(accessToken);
	}

}
