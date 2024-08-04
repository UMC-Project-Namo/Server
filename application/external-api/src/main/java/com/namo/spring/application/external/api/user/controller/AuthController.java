package com.namo.spring.application.external.api.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.namo.spring.application.external.api.user.api.AuthApi;
import com.namo.spring.application.external.api.user.dto.UserRequest;
import com.namo.spring.application.external.api.user.dto.UserResponse;
import com.namo.spring.application.external.api.user.facade.UserFacade;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.core.common.response.ResponseDto;
import com.namo.spring.db.mysql.domains.user.type.SocialType;

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
	@PreAuthorize("isAuthenticated()")
	public ResponseDto<UserResponse.ReissueDto> reissueAccessToken(
		@Valid @RequestBody UserRequest.ReissueDto reissueDto
	) {
		UserResponse.ReissueDto result = userFacade.reissueAccessToken(reissueDto);
		return ResponseDto.onSuccess(result);
	}

	@PostMapping(value = "/logout")
	@PreAuthorize("isAuthenticated()")
	public ResponseDto<Void> logout(
		@Valid @RequestBody UserRequest.LogoutDto logoutDto
	) {
		userFacade.logout(logoutDto);
		return ResponseDto.onSuccess(null);
	}

	@PostMapping("/kakao/delete")
	@PreAuthorize("isAuthenticated()")
	public ResponseDto<Void> removeKakaoUser(
		@AuthenticationPrincipal SecurityUserDetails user
	) {
		userFacade.removeKakaoUser(user.getUserId());
		return ResponseDto.onSuccess(null);
	}

	@PostMapping("/naver/delete")
	@PreAuthorize("isAuthenticated()")
	public ResponseDto<Void> removeNaverUser(
		@AuthenticationPrincipal SecurityUserDetails user
	) {
		userFacade.removeNaverUser(user.getUserId());
		return ResponseDto.onSuccess(null);
	}

	@PostMapping("/apple/delete")
	@PreAuthorize("isAuthenticated()")
	public ResponseDto<Void> removeAppleUser(
		@AuthenticationPrincipal SecurityUserDetails user
	) {
		userFacade.removeAppleUser(user.getUserId());
		return ResponseDto.onSuccess(null);
	}
}
