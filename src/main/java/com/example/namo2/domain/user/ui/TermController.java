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
import com.example.namo2.domain.user.ui.dto.UserRequest;

import com.example.namo2.global.annotation.swagger.ApiErrorCodes;
import com.example.namo2.global.common.response.BaseResponse;
import com.example.namo2.global.common.response.BaseResponseStatus;

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
		BaseResponseStatus.EMPTY_ACCESS_KEY,
		BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
		BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
		BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<Void> createTerm(@Valid @RequestBody UserRequest.TermDto termDto, HttpServletRequest request) {
		userFacade.createTerm(termDto, (Long)request.getAttribute("userId"));
		return BaseResponse.ok();
	}

}
