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
import java.util.List;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import com.namo.spring.application.external.api.user.dto.UserRequest;
import com.namo.spring.client.social.apple.dto.AppleResponse;
import com.namo.spring.client.social.common.properties.AppleProperties;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.exception.UserException;
import com.namo.spring.db.mysql.domains.user.domain.Term;
import com.namo.spring.db.mysql.domains.user.domain.User;
import com.namo.spring.db.mysql.domains.user.repository.TermRepository;
import com.namo.spring.db.mysql.domains.user.repository.UserRepository;
import com.namo.spring.db.mysql.domains.user.type.SocialType;
import com.namo.spring.db.mysql.domains.user.type.UserStatus;
import com.namo.spring.db.redis.cache.forbidden.ForbiddenTokenService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final TermRepository termRepository;

	private final AppleProperties appleProperties;

	public List<Term> getTerms(User user) {
		return termRepository.findTermsByUser(user);
	}

	public User createUser(User user) {
		return userRepository.save(user);
	}

	public Optional<User> readUser(Long userId) {
		return userRepository.findById(userId);
	}

	public User getUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new UserException(ErrorStatus.NOT_FOUND_USER_FAILURE));
	}

	public List<User> getUsersInGroupSchedule(List<Long> users) {
		return userRepository.findUsersById(users);
	}

	public Optional<User> getUserByEmailAndSocialType(String email, SocialType socialType) {
		return userRepository.findUserByEmailAndSocialType(email, socialType);
	}

	public User getUserByRefreshToken(String refreshToken) {
		return userRepository.findUserByRefreshToken(refreshToken)
			.orElseThrow(() -> new UserException(ErrorStatus.NOT_FOUND_USER_FAILURE));
	}

	public List<User> getInactiveUser() {
		return userRepository.findUsersByStatusAndDate(UserStatus.INACTIVE, LocalDateTime.now().minusDays(3));
	}

	public void createTerm(List<Term> terms) {
		for (Term term : terms) {
			if (!term.getIsCheck()) {
				throw new UserException(ErrorStatus.NOT_CHECK_TERM_ERROR);
			}
			termRepository.findTermByContentAndUser(term.getContent(), term.getUser())
				.ifPresentOrElse(
					savedTerm -> savedTerm.update(),
					() -> termRepository.save(term)
				);
		}
	}

	public void removeUser(User user) {
		userRepository.delete(user);
	}

	public void checkEmailAndName(String email, String name) {
		if (email.isBlank() || name.isBlank()) {
			throw new UserException(ErrorStatus.USER_POST_ERROR);
		}
	}

	public JSONObject getHeaderJson(UserRequest.AppleSignUpDto req) {
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
			throw new UserException(ErrorStatus.MAKE_PUBLIC_KEY_FAILURE);
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

		String issuer = (String)claims.get("iss");
		if (!"https://appleid.apple.com".equals(issuer)) {
			throw new IllegalArgumentException("Invalid issuer");
		}

		String audience = (String)claims.get("aud");
		log.debug("{}", audience);
		if (!appleProperties.getClientId().equals(audience)) {
			throw new IllegalArgumentException("Invalid audience");
		}

		long expiration = claims.getExpiration().getTime();
		log.debug("expriation : {} < now : {}", expiration, (new Date()).getTime());
		if (expiration <= (new Date()).getTime()) {
			throw new IllegalArgumentException("Token expired");
		}
		return true;
	}
}
