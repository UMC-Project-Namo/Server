package com.namo.spring.application.external.api.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.namo.spring.application.external.api.user.api.AuthApi;
import com.namo.spring.application.external.api.user.dto.UserRequest;
import com.namo.spring.application.external.api.user.dto.UserResponse;
import com.namo.spring.application.external.api.user.facade.UserFacade;
import com.namo.spring.db.mysql.domains.user.type.SocialType;
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
public class AuthController implements AuthApi {
	private final UserFacade userFacade;

	@PostMapping(value = "/kakao/signup")
	@PreAuthorize("isAnonymous()")
	public ResponseDto<UserResponse.SignUpDto> kakaoSignup(
		@Valid @RequestBody UserRequest.SocialSignUpDto signUpDto
	) {
		UserResponse.SignUpDto signupDto = userFacade.signupKakao(signUpDto, SocialType.KAKAO);
		return ResponseDto.onSuccess(signupDto);
	}

	@PostMapping(value = "/naver/signup")
	@PreAuthorize("isAnonymous()")
	public ResponseDto<UserResponse.SignUpDto> naverSignup(
		@Valid @RequestBody UserRequest.SocialSignUpDto signUpDto
	) {
		UserResponse.SignUpDto signupDto = userFacade.signupNaver(signUpDto, SocialType.NAVER);
		return ResponseDto.onSuccess(signupDto);
	}

	@PostMapping(value = "/apple/signup")
	@PreAuthorize("isAnonymous()")
	public ResponseDto<UserResponse.SignUpDto> appleSignup(
		@Valid @RequestBody UserRequest.AppleSignUpDto dto
	) {
		UserResponse.SignUpDto signupDto = userFacade.signupApple(dto, SocialType.APPLE);
		return ResponseDto.onSuccess(signupDto);
	}

	@PostMapping(value = "/reissuance")
	@PreAuthorize("isAnonymous()")
	public ResponseDto<UserResponse.ReissueDto> reissueAccessToken(
		@Valid @RequestBody UserRequest.ReissueDto reissueDto
	) {
		UserResponse.ReissueDto result = userFacade.reissueAccessToken(reissueDto);
		return ResponseDto.onSuccess(result);
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
	@PreAuthorize("isAuthenticated()")
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
	@PreAuthorize("isAuthenticated()")
	public ResponseDto<Void> removeKakaoUser(
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
	@PreAuthorize("isAuthenticated()")
	public ResponseDto<Void> removeNaverUser(
		HttpServletRequest request
	) {
		userFacade.removeNaverUser(request);
		return ResponseDto.onSuccess(null);
	}

	@Operation(summary = "애플 회원 탈퇴", description = "애플 회원 탈퇴")
	@PostMapping("/apple/delete")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	@PreAuthorize("isAuthenticated()")
	public ResponseDto<Void> removeAppleUser(
		HttpServletRequest request
	) {
		userFacade.removeAppleUser(request);
		return ResponseDto.onSuccess(null);
	}
}
