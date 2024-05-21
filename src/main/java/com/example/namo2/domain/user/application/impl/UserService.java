package com.example.namo2.domain.user.application.impl;

import static com.example.namo2.global.common.response.BaseResponseStatus.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import com.example.namo2.domain.user.dao.repository.TermRepository;
import com.example.namo2.domain.user.dao.repository.UserRepository;
import com.example.namo2.domain.user.domain.Term;
import com.example.namo2.domain.user.domain.User;
import com.example.namo2.domain.user.domain.constant.UserStatus;
import com.example.namo2.domain.user.ui.dto.UserRequest;

import com.example.namo2.global.common.exception.BaseException;
import com.example.namo2.global.common.response.BaseResponseStatus;
import com.example.namo2.global.feignclient.apple.AppleProperties;
import com.example.namo2.global.feignclient.apple.AppleResponse;
import com.example.namo2.global.utils.JwtUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

	private final UserRepository userRepository;
	private final TermRepository termRepository;

	private final AppleProperties appleProperties;
	private final JwtUtils jwtUtils;
	private final RedisTemplate<String, String> redisTemplate;

	public List<Term> getTerms(User user) {
		return termRepository.findTermsByUser(user);
	}

	public User createUser(User user) {
		return userRepository.save(user);
	}

	public User getUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
	}

	public List<User> getUsersInMoimSchedule(List<Long> users) {
		return userRepository.findUsersById(users);
	}

	public Optional<User> getUserByEmail(String email) {
		return userRepository.findUserByEmail(email);
	}

	public User getUserByRefreshToken(String refreshToken) {
		return userRepository.findUserByRefreshToken(refreshToken)
			.orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
	}

	public List<User> getInactiveUser() {
		return userRepository.findUsersByStatusAndDate(UserStatus.INACTIVE, LocalDateTime.now().minusDays(3));
	}

	public void updateRefreshToken(Long userId, String refreshToken) {
		User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(NOT_FOUND_USER_FAILURE));
		user.updateRefreshToken(refreshToken);
	}

	public void createTerm(List<Term> terms) {
		for (Term term : terms) {
			if (!term.getIsCheck()) {
				throw new BaseException(NOT_CHECK_TERM_ERROR);
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
			throw new BaseException(USER_POST_ERROR);
		}
	}

	public void checkAccessTokenValidation(String accessToken) {
		if (!jwtUtils.validateToken(accessToken)) {
			throw new BaseException(EXPIRATION_ACCESS_TOKEN);
		}
	}

	public void checkRefreshTokenValidation(String refreshToken) {
		if (!jwtUtils.validateToken(refreshToken)) {
			throw new BaseException(EXPIRATION_REFRESH_TOKEN);
		}
	}

	public void checkLogoutUser(UserRequest.SignUpDto signUpDto) {
		String blackToken = redisTemplate.opsForValue().get(signUpDto.getAccessToken());
		if (StringUtils.hasText(blackToken)) {
			throw new BaseException(BaseResponseStatus.LOGOUT_ERROR);
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
			throw new BaseException(MAKE_PUBLIC_KEY_FAILURE);
		}

	}

	public boolean validateToken(PublicKey publicKey, String token) {
		Claims claims = Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token).getBody();

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

	public PrivateKey getPrivateKey() {
		try {
			ClassPathResource resource = new ClassPathResource(appleProperties.getPrivateKeyPath());
			String privateKey = new String(Files.readAllBytes(Paths.get(resource.getURI())));
			Reader pemReader = new StringReader(privateKey);

			PEMParser pemParser = new PEMParser(pemReader);
			JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
			PrivateKeyInfo object = (PrivateKeyInfo)pemParser.readObject();
			return converter.getPrivateKey(object);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
