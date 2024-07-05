package com.namo.spring.application.external.api.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.namo.spring.application.external.api.user.dto.UserRequest;
import com.namo.spring.application.external.api.user.facade.UserFacade;
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
@RequestMapping("/api/v1/terms")
public class TermController {
	private final UserFacade userFacade;

	@Operation(summary = "약관을 동의합니다. ", description = "약관 동의 API")
	@PostMapping("")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<Void> createTerm(@Valid @RequestBody UserRequest.TermDto termDto, @AuthenticationPrincipal SecurityUserDetails user) {
		userFacade.createTerm(termDto, user.getUserId());
		return ResponseDto.onSuccess(null);
	}

}
