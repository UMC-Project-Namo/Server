package com.namo.spring.application.external.api.record.Controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.namo.spring.application.external.api.record.dto.DiaryRequest;
import com.namo.spring.application.external.api.record.usecase.DiaryUseCase;
import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCodes;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.response.ResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "기록(일기)", description = "기록(일기:diary) 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/diaries")
public class DiaryController {

	private final DiaryUseCase diaryUseCase;

	@Operation(summary = "기록 생성", description = "기록(일기) 생성 API 입니다. 개인, 단체 일정 모두 공통으로 사용합니다.")
	@ApiErrorCodes(value = {
		ErrorStatus.ALREADY_WRITTEN_DIARY_FAILURE,
		ErrorStatus.NOT_FOUND_PARTICIPANT_FAILURE,
	})
	@PostMapping("")
	public ResponseDto<String> createDiary(
		@AuthenticationPrincipal SecurityUserDetails memberInfo,
		@RequestBody DiaryRequest.CreateDiaryDto request
	) {
		diaryUseCase.createDiary(memberInfo, request);
		return ResponseDto.onSuccess("기록 생성 성공");
	}

}
