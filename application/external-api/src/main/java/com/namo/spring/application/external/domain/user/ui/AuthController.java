package com.namo.spring.application.external.domain.user.ui;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.namo.spring.application.external.domain.user.application.UserFacade;
import com.namo.spring.db.mysql.domains.user.type.SocialType;
import com.namo.spring.application.external.domain.user.ui.dto.UserRequest;
import com.namo.spring.application.external.domain.user.ui.dto.UserResponse;
import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCodes;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.response.ResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "1. Auth", description = "로그인, 회원가입 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auths")
public class AuthController {
	private final UserFacade userFacade;

	@Operation(summary = "카카오 회원가입", description = "카카오 소셜 로그인을 통한 회원가입")
	@PostMapping(value = "/kakao/signup")
	@ApiErrorCodes(value = {
		ErrorStatus.USER_POST_ERROR,
		ErrorStatus.KAKAO_UNAUTHORIZED,
		ErrorStatus.KAKAO_FORBIDDEN,
		ErrorStatus.KAKAO_BAD_GATEWAY,
		ErrorStatus.KAKAO_SERVICE_UNAVAILABLE,
		ErrorStatus.KAKAO_INTERNAL_SERVER_ERROR,
		ErrorStatus.SOCIAL_LOGIN_FAILURE,
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<UserResponse.SignUpDto> kakaoSignup(
		@Valid @RequestBody UserRequest.SocialSignUpDto signUpDto
	) {
		UserResponse.SignUpDto signupDto = userFacade.signupKakao(signUpDto, SocialType.KAKAO);
		return ResponseDto.onSuccess(signupDto);
	}

	@Operation(summary = "네이버 회원가입", description = "네이버 소셜 로그인을 통한 회원가입")
	@PostMapping(value = "/naver/signup")
	@ApiErrorCodes(value = {
		ErrorStatus.USER_POST_ERROR,
		ErrorStatus.NAVER_UNAUTHORIZED,
		ErrorStatus.NAVER_FORBIDDEN,
		ErrorStatus.SOCIAL_LOGIN_FAILURE,
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<UserResponse.SignUpDto> naverSignup(
		@Valid @RequestBody UserRequest.SocialSignUpDto signUpDto
	) {
		UserResponse.SignUpDto signupDto = userFacade.signupNaver(signUpDto, SocialType.NAVER);
		return ResponseDto.onSuccess(signupDto);
	}

	@Operation(summary = "애플 회원가입", description = "애플 소셜 로그인을 통한 회원가입.")
	@PostMapping(value = "/apple/signup")
	@ApiErrorCodes(value = {
		ErrorStatus.USER_POST_ERROR,
		ErrorStatus.MAKE_PUBLIC_KEY_FAILURE,
		ErrorStatus.APPLE_REQUEST_ERROR,
		ErrorStatus.APPLE_UNAUTHORIZED,
		ErrorStatus.SOCIAL_LOGIN_FAILURE,
		ErrorStatus.FEIGN_SERVER_ERROR
	})
	public ResponseDto<UserResponse.SignUpDto> appleSignup(
		@Valid @RequestBody UserRequest.AppleSignUpDto dto
	) {
		UserResponse.SignUpDto signupDto = userFacade.signupApple(dto, SocialType.APPLE);
		return ResponseDto.onSuccess(signupDto);
	}

	@Operation(summary = "토큰 재발급", description = "토큰 재발급")
	@PostMapping(value = "/reissuance")
	@ApiErrorCodes(value = {
		ErrorStatus.USER_POST_ERROR,
		ErrorStatus.SOCIAL_LOGIN_FAILURE,
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<UserResponse.ReissueDto> reissueAccessToken(
		@Valid @RequestBody UserRequest.SignUpDto signUpDto
	) {
		UserResponse.ReissueDto reissueDto = userFacade.reissueAccessToken(signUpDto);
		return ResponseDto.onSuccess(reissueDto);
	}

	@Operation(summary = "로그아웃", description = "로그아웃")
	@PostMapping(value = "/logout")
	@ApiErrorCodes(value = {
		ErrorStatus.USER_POST_ERROR,
		ErrorStatus.SOCIAL_LOGIN_FAILURE,
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<Void> logout(
		@Valid @RequestBody UserRequest.LogoutDto logoutDto
	) {
		userFacade.logout(logoutDto);
		return ResponseDto.onSuccess(null);
	}

	@Operation(summary = "카카오 회원 탈퇴", description = "카카오 회원 탈퇴")
	@PostMapping("/kakao/delete")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<?> removeKakaoUser(
		HttpServletRequest request) {
		userFacade.removeKakaoUser(request);
		return ResponseDto.onSuccess(null);
	}

	@Operation(summary = "네이버 회원 탈퇴", description = "네이버 회원 탈퇴")
	@PostMapping("/naver/delete")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<?> removeNaverUser(
		HttpServletRequest request
	) {
		userFacade.removeNaverUser(request);
		return ResponseDto.onSuccess(null);
	}

	@SuppressWarnings({"checkstyle:WhitespaceAround", "checkstyle:RegexpMultiline"})
	@Operation(summary = "애플 회원 탈퇴", description = "애플 회원 탈퇴")
	@PostMapping("/apple/delete")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<?> removeAppleUser(
		HttpServletRequest request
	) {
		userFacade.removeAppleUser(request);
		return ResponseDto.onSuccess(null);
	}
}
