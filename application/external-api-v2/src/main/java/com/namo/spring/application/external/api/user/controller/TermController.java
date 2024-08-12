package com.namo.spring.application.external.api.user.controller;

import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.namo.spring.application.external.api.user.dto.MemberRequest;
import com.namo.spring.application.external.api.user.facade.TermFacade;
import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCodes;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.response.ResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "2. Term", description = "약관 동의 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/terms")
public class TermController {

	private final TermFacade termFacade;

	@Operation(summary = "약관을 동의합니다. ", description = "약관 동의 API")
	@PostMapping("")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<String> createTerm(@Valid @RequestBody MemberRequest.TermDto termDto,
		@AuthenticationPrincipal SecurityUserDetails member) {
		termFacade.termAgreement(termDto, member.getUserId());
		return ResponseDto.onSuccess("약관 동의 완료");
	}

}
