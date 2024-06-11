package com.namo.spring.application.external.global.feignclient.kakao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoAuthClient {
	private final KakaoUnlinkApi kakaoUnlinkApi;
	private final KakaoAuthApi kakaoAuthApi;
	@Value("${spring.security.oauth1.client.registration.kakao.client-id}")
	private String clientId;

	public String getAccessToken(String refreshToken) {
		KakaoResponse.GetAccessToken getAccessToken = kakaoAuthApi.getAccessToken(
			"refresh_token",
			clientId,
			refreshToken
		);
		return getAccessToken.getAccessToken();
	}

	public KakaoResponse.UnlinkDto unlinkKakao(String accessToken) {
		log.info("Bearer " + accessToken + "로 카카오 계정 연동 해제 요청");
		return kakaoUnlinkApi.unlinkKakao("Bearer " + accessToken);
	}
}
