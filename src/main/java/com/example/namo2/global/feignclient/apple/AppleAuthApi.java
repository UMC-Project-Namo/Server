package com.example.namo2.global.feignclient.apple;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
	name = "apple-public-key-client",
	url = "https://appleid.apple.com/auth",
	configuration = AppleFeignConfiguration.class
)
public interface AppleAuthApi {
	@GetMapping(value = "/keys", consumes = "application/x-www-form-urlencoded")
	AppleResponse.ApplePublicKeyListDto getApplePublicKeys();

	@PostMapping(value = "/token", consumes = "application/x-www-form-urlencoded")
	AppleResponse.GetToken getAppleToken(
		@RequestParam(name = "client_id") String clientId,
		@RequestParam(name = "client_secret") String clientSecret,
		@RequestParam(name = "refresh_token") String refreshToken,
		@RequestParam(name = "grant_type") String grantType
	);

	@PostMapping(value = "/revoke", consumes = "application/x-www-form-urlencoded")
	void revoke(
		@RequestParam(name = "client_id") String clientId,
		@RequestParam(name = "client_secret") String clientSecret,
		@RequestParam(name = "token") String token,
		@RequestParam(name = "token_type_hint") String tokenType
	);

}
