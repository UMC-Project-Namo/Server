package com.namo.spring.application.external.api.user.controller;

import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.namo.spring.application.external.api.user.api.AuthApi;
import com.namo.spring.application.external.api.user.converter.MemberConverter;
import com.namo.spring.application.external.api.user.dto.MemberRequest;
import com.namo.spring.application.external.api.user.dto.MemberResponse;
import com.namo.spring.application.external.api.user.facade.AuthFacade;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.core.common.response.ResponseDto;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.type.SocialType;

import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "1. Auth", description = "로그인, 회원가입 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/auths")
public class AuthController implements AuthApi {

	private final AuthFacade memberFacade;

	@PostMapping(value = "/signup/apple")
	@PreAuthorize("isAnonymous()")
	public ResponseDto<MemberResponse.SignUpDto> appleSignup(
		@Valid @RequestBody MemberRequest.AppleSignUpDto dto
	) {
		MemberResponse.SignUpDto signupDto = memberFacade.signupApple(dto);
		return ResponseDto.onSuccess(signupDto);
	}

	@PostMapping(value = "/signup/{socialType}")
	@PreAuthorize("isAnonymous()")
	public ResponseDto<MemberResponse.SignUpDto> socialSignup(
		@Valid @RequestBody MemberRequest.SocialSignUpDto dto,
		@PathVariable(value = "socialType") SocialType socialType
	) {
		MemberResponse.SignUpDto signupDto = memberFacade.socialSignup(dto, socialType);
		return ResponseDto.onSuccess(signupDto);
	}

	@PostMapping(value = "/signup/complete")
	public ResponseDto<MemberResponse.SignUpDoneDto> completeSignup(
		@Valid @RequestBody MemberRequest.CompleteSignUpDto dto,
		@AuthenticationPrincipal SecurityUserDetails member
	) {
		Member target = memberFacade.completeSignup(dto, member.getUserId());
		return ResponseDto.onSuccess(MemberConverter.toSignUpDoneDto(target));
	}

	@PostMapping(value = "/reissuance")
	public ResponseDto<MemberResponse.ReissueDto> reissueAccessToken(
		@RequestHeader(value = "refreshToken") String refreshToken
	) {
		return ResponseDto.onSuccess(memberFacade.reissueAccessToken(refreshToken));
	}

	@PostMapping(value = "/logout")
	@PreAuthorize("isAuthenticated()")
	public ResponseDto<String> logout(
		@RequestHeader(value = "Authorization") String authHeader,
		@RequestHeader(value = "refreshToken") String refreshToken,
		@AuthenticationPrincipal SecurityUserDetails member
	) {
		String accessToken = authHeader.split(" ")[1];
		memberFacade.logout(member.getUserId(), accessToken, refreshToken);
		return ResponseDto.onSuccess("로그아웃 되었습니다.");
	}

	@PostMapping("/delete/{socialType}")
	@PreAuthorize("isAuthenticated()")
	public ResponseDto<String> removeAuthUser(
		@RequestHeader(value = "Authorization") String authHeader,
		@RequestHeader(value = "refreshToken") String refreshToken,
		@AuthenticationPrincipal SecurityUserDetails member
	) {
		String accessToken = authHeader.split(" ")[1];
		memberFacade.removeSocialMember(member.getUserId(), accessToken, refreshToken);
		return ResponseDto.onSuccess("회원탈퇴가 완료되었습니다.");
	}

}
