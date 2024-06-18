package com.namo.spring.db.redis.cache.refresh;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.namo.spring.db.redis.config.RedisConfig;
import com.namo.spring.db.redis.config.RedisTestContainerConfig;

@DataRedisTest(properties = "spring.config.location=classpath:/redis.yml")
@ContextConfiguration(classes = {RedisConfig.class, RefreshTokenService.class})
@ActiveProfiles("local")
class RefreshTokenServiceTest extends RedisTestContainerConfig {

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	private RefreshTokenService refreshTokenService;

	@BeforeEach
	void setUp() {
		refreshTokenService = new RefreshTokenService(refreshTokenRepository);
	}

	@Test
	@DisplayName("refresh token 저장 테스트")
	void saveSuccessTest() {
	    // given
		RefreshToken refreshToken = RefreshToken.builder()
			.userId(1L)
			.token("refreshToken")
			.ttl(1000L)
			.build();

	    // when
		refreshTokenService.save(refreshToken);

	    // then
		RefreshToken savedRefreshToken = refreshTokenRepository.findById(1L).orElse(null);
		assertThat(savedRefreshToken).isEqualTo(refreshToken);
	}


}
