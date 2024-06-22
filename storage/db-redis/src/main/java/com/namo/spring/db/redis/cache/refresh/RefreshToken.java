package com.namo.spring.db.redis.cache.refresh;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@RedisHash("refreshToken")
@ToString(of = {"userId", "token", "ttl"})
@EqualsAndHashCode(of = {"userId", "token"})
public class RefreshToken {

	@Id
	private final Long userId;
	private String token;

	private final long ttl;

	@Builder
	private RefreshToken(Long userId, String token, long ttl) {
		this.userId = userId;
		this.token = token;
		this.ttl = ttl;
	}

	public static RefreshToken of(Long userId, String token, long ttl) {
		return RefreshToken.builder()
			.userId(userId)
			.token(token)
			.ttl(ttl)
			.build();
	}

	protected void rotation(String token) {
		this.token = token;
	}
}
