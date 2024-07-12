package com.namo.spring.application.external.api.group.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.namo.spring.application.external.api.group.dto.MeetingDiaryRequest;
import com.namo.spring.application.external.api.group.dto.MeetingDiaryResponse;
import com.namo.spring.application.external.api.group.dto.MeetingScheduleRequest;
import com.namo.spring.application.external.api.group.facade.MeetingDiaryFacade;
import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCodes;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.response.ResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "8. Diary (모임) - 네임 규칙 적용", description = "모임 기록 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/temp/group/diaries")
public class TempMeetingDiaryController {
	private final MeetingDiaryFacade meetingDiaryFacade;

	@Operation(summary = "모임 기록 생성", description = "모임 기록 생성 API")
	@PostMapping(value = "/{meetingScheduleId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<Void> createMeetingDiary(
		@Parameter(description = "모임 일정 ID") @PathVariable Long meetingScheduleId,
		@Parameter(description = "모임 기록용 이미지") @RequestPart(required = false) List<MultipartFile> imgs,
		@Parameter(description = "모임 기록명") @RequestPart String name,
		@Parameter(description = "모임 회비") @RequestPart String money,
		@Parameter(description = "참여자", example = "멍청이, 똑똑이") @RequestPart String participants
	) {
		MeetingDiaryRequest.LocationDto locationDto = new MeetingDiaryRequest.LocationDto(name, money, participants);
		meetingDiaryFacade.createMeetingDiary(meetingScheduleId, locationDto, imgs);
		return ResponseDto.onSuccess(null);
	}

	@Operation(summary = "모임 기록 조회", description = "모임 기록 조회 API")
	@GetMapping("/{meetingScheduleId}")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<MeetingDiaryResponse.MeetingDiaryDto> getMeetingDiary(
		@Parameter(description = "모임 일정 ID") @PathVariable("meetingScheduleId") Long meetingScheduleId
	) {
		MeetingDiaryResponse.MeetingDiaryDto meetingDiaryDto = meetingDiaryFacade.getMeetingDiaryWithLocations(
			meetingScheduleId);
		return ResponseDto.onSuccess(meetingDiaryDto);
	}

	@Operation(summary = "모임 기록 상세 조회", description = "모임 기록 상세 조회 API")
	@GetMapping("/detail/{meetingScheduleId}")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<MeetingDiaryResponse.DiaryDto> getMeetingDiaryDetail(
		@Parameter(description = "모임 일정 ID") @PathVariable Long meetingScheduleId,
		@AuthenticationPrincipal SecurityUserDetails user
	) {
		Long userId = user.getUserId();
		MeetingDiaryResponse.DiaryDto diaryDto = meetingDiaryFacade.getMeetingDiaryDetail(meetingScheduleId, userId);
		return ResponseDto.onSuccess(diaryDto);
	}

	@Operation(summary = "개인 페이지 모임 기록 삭제", description = "일정에 대한 모임 활동 기록 삭제 API")
	@DeleteMapping("/person/{meetingScheduleId}")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<Object> removePersonMeetingDiary(
		@Parameter(description = "모임 일정 ID") @PathVariable Long meetingScheduleId,
		@AuthenticationPrincipal SecurityUserDetails user
	) {
		Long userId = user.getUserId();
		meetingDiaryFacade.removePersonMeetingDiary(meetingScheduleId, userId);
		return ResponseDto.onSuccess(null);
	}

	@Operation(summary = "모임 기록 전체 삭제", description = "일정에 대한 모임 기록 전체 삭제 API")
	@DeleteMapping("/all/{meetingScheduleId}")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<Object> removeMeetingDiary(
		@Parameter(description = "모임 일정 ID") @PathVariable Long meetingScheduleId
	) {
		meetingDiaryFacade.removeMeetingDiary(meetingScheduleId);
		return ResponseDto.onSuccess(null);
	}

	@Operation(summary = "모임 기록 텍스트 추가 (모임 메모 추가)", description = "모임 메모 추가 API")
	@PatchMapping("/text/{meetingScheduleId}")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<Object> createMeetingMemo(
		@Parameter(description = "모임 일정 ID") @PathVariable Long meetingScheduleId,
		@RequestBody MeetingScheduleRequest.PostMeetingScheduleTextDto meetingScheduleText,
		@AuthenticationPrincipal SecurityUserDetails user
	) {
		meetingDiaryFacade.createMeetingMemo(meetingScheduleId,
			user.getUserId(),
			meetingScheduleText);
		return ResponseDto.onSuccess(null);
	}
}
