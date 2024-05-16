package com.example.namo2.domain.group.ui;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
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

import com.example.namo2.domain.group.application.MoimMemoFacade;
import com.example.namo2.domain.group.ui.dto.GroupDiaryRequest;
import com.example.namo2.domain.group.ui.dto.GroupDiaryResponse;
import com.example.namo2.domain.group.ui.dto.GroupScheduleRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.example.namo2.global.annotation.swagger.ApiErrorCodes;
import com.example.namo2.global.common.response.BaseResponse;
import com.example.namo2.global.common.response.BaseResponseStatus;
import com.example.namo2.global.utils.Converter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "8. Diary (모임)", description = "모임 기록 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/group/diaries")
public class GroupDiaryController {
	private final MoimMemoFacade moimMemoFacade;
	private final Converter converter;

	@Operation(summary = "모임 기록 생성", description = "모임 기록 생성 API")
	@PostMapping(value = "/{moimScheduleId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiErrorCodes(value = {
		BaseResponseStatus.EMPTY_ACCESS_KEY,
		BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
		BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
		BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<Void> createMoimMemo(
		@Parameter(description = "모임 일정 ID") @PathVariable Long moimScheduleId,
		@Parameter(description = "모임 기록용 이미지") @RequestPart(required = false) List<MultipartFile> imgs,
		@Parameter(description = "모임 기록명") @RequestPart String name,
		@Parameter(description = "모임 회비") @RequestPart String money,
		@Parameter(description = "참여자", example = "멍청이, 똑똑이") @RequestPart String participants
	) {
		GroupDiaryRequest.LocationDto locationDto = new GroupDiaryRequest.LocationDto(name, money, participants);
		moimMemoFacade.create(moimScheduleId, locationDto, imgs);
		return BaseResponse.ok();
	}

	@Operation(summary = "모임 기록 장소 수정", description = "모임 기록 장소 수정 API")
	@PatchMapping(value = "/{activityId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiErrorCodes(value = {
		BaseResponseStatus.EMPTY_ACCESS_KEY,
		BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
		BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
		BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<Object> updateMoimMemo(
		@Parameter(description = "수정하고자 하는 활동 ID") @PathVariable Long activityId,
		@Parameter(description = "모임 기록용 이미지") @RequestPart(required = false) List<MultipartFile> imgs,
		@Parameter(description = "모임 기록명") @RequestPart String name,
		@Parameter(description = "모임 회비") @RequestPart String money,
		@Parameter(description = "참여자", example = "멍청이, 똑똑이") @RequestPart String participants
	) {
		GroupDiaryRequest.LocationDto locationDto = new GroupDiaryRequest.LocationDto(name, money, participants);
		moimMemoFacade.modifyMoimMemoLocation(activityId, locationDto, imgs);
		return BaseResponse.ok();
	}

	@Operation(summary = "모임 기록 조회", description = "모임 기록 조회 API")
	@GetMapping("/{moimScheduleId}")
	@ApiErrorCodes(value = {
		BaseResponseStatus.EMPTY_ACCESS_KEY,
		BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
		BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
		BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<Object> getMoimMemo(
		@Parameter(description = "모임 기록 ID") @PathVariable("moimScheduleId") Long moimScheduleId
	) {
		GroupDiaryResponse.MoimDiaryDto moimDiaryDto = moimMemoFacade.getMoimMemoWithLocations(moimScheduleId);
		return new BaseResponse(moimDiaryDto);
	}

	@Operation(summary = "월간 모임 기록 조회", description = "월간 모임 기록 조회 API")
	@GetMapping("/month/{month}")
	@ApiErrorCodes(value = {
		BaseResponseStatus.EMPTY_ACCESS_KEY,
		BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
		BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
		BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<GroupDiaryResponse.SliceDiaryDto<GroupDiaryResponse.DiaryDto>> findMonthMoimMemo(
		@Parameter(description = "조회 일자", example = "{년},{월}") @PathVariable("month") String month,
		Pageable pageable,
		HttpServletRequest request
	) {
		List<LocalDateTime> localDateTimes = converter.convertLongToLocalDateTime(month);
		GroupDiaryResponse.SliceDiaryDto<GroupDiaryResponse.DiaryDto> diaryDto = moimMemoFacade
			.getMonthMonthMoimMemo((Long)request.getAttribute("userId"), localDateTimes, pageable);
		return new BaseResponse(diaryDto);
	}

	@Operation(summary = "개인 페이지 모임 기록 삭제", description = "일정에 대한 모임 활동 기록 삭제 API")
	@DeleteMapping("/person/{scheduleId}")
	@ApiErrorCodes(value = {
		BaseResponseStatus.EMPTY_ACCESS_KEY,
		BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
		BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
		BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<Object> removePersonMoimMemo(
		@Parameter(description = "일정 ID") @PathVariable Long scheduleId,
		HttpServletRequest request
	) {
		Long userId = (Long)request.getAttribute("userId");
		moimMemoFacade.removePersonMoimMemo(scheduleId, userId);
		return BaseResponse.ok();
	}

	@Operation(summary = "모임 기록 전체 삭제", description = "일정에 대한 모임 기록 전체 삭제 API")
	@DeleteMapping("/all/{moimScheduleId}")
	@ApiErrorCodes(value = {
		BaseResponseStatus.EMPTY_ACCESS_KEY,
		BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
		BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
		BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<Object> removeMoimMemo(
		@Parameter(description = "모임 일정 ID") @PathVariable Long moimScheduleId
	) {
		moimMemoFacade.removeMoimMemo(moimScheduleId);
		return BaseResponse.ok();
	}

	@Operation(summary = "모임 활동 삭제", description = "모임 활동 삭제 API")
	@DeleteMapping("/{activityId}")
	@ApiErrorCodes(value = {
		BaseResponseStatus.EMPTY_ACCESS_KEY,
		BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
		BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
		BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<Object> removeMoimMemoLocation(
		@Parameter(description = "모임 활동 ID") @PathVariable Long activityId
	) {
		moimMemoFacade.removeMoimMemoLocation(activityId);
		return BaseResponse.ok();
	}

	@Operation(summary = "모임 기록 텍스트 추가", description = "모임 기록 추가 API")
	@PatchMapping("/text/{moimScheduleId}")
	@ApiErrorCodes(value = {
		BaseResponseStatus.EMPTY_ACCESS_KEY,
		BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
		BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
		BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<Object> createMoimScheduleText(
		@Parameter(description = "모임 일정 ID") @PathVariable Long moimScheduleId,
		@RequestBody GroupScheduleRequest.PostGroupScheduleTextDto moimScheduleText,
		HttpServletRequest request
	) {
		moimMemoFacade.createMoimScheduleText(moimScheduleId,
			(Long)request.getAttribute("userId"),
			moimScheduleText);
		return BaseResponse.ok();
	}
}
