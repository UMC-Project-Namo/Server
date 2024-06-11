package com.namo.spring.application.external.global.feignclient.naver;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
	name = "naver-auth-client",
	url = "https://nid.naver.com/oauth2.0",
	configuration = NaverFeignConfiguration.class
)
public interface NaverAuthApi {
	@PostMapping("/token")
	NaverResponse.UnlinkDto unlinkNaver(
		@RequestParam(name = "grant_type") String grantType,
		@RequestParam(name = "client_id") String clientId,
		@RequestParam(name = "client_secret") String clientSecret,
		@RequestParam(name = "access_token") String accessToken,
		@RequestParam(name = "service_provider") String serviceProvider
	);

	@PostMapping("/token")
	NaverResponse.GetAccessToken getAccessToken(
		@RequestParam(name = "grant_type") String grantType,
		@RequestParam(name = "client_id") String clientId,
		@RequestParam(name = "client_secret") String clientSecret,
		@RequestParam(name = "refresh_token") String refreshToken
	);

}

