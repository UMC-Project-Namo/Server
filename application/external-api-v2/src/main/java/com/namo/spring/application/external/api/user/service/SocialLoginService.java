package com.namo.spring.application.external.api.user.service;

import java.net.HttpURLConnection;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.namo.spring.application.external.api.user.dto.MemberRequest;
import com.namo.spring.application.external.global.utils.SocialUtils;
import com.namo.spring.client.social.apple.client.AppleAuthClient;
import com.namo.spring.client.social.kakao.client.KakaoAuthClient;
import com.namo.spring.client.social.naver.client.NaverAuthClient;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.type.SocialType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SocialLoginService {
	private final KakaoAuthClient kakaoAuthClient;
	private final NaverAuthClient naverAuthClient;
	private final AppleAuthClient appleAuthClient;

	private final SocialUtils socialUtils;

	public HttpURLConnection connectToSocialResourceServer(MemberRequest.SocialSignUpDto signUpDto,
		SocialType socialType) {
		return switch (socialType) {
			case KAKAO -> socialUtils.connectKakaoResourceServer(signUpDto);
			case NAVER -> socialUtils.connectNaverResourceServer(signUpDto);
			default -> throw new UnsupportedOperationException("Unsupported social type: " + socialType);
		};
	}

	public Map<String, String> findResponseFromSocial(String result, SocialType socialType) {
		return switch (socialType) {
			case KAKAO -> socialUtils.findResponseFromKakako(result);
			case NAVER -> socialUtils.findResponseFromNaver(result);
			default -> throw new UnsupportedOperationException("Unsupported social type: " + socialType);
		};
	}

	public void unlinkSocialAccount(Member member) {
		switch (member.getSocialType()) {
			case KAKAO:
				String kakaoAccessToken = kakaoAuthClient.getAccessToken(member.getSocialRefreshToken());
				kakaoAuthClient.unlinkKakao(kakaoAccessToken);
				break;
			case NAVER:
				String naverAccessToken = naverAuthClient.getAccessToken(member.getSocialRefreshToken());
				naverAuthClient.unlinkNaver(naverAccessToken);
				break;
			case APPLE:
				String clientSecret = appleAuthClient.createClientSecret();
				String appleAccessToken = appleAuthClient.getAppleAccessToken(clientSecret,
					member.getSocialRefreshToken());
				appleAuthClient.revoke(clientSecret, appleAccessToken);
				break;
			default:
				throw new IllegalArgumentException("Unsupported social type: " + member.getSocialType());
		}
	}
}

