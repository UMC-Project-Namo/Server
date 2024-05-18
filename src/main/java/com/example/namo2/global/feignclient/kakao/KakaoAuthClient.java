package com.example.namo2.global.feignclient.kakao;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoAuthClient {
	private final KakaoAuthApi kakaoAuthApi;

	public KakaoResponse.UnlinkDto unlinkKakao(String accessToken) {
		log.info("Bearer " + accessToken + "로 카카오 계정 연동 해제 요청");
		return kakaoAuthApi.unlinkKakao("Bearer " + accessToken);
	}
}
