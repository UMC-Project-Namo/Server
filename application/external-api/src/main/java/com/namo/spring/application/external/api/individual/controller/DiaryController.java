package com.namo.spring.application.external.api.individual.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.namo.spring.application.external.api.individual.dto.DiaryResponse;
import com.namo.spring.application.external.api.individual.facade.DiaryFacade;
import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCodes;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.application.external.global.utils.Converter;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.response.ResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "4. Diary (개인)", description = "개인 기록 관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/diaries")
public class DiaryController {
	private final DiaryFacade diaryFacade;
	private final Converter converter;

	@Operation(summary = "기록 생성", description = "기록 생성 API")
	@PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<DiaryResponse.ScheduleIdDto> createDiary(
		@Parameter(description = "기록용 이미지") @RequestPart(required = false) List<MultipartFile> imgs,
		@Parameter(description = "일정 ID") @RequestPart String scheduleId,
		@Parameter(description = "기록 내용") @RequestPart(required = false) String content
	) {
		DiaryResponse.ScheduleIdDto dto = diaryFacade.createDiary(Long.valueOf(scheduleId), content, imgs);
		return ResponseDto.onSuccess(dto);
	}

	@Operation(summary = "일정 기록 월간 조회", description = "일정 기록 월간 조회 API")
	@GetMapping("/month/{month}")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<DiaryResponse.SliceDiaryDto> findDiaryByMonth(
		@Parameter(description = "조회 일자", example = "{년},{월}") @PathVariable("month") String month,
		Pageable pageable,
		@AuthenticationPrincipal SecurityUserDetails user
	) {
		Long userId = user.getUserId();
		List<LocalDateTime> localDateTimes = converter.convertLongToLocalDateTime(month);
		DiaryResponse.SliceDiaryDto diaries = diaryFacade.getMonthDiary(userId, localDateTimes, pageable);
		return ResponseDto.onSuccess(diaries);
	}

	@Operation(summary = "개인 일정 기록 전체 조회", description = "개인 일정 기록 전체 조회 API")
	@GetMapping("/all")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<List<DiaryResponse.GetDiaryByUserDto>> findAllDiary(
		@AuthenticationPrincipal SecurityUserDetails user
	) {
		Long userId = user.getUserId();
		List<DiaryResponse.GetDiaryByUserDto> diaries = diaryFacade.getAllDiariesByUser(userId);
		return ResponseDto.onSuccess(diaries);
	}

	//일정 별 기록 조회 == 1개 조회
	@Operation(summary = "일정 기록 개별 조회", description = "일정 기록 개별 조회 API")
	@GetMapping("/{scheduleId}")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<DiaryResponse.GetDiaryByScheduleDto> findDiaryById(
		@Parameter(description = "일정 ID") @PathVariable("scheduleId") Long scheduleId
	) {
		DiaryResponse.GetDiaryByScheduleDto diary = diaryFacade.getDiaryBySchedule(scheduleId);
		return ResponseDto.onSuccess(diary);
	}

	@Operation(summary = "일정 기록 수정", description = "일정 기록 수정 API")
	@PatchMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<String> updateDiary(
		@Parameter(description = "기록 이미지") @RequestPart(required = false) List<MultipartFile> imgs,
		@Parameter(description = "일정 ID") @RequestPart String scheduleId,
		@Parameter(description = "기록 내용") @RequestPart(required = false) String content
	) {
		diaryFacade.removeDiary(Long.valueOf(scheduleId));
		diaryFacade.createDiary(Long.valueOf(scheduleId), content, imgs);
		return ResponseDto.onSuccess("수정에 성공하셨습니다.");
	}

	@Operation(summary = "일정 기록 삭제", description = "일정 기록 삭제 API")
	@DeleteMapping("/{scheduleId}")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<String> deleteDiary(
		@Parameter(description = "일정 ID") @PathVariable("scheduleId") Long scheduleId
	) {
		diaryFacade.removeDiary(scheduleId);
		return ResponseDto.onSuccess("삭제에 성공하셨습니다.");
	}

	@Operation(summary = "일정 기록 이미지 삭제", description = "일정 이미지 개별 삭제 API")
	@DeleteMapping("/{scheduleId}/image/{imgUrl}")
	@ApiErrorCodes(value = {
		ErrorStatus.NOT_FOUND_IMAGE,
		ErrorStatus.NOT_IMAGE_IN_DIARY,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<String> deleteDiaryImage(
		@Parameter(description = "일정 ID") @PathVariable("scheduleId") Long scheduleId,
		@Parameter(description = "이미지 URL") @PathVariable("imgUrl") String imgUrl
	) {
		diaryFacade.removeDiaryImage(scheduleId, imgUrl);
		return ResponseDto.onSuccess("삭제에 성공하셨습니다.");
	}

}
