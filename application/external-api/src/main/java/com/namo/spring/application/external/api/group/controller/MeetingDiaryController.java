package com.namo.spring.application.external.api.group.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.namo.spring.application.external.api.group.dto.GroupDiaryResponse;
import com.namo.spring.application.external.api.group.dto.MeetingDiaryRequest;
import com.namo.spring.application.external.api.group.dto.MeetingDiaryResponse;
import com.namo.spring.application.external.api.group.dto.MeetingScheduleRequest;
import com.namo.spring.application.external.api.group.facade.MeetingDiaryFacade;
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

@Tag(name = "8. Diary (모임)", description = "모임 기록 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/group/diaries")
public class MeetingDiaryController {
	private final MeetingDiaryFacade meetingDiaryFacade;
	private final Converter converter;

	// ver1
	@Operation(summary = "모임 기록 생성", description = "모임 기록 생성 API")
	@PostMapping(value = "/{moimScheduleId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<Void> createMeetingDiary(
		@Parameter(description = "모임 일정 ID") @PathVariable Long moimScheduleId,
		@Parameter(description = "추가할 모임 활동 이미지") @RequestPart(required = false) List<MultipartFile> createImages,
		@Parameter(description = "모임 기록명") @RequestParam String activityName,
		@Parameter(description = "모임 회비") @RequestParam String activityMoney,
		@Parameter(description = "참여자", example = "1, 2") @RequestParam List<Long> participantUserIds
	) {
		MeetingDiaryRequest.LocationDto locationDto = new MeetingDiaryRequest.LocationDto(activityName, activityMoney,
			participantUserIds);
		meetingDiaryFacade.createMeetingDiary(moimScheduleId, locationDto, createImages);
		return ResponseDto.onSuccess(null);
	}

	@Operation(summary = "모임 기록 수정", description = "모임 기록 수정 API")
	@PatchMapping(value = "/{activityId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<Object> updateMeetingDiary(
		@Parameter(description = "수정하고자 하는 활동 ID") @PathVariable Long activityId,
		@Parameter(description = "추가할 모임 활동 이미지") @RequestPart(required = false) List<MultipartFile> createImages,
		@Parameter(description = "삭제할 기록 이미지 ID") @RequestParam(required = false) List<Long> deleteImageIds,
		@Parameter(description = "모임 기록명") @RequestParam String activityName,
		@Parameter(description = "모임 회비") @RequestParam String activityMoney,
		@Parameter(description = "참여자", example = "1, 2") @RequestParam List<Long> participantUserIds
	) {
		MeetingDiaryRequest.LocationDto locationDto = new MeetingDiaryRequest.LocationDto(activityName, activityMoney,
			participantUserIds);
		meetingDiaryFacade.modifyMeetingActivity(activityId, locationDto, createImages, deleteImageIds);
		return ResponseDto.onSuccess(null);
	}

	// ver1
	@Operation(summary = "모임 기록 조회", description = "모임 기록 조회 API")
	@GetMapping("/{moimScheduleId}")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<GroupDiaryResponse.GroupDiaryDto> getGroupDiary(
		@Parameter(description = "모임 기록 ID") @PathVariable("moimScheduleId") Long moimScheduleId
	) {
		GroupDiaryResponse.GroupDiaryDto groupDiaryDto = meetingDiaryFacade.getGroupDiaryWithLocations(moimScheduleId);
		return ResponseDto.onSuccess(groupDiaryDto);
	}

	@Operation(summary = "월간 모임 기록 조회", description = "월간 모임 기록 조회 API")
	@GetMapping("/month/{month}")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<MeetingDiaryResponse.SliceDiaryDto<MeetingDiaryResponse.DiaryDto>> findMonthMeetingDiary(
		@Parameter(description = "조회 일자", example = "{년},{월}") @PathVariable("month") String month,
		Pageable pageable,
		@AuthenticationPrincipal SecurityUserDetails user
	) {
		List<LocalDateTime> localDateTimes = converter.convertLongToLocalDateTime(month);
		MeetingDiaryResponse.SliceDiaryDto<MeetingDiaryResponse.DiaryDto> diaryDto = meetingDiaryFacade
			.getMonthMonthMeetingDiary(user.getUserId(), localDateTimes, pageable);
		return ResponseDto.onSuccess(diaryDto);
	}

	// ver1
	@Operation(summary = "모임 기록 상세 조회", description = "모임 기록 상세 조회 API")
	@GetMapping("/detail/{moimScheduleId}")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<MeetingDiaryResponse.DiaryDto> getMeetingDiaryDetail(
		@Parameter(description = "모임 일정 ID") @PathVariable Long moimScheduleId,
		@AuthenticationPrincipal SecurityUserDetails user
	) {
		Long userId = user.getUserId();
		MeetingDiaryResponse.DiaryDto diaryDto = meetingDiaryFacade.getMeetingDiaryDetail(moimScheduleId, userId);
		return ResponseDto.onSuccess(diaryDto);
	}

	// ver1
	@Operation(summary = "개인 페이지 모임 기록 삭제", description = "일정에 대한 모임 활동 기록 삭제 API")
	@DeleteMapping("/person/{scheduleId}")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<Object> removePersonMeetingDiary(
		@Parameter(description = "모임 일정 ID") @PathVariable Long scheduleId,
		@AuthenticationPrincipal SecurityUserDetails user
	) {
		Long userId = user.getUserId();
		meetingDiaryFacade.removePersonMeetingDiary(scheduleId, userId);
		return ResponseDto.onSuccess(null);
	}

	// ver1
	@Operation(summary = "모임 기록 전체 삭제", description = "일정에 대한 모임 기록 전체 삭제 API")
	@DeleteMapping("/all/{moimScheduleId}")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<Object> removeMeetingDiary(
		@Parameter(description = "모임 일정 ID") @PathVariable Long moimScheduleId
	) {
		meetingDiaryFacade.removeMeetingDiary(moimScheduleId);
		return ResponseDto.onSuccess(null);
	}

	@Operation(summary = "모임 활동 삭제", description = "모임 활동 삭제 API")
	@DeleteMapping("/{activityId}")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<Object> removeMeetingActivity(
		@Parameter(description = "모임 활동 ID") @PathVariable Long activityId
	) {
		meetingDiaryFacade.removeMeetingActivity(activityId);
		return ResponseDto.onSuccess(null);
	}

	// ver1
	@Operation(summary = "모임 기록 텍스트 추가 (모임 메모 추가)", description = "모임 메모 추가 API")
	@PatchMapping("/text/{moimScheduleId}")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<Object> createMeetingMemo(
		@Parameter(description = "모임 일정 ID") @PathVariable Long moimScheduleId,
		@RequestBody MeetingScheduleRequest.PostMeetingScheduleTextDto meetingScheduleText,
		@AuthenticationPrincipal SecurityUserDetails user
	) {
		meetingDiaryFacade.createMeetingMemo(moimScheduleId,
			user.getUserId(),
			meetingScheduleText);
		return ResponseDto.onSuccess(null);
	}
}
