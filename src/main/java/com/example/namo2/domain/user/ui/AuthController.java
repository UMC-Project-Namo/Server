package com.example.namo2.domain.user.ui;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.example.namo2.domain.user.application.UserFacade;
import com.example.namo2.domain.user.domain.constant.SocialType;
import com.example.namo2.domain.user.ui.dto.UserRequest;
import com.example.namo2.domain.user.ui.dto.UserResponse;

import com.example.namo2.global.annotation.swagger.ApiErrorCodes;
import com.example.namo2.global.common.response.BaseResponse;
import com.example.namo2.global.common.response.BaseResponseStatus;

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
		BaseResponseStatus.USER_POST_ERROR,
		BaseResponseStatus.KAKAO_UNAUTHORIZED,
		BaseResponseStatus.KAKAO_FORBIDDEN,
		BaseResponseStatus.KAKAO_BAD_GATEWAY,
		BaseResponseStatus.KAKAO_SERVICE_UNAVAILABLE,
		BaseResponseStatus.KAKAO_INTERNAL_SERVER_ERROR,
		BaseResponseStatus.SOCIAL_LOGIN_FAILURE,
		BaseResponseStatus.EMPTY_ACCESS_KEY,
		BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
		BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
		BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<UserResponse.SignUpDto> kakaoSignup(
		@Valid @RequestBody UserRequest.SocialSignUpDto signUpDto
	) {
		UserResponse.SignUpDto signupDto = userFacade.signupKakao(signUpDto, SocialType.KAKAO);
		return new BaseResponse<>(signupDto);
	}

	@Operation(summary = "네이버 회원가입", description = "네이버 소셜 로그인을 통한 회원가입")
	@PostMapping(value = "/naver/signup")
	@ApiErrorCodes(value = {
		BaseResponseStatus.USER_POST_ERROR,
		BaseResponseStatus.NAVER_UNAUTHORIZED,
		BaseResponseStatus.NAVER_FORBIDDEN,
		BaseResponseStatus.SOCIAL_LOGIN_FAILURE,
		BaseResponseStatus.EMPTY_ACCESS_KEY,
		BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
		BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
		BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<UserResponse.SignUpDto> naverSignup(
		@Valid @RequestBody UserRequest.SocialSignUpDto signUpDto
	) {
		UserResponse.SignUpDto signupDto = userFacade.signupNaver(signUpDto, SocialType.NAVER);
		return new BaseResponse<>(signupDto);
	}

	@Operation(summary = "애플 회원가입", description = "애플 소셜 로그인을 통한 회원가입.")
	@PostMapping(value = "/apple/signup")
	@ApiErrorCodes(value = {
		BaseResponseStatus.USER_POST_ERROR,
		BaseResponseStatus.MAKE_PUBLIC_KEY_FAILURE,
		BaseResponseStatus.APPLE_REQUEST_ERROR,
		BaseResponseStatus.APPLE_UNAUTHORIZED,
		BaseResponseStatus.SOCIAL_LOGIN_FAILURE,
		BaseResponseStatus.FEIGN_SERVER_ERROR
	})
	public BaseResponse<UserResponse.SignUpDto> appleSignup(
		@Valid @RequestBody UserRequest.AppleSignUpDto dto
	) {
		UserResponse.SignUpDto signupDto = userFacade.signupApple(dto, SocialType.APPLE);
		return new BaseResponse<>(signupDto);
	}

	@Operation(summary = "토큰 재발급", description = "토큰 재발급")
	@PostMapping(value = "/reissuance")
	@ApiErrorCodes(value = {
		BaseResponseStatus.USER_POST_ERROR,
		BaseResponseStatus.SOCIAL_LOGIN_FAILURE,
		BaseResponseStatus.EMPTY_ACCESS_KEY,
		BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
		BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
		BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<UserResponse.ReissueDto> reissueAccessToken(
		@Valid @RequestBody UserRequest.SignUpDto signUpDto
	) {
		UserResponse.ReissueDto reissueDto = userFacade.reissueAccessToken(signUpDto);
		return new BaseResponse<>(reissueDto);
	}

	@Operation(summary = "로그아웃", description = "로그아웃")
	@PostMapping(value = "/logout")
	@ApiErrorCodes(value = {
		BaseResponseStatus.USER_POST_ERROR,
		BaseResponseStatus.SOCIAL_LOGIN_FAILURE,
		BaseResponseStatus.EMPTY_ACCESS_KEY,
		BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
		BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
		BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<Void> logout(
		@Valid @RequestBody UserRequest.LogoutDto logoutDto
	) {
		userFacade.logout(logoutDto);
		return BaseResponse.ok();
	}

	@Operation(summary = "카카오 회원 탈퇴", description = "카카오 회원 탈퇴")
	@PostMapping("/kakao/delete")
	@ApiErrorCodes(value = {
		BaseResponseStatus.EMPTY_ACCESS_KEY,
		BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
		BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
		BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<?> removeKakaoUser(
		HttpServletRequest request) {
		userFacade.removeKakaoUser(request);
		return BaseResponse.ok();
	}

	@Operation(summary = "네이버 회원 탈퇴", description = "네이버 회원 탈퇴")
	@PostMapping("/naver/delete")
	@ApiErrorCodes(value = {
		BaseResponseStatus.EMPTY_ACCESS_KEY,
		BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
		BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
		BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<?> removeNaverUser(
		HttpServletRequest request
	) {
		userFacade.removeNaverUser(request);
		return BaseResponse.ok();
	}

	@SuppressWarnings({"checkstyle:WhitespaceAround", "checkstyle:RegexpMultiline"})
	@Operation(summary = "애플 회원 탈퇴", description = "애플 회원 탈퇴")
	@PostMapping("/apple/delete")
	@ApiErrorCodes(value = {
		BaseResponseStatus.EMPTY_ACCESS_KEY,
		BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
		BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
		BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<?> removeAppleUser(
		HttpServletRequest request,
		@Valid @RequestBody UserRequest.DeleteAppleUserDto deleteAppleUserDto
	) {
		userFacade.removeAppleUser(request, deleteAppleUserDto.getAuthorizationCode());
		return BaseResponse.ok();
	}
}
