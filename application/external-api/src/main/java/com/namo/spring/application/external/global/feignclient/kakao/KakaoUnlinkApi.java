package com.namo.spring.application.external.global.feignclient.kakao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
	name = "kakao-unlink-client",
	url = "https://kapi.kakao.com/v1/user",
	configuration = KakaoFeignConfiguration.class
)
public interface KakaoUnlinkApi {
	@PostMapping(value = "/unlink")
	KakaoResponse.UnlinkDto unlinkKakao(@RequestHeader("Authorization") String authorization);
}
