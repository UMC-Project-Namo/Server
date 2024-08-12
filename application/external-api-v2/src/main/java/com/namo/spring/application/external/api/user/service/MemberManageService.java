package com.namo.spring.application.external.api.user.service;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import com.namo.spring.application.external.api.Category.service.CategoryMaker;
import com.namo.spring.application.external.api.user.converter.MemberConverter;
import com.namo.spring.application.external.api.user.dto.MemberDto;
import com.namo.spring.application.external.api.user.dto.MemberRequest;
import com.namo.spring.client.social.apple.dto.AppleResponse;
import com.namo.spring.client.social.common.properties.AppleProperties;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.entity.Term;
import com.namo.spring.db.mysql.domains.user.exception.MemberException;
import com.namo.spring.db.mysql.domains.user.repository.MemberRepository;
import com.namo.spring.db.mysql.domains.user.repository.TermRepository;
import com.namo.spring.db.mysql.domains.user.service.MemberService;
import com.namo.spring.db.mysql.domains.user.type.MemberStatus;
import com.namo.spring.db.mysql.domains.user.type.SocialType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberManageService {

	private final MemberRepository memberRepository;
	private final TermRepository termRepository;
	private final AppleProperties appleProperties;
	private final MemberService memberService;
	private final CategoryMaker categoryMaker;

	public List<Term> getTerms(Member member) {
		return termRepository.findTermsByMember(member);
	}

	public Member createMember(Member member) {
		return memberRepository.save(member);
	}

	public Member getMember(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(ErrorStatus.NOT_FOUND_USER_FAILURE));
	}

	public Optional<Member> getMemberByEmailAndSocialType(String email, SocialType socialType) {
		return memberRepository.findMemberByEmailAndSocialType(email, socialType);
	}

	public List<Member> getInactiveMember() {
		return memberRepository.findMembersByStatusAndDate(MemberStatus.INACTIVE, LocalDateTime.now().minusDays(3));
	}

	public void removeMember(Member member) {
		memberRepository.delete(member);
	}

	public void checkEmailAndName(String email, String name) {
		if (email.isBlank() || name.isBlank()) {
			throw new MemberException(ErrorStatus.USER_POST_ERROR);
		}
	}

	public JSONObject getHeaderJson(MemberRequest.AppleSignUpDto req) {
		try {
			JSONParser parser = new JSONParser();
			String[] decodeArr = req.getIdentityToken().split("\\.");
			String header = new String(Base64.getDecoder().decode(decodeArr[0]));
			JSONObject headerJson = (JSONObject)parser.parse(header);
			return headerJson;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public PublicKey getPublicKey(AppleResponse.ApplePublicKeyDto applePublicKey) {
		String nStr = applePublicKey.getModulus(); //RSA public key의 모듈러스 값
		String eStr = applePublicKey.getExponent(); //RSA public key의 지수 값

		byte[] nBytes = Base64.getUrlDecoder().decode(nStr);
		byte[] eBytes = Base64.getUrlDecoder().decode(eStr);

		BigInteger n = new BigInteger(1, nBytes);
		BigInteger e = new BigInteger(1, eBytes);

		try {
			RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
			KeyFactory keyFactory = KeyFactory.getInstance(applePublicKey.getKty());
			return keyFactory.generatePublic(publicKeySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
			throw new MemberException(ErrorStatus.MAKE_PUBLIC_KEY_FAILURE);
		}

	}

	/**
	 * Apple로부터 받은 토큰을 검증합니다.
	 *
	 * @param publicKey : Apple로부터 받은 공개키
	 * @param token     : Apple로부터 받은 토큰
	 * @return 토큰이 유효하면 true, 그렇지 않으면 false
	 */
	// TODO: 2024.06.22. 추후 social-client 모듈료 이동해야합니다. - 루카
	public boolean validateToken(PublicKey publicKey, String token) {
		Claims claims = Jwts.parser().setSigningKey(publicKey).build().parseClaimsJws(token).getBody();

		Object issuerObj = claims.get("iss");
		if (issuerObj instanceof String) {
			String issuer = (String)issuerObj;
			if (!"https://appleid.apple.com".equals(issuer)) {
				throw new IllegalArgumentException("Invalid issuer");
			}
		} else {
			throw new IllegalArgumentException("Issuer is not a string");
		}

		Object audienceObj = claims.get("aud");
		log.debug("apple aud {}", audienceObj);
		if (audienceObj instanceof String) {
			//String 인 경우
			String audience = (String)audienceObj;
			log.debug("{}", audience);
			if (!appleProperties.getClientId().equals(audience)) {
				throw new IllegalArgumentException("Invalid audience");
			}
		} else if (audienceObj instanceof LinkedHashSet<?>) {
			// LinkedHashSet인 경우 처리
			LinkedHashSet<String> audienceList = (LinkedHashSet<String>)audienceObj;
			log.debug("linked {}, {}}", audienceList, audienceList.size());
			for (String aud : audienceList) {
				if (!appleProperties.getClientId().equals(aud)) {
					throw new IllegalArgumentException("Invalid audience");
				}
			}
		} else {
			throw new IllegalArgumentException("Audience is not a string");
		}

		long expiration = claims.getExpiration().getTime();
		log.debug("expriation : {} < now : {}", expiration, (new Date()).getTime());
		if (expiration <= (new Date()).getTime()) {
			throw new IllegalArgumentException("Token expired");
		}
		return true;
	}

	public boolean isSignUpComplete(Member savedMember) {
		return savedMember.getNickname() != null && savedMember.getBirthday() != null && savedMember.getTag() != null;
	}

	public MemberDto.MemberCreationRecord updateExistingMember(Member existingMember, String socialRefreshToken) {
		existingMember.changeToActive();
		existingMember.updateSocialRefreshToken(socialRefreshToken);
		return new MemberDto.MemberCreationRecord(existingMember, false);
	}

	public MemberDto.MemberCreationRecord createNewMember(Member member) {
		log.debug("Creating new member");
		Member savedMember = memberService.createMember(member);
		makeBaseCategory(savedMember);
		return new MemberDto.MemberCreationRecord(savedMember, true);
	}

	public Member createNewAppleMember(MemberRequest.AppleSignUpDto req, String email, String appleRefreshToken,
		SocialType socialType) {
		checkEmailAndName(email, req.getUsername());
		Member newMember = memberService.createMember(MemberConverter.toMember(
			email,
			req.getUsername(),
			appleRefreshToken,
			socialType));
		makeBaseCategory(newMember);
		return newMember;
	}

	public Member updateExistingAppleMember(Member existingMember, String appleRefreshToken) {
		existingMember.changeToActive();
		existingMember.updateSocialRefreshToken(appleRefreshToken);
		return existingMember;
	}

	private void makeBaseCategory(Member member) {
		categoryMaker.makeIndividualCategory(member);
		categoryMaker.makeGroupCategory(member);
	}
}
