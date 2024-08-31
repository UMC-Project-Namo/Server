package com.namo.spring.application.external.api.record.Controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.namo.spring.application.external.api.record.dto.DiaryRequest;
import com.namo.spring.application.external.api.record.dto.DiaryResponse;
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

	@PatchMapping("/{diaryId}")
	public ResponseDto<String> updateDiary(
		@AuthenticationPrincipal SecurityUserDetails memberInfo,
		@PathVariable Long diaryId,
		@RequestBody DiaryRequest.UpdateDiaryDto request
	) {
		diaryUseCase.updateDiary(diaryId, memberInfo, request);
		return ResponseDto.onSuccess("기록 수정 성공");
	}

	@Operation(summary = "기록 조회", description = "기록(일기) 단일 상세 조회 API 입니다. 개인, 단체 일정 모두 공통으로 사용합니다.")
	@ApiErrorCodes(value = {
		ErrorStatus.NOT_FOUND_PARTICIPANT_FAILURE,
		ErrorStatus.NOT_WRITTEN_DIARY_FAILURE,
	})
	@GetMapping("/{scheduleId}")
	public ResponseDto<DiaryResponse.DiaryDto> getDiary(
		@AuthenticationPrincipal SecurityUserDetails memberInfo,
		@PathVariable Long scheduleId
	) {
		return ResponseDto.onSuccess(diaryUseCase
			.getScheduleDiary(scheduleId, memberInfo));
	}

	@Operation(summary = "기록 보관함 조회", description = "기록(일기) 보관함 조회 API 입니다. ")
	@GetMapping("/archive")
	public ResponseDto<DiaryResponse.DiaryArchiveDto> getDiaryArchive(
		@AuthenticationPrincipal SecurityUserDetails memberInfo,
		@ParameterObject Pageable pageable
	) {
		return ResponseDto.onSuccess(null);
	}
}
