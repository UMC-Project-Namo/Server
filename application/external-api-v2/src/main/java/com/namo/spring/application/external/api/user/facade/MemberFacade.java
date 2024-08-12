package com.namo.spring.application.external.api.user.facade;

import java.net.HttpURLConnection;
import java.security.PublicKey;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.application.external.api.user.converter.MemberConverter;
import com.namo.spring.application.external.api.user.converter.MemberResponseConverter;
import com.namo.spring.application.external.api.user.converter.TermConverter;
import com.namo.spring.application.external.api.user.dto.MemberDto;
import com.namo.spring.application.external.api.user.dto.MemberRequest;
import com.namo.spring.application.external.api.user.dto.MemberResponse;
import com.namo.spring.application.external.api.user.helper.JwtAuthHelper;
import com.namo.spring.application.external.api.user.service.MemberManageService;
import com.namo.spring.application.external.api.user.service.SocialLoginService;
import com.namo.spring.application.external.global.common.security.jwt.CustomJwts;
import com.namo.spring.application.external.global.utils.SocialUtils;
import com.namo.spring.client.social.apple.client.AppleAuthClient;
import com.namo.spring.client.social.apple.dto.AppleResponse;
import com.namo.spring.client.social.apple.dto.AppleResponseConverter;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.type.SocialType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberFacade {
	private final JwtAuthHelper jwtAuthHelper;
	private final SocialUtils socialUtils;
	private final MemberManageService memberManageService;
	private final AppleAuthClient appleAuthClient;
	private final SocialLoginService socialLoginService;

	@Transactional
	public MemberResponse.SignUpDto socialSignup(MemberRequest.SocialSignUpDto signUpDto, SocialType socialType) {
		HttpURLConnection con = socialLoginService.connectToSocialResourceServer(signUpDto, socialType);
		socialUtils.validateSocialAccessToken(con);

		String result = socialUtils.findSocialLoginUsersInfo(con);
		Map<String, String> response = socialLoginService.findResponseFromSocial(result, socialType);
		return processSocialSignup(signUpDto, socialType, response);
	}

	private MemberResponse.SignUpDto processSocialSignup(
		MemberRequest.SocialSignUpDto signUpDto,
		SocialType socialType,
		Map<String, String> response
	) {
		Member member = MemberConverter.toMember(response, signUpDto.getSocialRefreshToken(), socialType);
		MemberDto.MemberCreationRecord memberInfo = processOrRetrieveMember(member, socialType,
			signUpDto.getSocialRefreshToken());
		return createSignUpResponse(memberInfo.member(), memberInfo.isNewUser());
	}

	private MemberDto.MemberCreationRecord processOrRetrieveMember(Member member, SocialType socialType,
		String socialRefreshToken) {
		return memberManageService.getMemberByEmailAndSocialType(member.getEmail(), socialType)
			.map(existingMember -> memberManageService.updateExistingMember(existingMember, socialRefreshToken))
			.orElseGet(() -> memberManageService.createNewMember(member));
	}

	@Transactional
	public MemberResponse.SignUpDto signupApple(MemberRequest.AppleSignUpDto req, SocialType socialType) {
		String appleRefreshToken = processAppleAuthentication(req);
		String email = determineEmail(req);
		MemberDto.MemberCreationRecord savedUser = processAppleLogin(req, socialType, email, appleRefreshToken);

		return createSignUpResponse(savedUser.member(), savedUser.isNewUser());
	}

	private String processAppleAuthentication(MemberRequest.AppleSignUpDto req) {
		AppleResponse.ApplePublicKeyListDto applePublicKeys = appleAuthClient.getApplePublicKeys();
		String clientSecret = appleAuthClient.createClientSecret();
		String appleRefreshToken = appleAuthClient.getAppleRefreshToken(clientSecret, req.getAuthorizationCode());

		JSONObject headerJson = memberManageService.getHeaderJson(req);
		AppleResponse.ApplePublicKeyDto applePublicKey = getApplePublicKey(applePublicKeys, headerJson);

		PublicKey publicKey = memberManageService.getPublicKey(applePublicKey);
		memberManageService.validateToken(publicKey, req.getIdentityToken());

		return appleRefreshToken;
	}

	private AppleResponse.ApplePublicKeyDto getApplePublicKey(AppleResponse.ApplePublicKeyListDto applePublicKeys,
		JSONObject headerJson) {
		Object kid = headerJson.get("kid");
		Object alg = headerJson.get("alg");
		return AppleResponseConverter.toApplePublicKey(applePublicKeys, alg, kid);
	}

	private String determineEmail(MemberRequest.AppleSignUpDto req) {
		return req.getEmail().isBlank() ? getAppleEmail(req) : req.getEmail();
	}

	private String getAppleEmail(MemberRequest.AppleSignUpDto req) {
		Claims claims = Jwts.parser()
			.setSigningKey(memberManageService.getPublicKey(
				getApplePublicKey(appleAuthClient.getApplePublicKeys(), memberManageService.getHeaderJson(req))))
			.build()
			.parseClaimsJws(req.getIdentityToken())
			.getBody();
		return claims.get("email", String.class);
	}

	private MemberDto.MemberCreationRecord processAppleLogin(MemberRequest.AppleSignUpDto req, SocialType socialType,
		String email, String appleRefreshToken) {
		Optional<Member> userByEmail = memberManageService.getMemberByEmailAndSocialType(email, socialType);
		return userByEmail.map(member -> new MemberDto.MemberCreationRecord(
				memberManageService.updateExistingAppleMember(member, appleRefreshToken),
				false))
			.orElseGet(() -> new MemberDto.MemberCreationRecord(
				memberManageService.createNewAppleMember(req, email, appleRefreshToken, socialType),
				true));
	}

	private MemberResponse.SignUpDto createSignUpResponse(Member savedMember, boolean isNewUser) {
		List<MemberResponse.TermsDto> terms = TermConverter.toTerms(memberManageService.getTerms(savedMember));
		CustomJwts jwts = jwtAuthHelper.createToken(savedMember);
		boolean isSignUpComplete = memberManageService.isSignUpComplete(savedMember);

		return MemberResponseConverter.toSignUpDto(
			jwts.accessToken(),
			jwts.refreshToken(),
			isNewUser,
			isSignUpComplete,
			terms
		);
	}

	@Transactional
	public MemberResponse.ReissueDto reissueAccessToken(String refreshToken) {
		Pair<Long, CustomJwts> member = jwtAuthHelper.refresh(refreshToken);
		return MemberResponseConverter.toReissueDto(
			member.getValue().accessToken(),
			member.getValue().refreshToken()
		);
	}

	@Transactional
	public void logout(Long memberId, String accessToken, String refreshToken) {
		jwtAuthHelper.removeJwtsToken(memberId, accessToken, refreshToken);
	}

	@Transactional
	public void removeSocialMember(Long memberId, String accessToken, String refreshToken) {
		Member member = memberManageService.getMember(memberId);
		socialLoginService.unlinkSocialAccount(member);
		jwtAuthHelper.removeJwtsToken(memberId, accessToken, refreshToken);
		member.changeToInactive();
	}

}
