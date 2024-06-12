package com.namo.spring.client.social.kakao.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.namo.spring.client.social.kakao.config.KakaoFeignConfiguration;
import com.namo.spring.client.social.kakao.dto.KakaoResponse;

@FeignClient(
	name = "kakao-auth-client",
	url = "https://kauth.kakao.com/oauth",
	configuration = KakaoFeignConfiguration.class
)
public interface KakaoAuthApi {
	@PostMapping(value = "/token")
	KakaoResponse.GetAccessToken getAccessToken(
		@RequestParam("grant_type") String grantType,
		@RequestParam("client_id") String clientId,
		@RequestParam("refresh_token") String refreshToken
	);
}
