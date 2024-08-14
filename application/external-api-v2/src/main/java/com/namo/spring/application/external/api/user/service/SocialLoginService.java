package com.namo.spring.application.external.api.user.service;

import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import com.namo.spring.application.external.api.user.dto.MemberRequest;
import com.namo.spring.application.external.global.utils.SocialUtils;
import com.namo.spring.client.social.apple.client.AppleAuthClient;
import com.namo.spring.client.social.apple.dto.AppleResponse;
import com.namo.spring.client.social.kakao.client.KakaoAuthClient;
import com.namo.spring.client.social.naver.client.NaverAuthClient;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.exception.MemberException;
import com.namo.spring.db.mysql.domains.user.type.SocialType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocialLoginService {
	private final KakaoAuthClient kakaoAuthClient;
	private final NaverAuthClient naverAuthClient;
	private final AppleAuthClient appleAuthClient;
	private final SocialUtils socialUtils;

	public Map<String, String> getSocialUserInfo(MemberRequest.SocialSignUpDto signUpDto, SocialType socialType) {
		HttpURLConnection connection = connectToSocialResourceServer(signUpDto, socialType);
		socialUtils.validateSocialAccessToken(connection);
		String result = socialUtils.findSocialLoginUsersInfo(connection);
		return findResponseFromSocial(result, socialType);
	}

	private HttpURLConnection connectToSocialResourceServer(MemberRequest.SocialSignUpDto signUpDto,
		SocialType socialType) {
		return switch (socialType) {
			case KAKAO -> socialUtils.connectKakaoResourceServer(signUpDto);
			case NAVER -> socialUtils.connectNaverResourceServer(signUpDto);
			default -> throw new UnsupportedOperationException("Unsupported social type: " + socialType);
		};
	}

	private Map<String, String> findResponseFromSocial(String result, SocialType socialType) {
		return switch (socialType) {
			case KAKAO -> socialUtils.findResponseFromKakako(result);
			case NAVER -> socialUtils.findResponseFromNaver(result);
			default -> throw new UnsupportedOperationException("Unsupported social type: " + socialType);
		};
	}

	public void unlinkSocialAccount(Member member) {
		switch (member.getSocialType()) {
			case KAKAO -> unlinkKakao(member);
			case NAVER -> unlinkNaver(member);
			case APPLE -> unlinkApple(member);
			default -> throw new IllegalArgumentException("Unsupported social type: " + member.getSocialType());
		}
	}

	private void unlinkKakao(Member member) {
		String kakaoAccessToken = kakaoAuthClient.getAccessToken(member.getSocialRefreshToken());
		kakaoAuthClient.unlinkKakao(kakaoAccessToken);
	}

	private void unlinkNaver(Member member) {
		String naverAccessToken = naverAuthClient.getAccessToken(member.getSocialRefreshToken());
		naverAuthClient.unlinkNaver(naverAccessToken);
	}

	private void unlinkApple(Member member) {
		String clientSecret = appleAuthClient.createClientSecret();
		String appleAccessToken = appleAuthClient.getAppleAccessToken(clientSecret, member.getSocialRefreshToken());
		appleAuthClient.revoke(clientSecret, appleAccessToken);
	}

	public String getAppleRefreshToken(MemberRequest.AppleSignUpDto req) {
		AppleResponse.ApplePublicKeyListDto applePublicKeys = appleAuthClient.getApplePublicKeys();
		String clientSecret = appleAuthClient.createClientSecret();
		String appleRefreshToken = appleAuthClient.getAppleRefreshToken(clientSecret, req.getAuthorizationCode());

		JSONObject headerJson = getHeaderJson(req);
		AppleResponse.ApplePublicKeyDto applePublicKey = appleAuthClient.getApplePublicKey(applePublicKeys, headerJson);

		PublicKey publicKey = createRSAPublicKey(applePublicKey);
		appleAuthClient.validateToken(publicKey, req.getIdentityToken());

		return appleRefreshToken;
	}

	public String determineEmail(MemberRequest.AppleSignUpDto req) {
		return req.getEmail().isBlank() ? getAppleEmail(req) : req.getEmail();
	}

	private String getAppleEmail(MemberRequest.AppleSignUpDto req) {
		PublicKey publicKey = createRSAPublicKey(
			appleAuthClient.getApplePublicKey(appleAuthClient.getApplePublicKeys(), getHeaderJson(req))
		);
		Claims claims = Jwts.parser()
			.setSigningKey(publicKey)
			.build()
			.parseClaimsJws(req.getIdentityToken())
			.getBody();
		String appleOauthId = claims.get("sub", String.class);
		String appleEmail = claims.get("email", String.class);
		log.debug("email: {}, oauthId : {}", appleEmail, appleOauthId);
		return appleEmail;
	}

	private PublicKey createRSAPublicKey(AppleResponse.ApplePublicKeyDto applePublicKey) {
		try {
			BigInteger modulus = KeyDecoder.decodeToBigInteger(applePublicKey.getModulus());
			BigInteger exponent = KeyDecoder.decodeToBigInteger(applePublicKey.getExponent());
			RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, exponent);
			KeyFactory keyFactory = KeyFactory.getInstance(applePublicKey.getKty());
			return keyFactory.generatePublic(publicKeySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
			throw new MemberException(ErrorStatus.MAKE_PUBLIC_KEY_FAILURE);
		}
	}

	private JSONObject getHeaderJson(MemberRequest.AppleSignUpDto req) {
		try {
			JSONParser parser = new JSONParser();
			String[] decodeArr = req.getIdentityToken().split("\\.");
			String header = new String(Base64.getDecoder().decode(decodeArr[0]));
			return (JSONObject)parser.parse(header);
		} catch (ParseException e) {
			throw new RuntimeException("Failed to parse Apple identity token header", e);
		}
	}
}

