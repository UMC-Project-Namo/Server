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

import com.namo.spring.application.external.api.group.api.MeetingDiaryApi;
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
public class MeetingDiaryController implements MeetingDiaryApi {
	private final MeetingDiaryFacade meetingDiaryFacade;
	private final Converter converter;

	/**
	 * [이름 규칙 적용 x ] 모임 활동 추가
	 */
	@PostMapping(value = "/{moimScheduleId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseDto<Void> createMeetingActivity(
		@PathVariable Long moimScheduleId,
		@RequestPart(required = false) List<MultipartFile> createImages,
		@RequestParam String activityName,
		@RequestParam String activityMoney,
		@RequestParam List<Long> participantUserIds
	) {
		MeetingDiaryRequest.LocationDto locationDto = new MeetingDiaryRequest.LocationDto(activityName, activityMoney,
			participantUserIds);
		meetingDiaryFacade.createMeetingDiary(moimScheduleId, locationDto, createImages);
		return ResponseDto.onSuccess(null);
	}

	/**
	 * 모임 활동 수정
	 */
	@PatchMapping(value = "/{activityId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseDto<Object> updateMeetingActivity(
		@PathVariable Long activityId,
		@RequestPart(required = false) List<MultipartFile> createImages,
		@RequestParam(required = false) List<Long> deleteImageIds,
		@RequestParam String activityName,
		@RequestParam String activityMoney,
		@RequestParam List<Long> participantUserIds
	) {
		MeetingDiaryRequest.LocationDto locationDto = new MeetingDiaryRequest.LocationDto(activityName, activityMoney,
			participantUserIds);
		meetingDiaryFacade.modifyMeetingActivity(activityId, locationDto, createImages, deleteImageIds);
		return ResponseDto.onSuccess(null);
	}

	/**
	 * [이름 규칙 적용 x] 모임 기록 조회
	 */
	@GetMapping("/{moimScheduleId}")
	public ResponseDto<GroupDiaryResponse.GroupDiaryDto> getGroupDiary(
		@PathVariable("moimScheduleId") Long moimScheduleId
	) {
		GroupDiaryResponse.GroupDiaryDto groupDiaryDto = meetingDiaryFacade.getGroupDiaryWithLocations(moimScheduleId);
		return ResponseDto.onSuccess(groupDiaryDto);
	}

	/**
	 * [개인 페이지] 월간 모임 기록 조회
	 */
	@GetMapping("/month/{month}")
	public ResponseDto<MeetingDiaryResponse.SliceDiaryDto<MeetingDiaryResponse.DiaryDto>> findMonthMeetingDiary(
		@PathVariable("month") String month,
		Pageable pageable,
		@AuthenticationPrincipal SecurityUserDetails user
	) {
		List<LocalDateTime> localDateTimes = converter.convertLongToLocalDateTime(month);
		MeetingDiaryResponse.SliceDiaryDto<MeetingDiaryResponse.DiaryDto> diaryDto = meetingDiaryFacade
			.getMonthMonthMeetingDiary(user.getUserId(), localDateTimes, pageable);
		return ResponseDto.onSuccess(diaryDto);
	}

	/**
	 * [이름 규칙 적용 x] [개인 페이지] 모임 기록 상세 조회
	 */
	@GetMapping("/detail/{moimScheduleId}")
	public ResponseDto<MeetingDiaryResponse.DiaryDto> getMeetingDiaryDetail(
		@Parameter(description = "모임 일정 ID") @PathVariable Long moimScheduleId,
		@AuthenticationPrincipal SecurityUserDetails user
	) {
		Long userId = user.getUserId();
		MeetingDiaryResponse.DiaryDto diaryDto = meetingDiaryFacade.getMeetingDiaryDetail(moimScheduleId, userId);
		return ResponseDto.onSuccess(diaryDto);
	}

	/**
	 * [이름 규칙 적용 x] [개인 페이지] 모임 기록 삭제
	 */
	@DeleteMapping("/person/{scheduleId}")
	public ResponseDto<Object> removePersonMeetingDiary(
		@PathVariable Long scheduleId,
		@AuthenticationPrincipal SecurityUserDetails user
	) {
		Long userId = user.getUserId();
		meetingDiaryFacade.removePersonMeetingDiary(scheduleId, userId);
		return ResponseDto.onSuccess(null);
	}

	/**
	 * [이름 규칙 적용 x] 모임 기록 삭제
	 */
	@DeleteMapping("/all/{moimScheduleId}")
	public ResponseDto<Object> removeMeetingDiary(
		@PathVariable Long moimScheduleId
	) {
		meetingDiaryFacade.removeMeetingDiary(moimScheduleId);
		return ResponseDto.onSuccess(null);
	}

	/**
	 * 모임 활동 삭제
	 */
	@DeleteMapping("/{activityId}")
	public ResponseDto<Object> removeMeetingActivity(
		@PathVariable Long activityId
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
