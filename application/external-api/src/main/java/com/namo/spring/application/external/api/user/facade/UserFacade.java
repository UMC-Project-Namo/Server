package com.namo.spring.application.external.api.user.facade;

import java.net.HttpURLConnection;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.json.simple.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.application.external.api.group.service.GroupActivityService;
import com.namo.spring.application.external.api.group.service.GroupAndUserService;
import com.namo.spring.application.external.api.group.service.GroupScheduleAndUserService;
import com.namo.spring.application.external.api.individual.converter.CategoryConverter;
import com.namo.spring.application.external.api.individual.service.AlarmService;
import com.namo.spring.application.external.api.individual.service.CategoryService;
import com.namo.spring.application.external.api.individual.service.ImageService;
import com.namo.spring.application.external.api.individual.service.PaletteService;
import com.namo.spring.application.external.api.individual.service.ScheduleService;
import com.namo.spring.application.external.api.user.converter.TermConverter;
import com.namo.spring.application.external.api.user.converter.UserConverter;
import com.namo.spring.application.external.api.user.converter.UserResponseConverter;
import com.namo.spring.application.external.api.user.dto.UserRequest;
import com.namo.spring.application.external.api.user.dto.UserResponse;
import com.namo.spring.application.external.api.user.helper.JwtAuthHelper;
import com.namo.spring.application.external.api.user.service.UserService;
import com.namo.spring.application.external.global.common.security.jwt.CustomJwts;
import com.namo.spring.application.external.global.utils.SocialUtils;
import com.namo.spring.client.social.apple.client.AppleAuthClient;
import com.namo.spring.client.social.apple.dto.AppleResponse;
import com.namo.spring.client.social.apple.dto.AppleResponseConverter;
import com.namo.spring.client.social.common.properties.AppleProperties;
import com.namo.spring.client.social.common.utils.AppleUtils;
import com.namo.spring.client.social.kakao.client.KakaoAuthClient;
import com.namo.spring.client.social.naver.client.NaverAuthClient;
import com.namo.spring.core.infra.common.aws.s3.FileUtils;
import com.namo.spring.core.infra.common.constant.FilePath;
import com.namo.spring.db.mysql.domains.group.domain.MoimScheduleAndUser;
import com.namo.spring.db.mysql.domains.individual.domain.Category;
import com.namo.spring.db.mysql.domains.individual.domain.Image;
import com.namo.spring.db.mysql.domains.individual.domain.Schedule;
import com.namo.spring.db.mysql.domains.individual.type.CategoryKind;
import com.namo.spring.db.mysql.domains.user.domain.Term;
import com.namo.spring.db.mysql.domains.user.domain.User;
import com.namo.spring.db.mysql.domains.user.type.SocialType;
import com.namo.spring.db.mysql.domains.user.type.UserStatus;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserFacade {
	private final JwtAuthHelper jwtAuthHelper;

	private final SocialUtils socialUtils;
	private final FileUtils fileUtils;

	private final UserService userService;
	private final PaletteService paletteService;
	private final CategoryService categoryService;
	private final ScheduleService scheduleService;
	private final AlarmService alarmService;
	private final ImageService imageService;
	private final GroupAndUserService groupAndUserService;
	private final GroupScheduleAndUserService groupScheduleAndUserService;
	private final GroupActivityService groupActivityService;

	private final KakaoAuthClient kakaoAuthClient;
	private final NaverAuthClient naverAuthClient;
	private final AppleAuthClient appleAuthClient;
	private final AppleUtils appleUtils;
	private final AppleProperties appleProperties;

	// TODO: 2024.06.22. 추후에 Social Login을 한번에 처리할 수 있는 Util 클래스로 묶기 - 루카
	@Transactional
	public UserResponse.SignUpDto signupKakao(UserRequest.SocialSignUpDto signUpDto, SocialType socialType) {
		HttpURLConnection con = socialUtils.connectKakaoResourceServer(signUpDto);
		socialUtils.validateSocialAccessToken(con);

		String result = socialUtils.findSocialLoginUsersInfo(con);

		Map<String, String> response = socialUtils.findResponseFromKakako(result);
		return getSignUpDto(signUpDto, socialType, response);
	}

	// TODO: 2024.06.22. 추후에 Social Login을 한번에 처리할 수 있는 Util 클래스로 묶기 - 루카
	@Transactional
	public UserResponse.SignUpDto signupNaver(UserRequest.SocialSignUpDto signUpDto, SocialType socialType) {
		HttpURLConnection con = socialUtils.connectNaverResourceServer(signUpDto);
		socialUtils.validateSocialAccessToken(con);

		String result = socialUtils.findSocialLoginUsersInfo(con);

		Map<String, String> response = socialUtils.findResponseFromNaver(result);
		return getSignUpDto(signUpDto, socialType, response);
	}

	// TODO: 2024.06.22. 추후에 Social Login을 한번에 처리할 수 있는 Util 클래스로 묶기 - 루카
	@Transactional
	public UserResponse.SignUpDto signupApple(UserRequest.AppleSignUpDto req, SocialType socialType) {
		AppleResponse.ApplePublicKeyListDto applePublicKeys = appleAuthClient.getApplePublicKeys();//애플 퍼블릭 키 조회

		//get apple refresh token
		String clientSecret = createClientSecret();
		String appleRefreshToken = appleAuthClient.getAppleRefreshToken(clientSecret, req.getAuthorizationCode());
		String email = "";

		JSONObject headerJson = userService.getHeaderJson(req);
		Object kid = headerJson.get("kid"); //개발자 계정에서 얻은 10자리 식별자 키
		Object alg = headerJson.get("alg"); //토큰을 암호화하는데 사용되는 암호화 알고리즘

		//identityToken 검증
		AppleResponse.ApplePublicKeyDto applePublicKey =
			AppleResponseConverter.toApplePublicKey(applePublicKeys, alg, kid);

		PublicKey publicKey = userService.getPublicKey(applePublicKey);
		userService.validateToken(publicKey, req.getIdentityToken());

		//identity에서 email뽑기
		Claims claims = Jwts.parser()
			.setSigningKey(publicKey)
			.build()
			.parseClaimsJws(req.getIdentityToken())
			.getBody();
		String appleOauthId = claims.get("sub", String.class);
		String appleEmail = claims.get("email", String.class);
		log.debug("email: {}, oauthId : {}", appleEmail, appleOauthId);

		//이메일 셋팅
		if (!req.getEmail().isBlank()) { //첫 로그인
			email = req.getEmail();
		} else { //재로그인
			email = appleEmail;
		}

		//로그인 분기처리
		User savedUser;
		boolean isNewUser;
		Optional<User> userByEmail = userService.getUserByEmailAndSocialType(email, socialType);
		if (userByEmail.isEmpty()) { //첫로그인
			userService.checkEmailAndName(req.getEmail(), req.getUsername());
			savedUser = userService.createUser(UserConverter.toUser(
				req.getEmail(),
				req.getUsername(),
				appleRefreshToken,
				socialType));
			makeBaseCategory(savedUser);
			isNewUser = true;
		} else { //재로그인
			savedUser = userByEmail.get();
			savedUser.setStatus(UserStatus.ACTIVE);
			savedUser.updateSocialRefreshToken(appleRefreshToken);
			isNewUser = false;
		}

		return getSignUpDto(savedUser, isNewUser);
	}

	// HACK: 2024.06.22. getSignUpDto 메서드를 추출하기위한 임시메서드 - 루카
	private UserResponse.SignUpDto getSignUpDto(
		UserRequest.SocialSignUpDto signUpDto,
		SocialType socialType,
		Map<String, String> response
	) {
		User user = UserConverter.toUser(response, signUpDto.getSocialRefreshToken(), socialType);

		Object[] objects = saveOrNot(user, socialType, signUpDto.getSocialRefreshToken());
		User savedUser = (User)objects[0];
		boolean isNewUser = (boolean)objects[1];

		return getSignUpDto(savedUser, isNewUser);
	}

	// HACK: 2024.06.22. getSignUpDto 메서드를 추출하기위한 임시메서드 - 루카
	private UserResponse.SignUpDto getSignUpDto(User savedUser, boolean isNewUser) {
		List<UserResponse.TermsDto> terms = TermConverter.toTerms(userService.getTerms(savedUser));

		CustomJwts jwts = jwtAuthHelper.createToken(savedUser);

		return UserResponseConverter.toSignUpDto(
			jwts.accessToken(),
			jwts.refreshToken(),
			isNewUser,
			terms
		);
	}

	@Transactional
	public UserResponse.ReissueDto reissueAccessToken(String refreshToken) {
		Pair<Long, CustomJwts> user = jwtAuthHelper.refresh(refreshToken);
		return UserResponseConverter.toReissueDto(
			user.getValue().accessToken(),
			user.getValue().refreshToken()
		);
	}

	@Transactional
	public void logout(Long userId, String accessToken, String refreshToken) {
		jwtAuthHelper.removeJwtsToken(userId, accessToken, refreshToken);
	}

	@Transactional(readOnly = false)
	public void createTerm(UserRequest.TermDto termDto, Long userId) {
		User user = userService.getUser(userId);
		List<Term> terms = TermConverter.toTerms(termDto, user);
		userService.createTerm(terms);
	}

	@Transactional
	public void removeKakaoUser(Long userId, String accessToken, String refreshToken) {
		User user = userService.getUser(userId);

		//kakao unlink
		String kakaoAccessToken = kakaoAuthClient.getAccessToken(user.getSocialRefreshToken());
		kakaoAuthClient.unlinkKakao(kakaoAccessToken);

		// Token 삭제
		jwtAuthHelper.removeJwtsToken(userId, accessToken, refreshToken);
	}

	@Transactional
	public void removeNaverUser(Long userId, String accessToken, String refreshToken) {
		User user = userService.getUser(userId);

		//naver unlink
		String naverAccessToken = naverAuthClient.getAccessToken(user.getSocialRefreshToken());
		naverAuthClient.unlinkNaver(naverAccessToken);

		// Token 삭제
		jwtAuthHelper.removeJwtsToken(userId, accessToken, refreshToken);
	}

	@Transactional
	public void removeAppleUser(Long userId, String accessToken, String refreshToken) {
		User user = userService.getUser(userId);

		// apple unlink
		String clientSecret = createClientSecret();
		String appleAccessToken = appleAuthClient.getAppleAccessToken(clientSecret, user.getSocialRefreshToken());
		appleAuthClient.revoke(clientSecret, appleAccessToken);

		// Token 삭제
		jwtAuthHelper.removeJwtsToken(userId, accessToken, refreshToken);
	}

	public String createClientSecret() {
		Date expirationDate = Date.from(
			LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());

		return Jwts.builder()
			.setHeaderParam("kid", appleProperties.getKeyId())
			.setHeaderParam("alg", "ES256")
			.setIssuer(appleProperties.getTeamId())
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(expirationDate)
			.setAudience("https://appleid.apple.com")
			.setSubject(appleProperties.getClientId())
			.signWith(SignatureAlgorithm.ES256, appleUtils.getPrivateKey())
			.compact();
	}

	/**
	 * [유저삭제]
	 * 카테고리 삭제
	 * 스케줄 삭제
	 * - 스케줄 알람 삭제
	 * - 스케줄 이미지 삭제
	 * moimAndUser삭제
	 * moimScheduleAndUser 삭제
	 * - moimScheduleAlarm 삭제
	 * moimMemoLocationAndUser 삭제
	 */
	@Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
	@Transactional
	public void removeUserFromDB() {
		List<User> users = userService.getInactiveUser();
		users.forEach(
			user -> { //db에서 삭제
				log.debug("[Delete] user name : " + user.getName());

				categoryService.removeCategoriesByUser(user);

				List<Schedule> schedules = scheduleService.getAllSchedulesByUser(user);
				alarmService.removeAlarmsBySchedules(schedules);
				List<Image> images = imageService.getImagesBySchedules(schedules);
				List<String> urls = images.stream().map(Image::getImgUrl).collect(Collectors.toList());
				fileUtils.deleteImages(urls, FilePath.INVITATION_ACTIVITY_IMG);
				imageService.removeImagesBySchedules(schedules);
				scheduleService.removeSchedules(schedules);

				groupAndUserService.removeGroupAndUsersByUser(user);

				List<MoimScheduleAndUser> groupScheduleAndUsers = groupScheduleAndUserService.getAllByUser(user);
				groupScheduleAndUserService.removeGroupScheduleAlarms(groupScheduleAndUsers);
				groupScheduleAndUserService.removeGroupScheduleAndUsers(groupScheduleAndUsers);

				groupActivityService.removeGroupActivityAndUsersByUser(user);
				userService.removeUser(user);
			}
		);

	}

	private Object[] saveOrNot(User user, SocialType socialType, String socialRefreshToken) {
		Optional<User> userByEmail = userService.getUserByEmailAndSocialType(user.getEmail(), socialType);
		if (userByEmail.isEmpty()) {
			log.debug("userByEmail is empty");
			User save = userService.createUser(user);
			makeBaseCategory(save);
			boolean isNewUser = true;
			return new Object[] {save, isNewUser};
		}
		User exitingUser = userByEmail.get();
		exitingUser.setStatus(UserStatus.ACTIVE);
		exitingUser.updateSocialRefreshToken(socialRefreshToken);
		boolean isNewUser = false;
		return new Object[] {exitingUser, isNewUser};
	}

	private void makeBaseCategory(User save) {
		Category baseCategory = CategoryConverter.toCategory(
			CategoryKind.SCHEDULE.getCategoryName(),
			paletteService.getReferenceById(1L),
			Boolean.TRUE,
			save,
			CategoryKind.SCHEDULE
		);
		Category groupCategory = CategoryConverter.toCategory(
			CategoryKind.MOIM.getCategoryName(),
			paletteService.getReferenceById(4L),
			Boolean.TRUE,
			save,
			CategoryKind.MOIM
		);

		categoryService.createCategory(baseCategory);
		categoryService.createCategory(groupCategory);
	}
}
