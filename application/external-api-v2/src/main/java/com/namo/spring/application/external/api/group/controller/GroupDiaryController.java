package com.namo.spring.application.external.api.group.controller;

import com.namo.spring.application.external.api.group.dto.GroupDiaryResponse;
import com.namo.spring.application.external.api.group.dto.GroupScheduleRequest;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "8. Diary (모임)", description = "모임 기록 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/group/diaries")
public class GroupDiaryController {
    private final Converter converter;

    @Operation(summary = "모임 기록 생성", description = "모임 기록 생성 API")
    @PostMapping(value = "/{moimScheduleId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<Void> createGroupDiary(
            @Parameter(description = "모임 일정 ID") @PathVariable Long moimScheduleId,
            @Parameter(description = "모임 기록용 이미지") @RequestPart(required = false) List<MultipartFile> imgs,
            @Parameter(description = "모임 기록명") @RequestPart String name,
            @Parameter(description = "모임 회비") @RequestPart String money,
            @Parameter(description = "참여자", example = "멍청이, 똑똑이") @RequestPart String participants
    ) {
        return null;
    }

    @Operation(summary = "모임 기록 장소 수정", description = "모임 기록 장소 수정 API")
    @PatchMapping(value = "/{activityId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<Object> updateGroupDiary(
            @Parameter(description = "수정하고자 하는 활동 ID") @PathVariable Long activityId,
            @Parameter(description = "모임 기록용 이미지") @RequestPart(required = false) List<MultipartFile> imgs,
            @Parameter(description = "모임 기록명") @RequestPart String name,
            @Parameter(description = "모임 회비") @RequestPart String money,
            @Parameter(description = "참여자", example = "멍청이, 똑똑이") @RequestPart String participants
    ) {
        return null;
    }

    @Operation(summary = "모임 기록 조회", description = "모임 기록 조회 API")
    @GetMapping("/{moimScheduleId}")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<Object> getGroupDiary(
            @Parameter(description = "모임 기록 ID") @PathVariable("moimScheduleId") Long moimScheduleId
    ) {
        return null;
    }

    @Operation(summary = "월간 모임 기록 조회", description = "월간 모임 기록 조회 API")
    @GetMapping("/month/{month}")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<GroupDiaryResponse.SliceDiaryDto<GroupDiaryResponse.DiaryDto>> findMonthGroupDiary(
            @Parameter(description = "조회 일자", example = "{년},{월}") @PathVariable("month") String month,
            Pageable pageable,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        return null;
    }

    @Operation(summary = "모임 기록 상세 조회", description = "모임 기록 상세 조회 API")
    @GetMapping("/detail/{moimScheduleId}")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<GroupDiaryResponse.DiaryDto> getGroupDiaryDetail(
            @Parameter(description = "모임 일정 ID") @PathVariable Long moimScheduleId,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        return null;
    }

    @Operation(summary = "개인 페이지 모임 기록 삭제", description = "일정에 대한 모임 활동 기록 삭제 API")
    @DeleteMapping("/person/{scheduleId}")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<Object> removePersonGroupDiary(
            @Parameter(description = "일정 ID") @PathVariable Long scheduleId,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        return null;
    }

    @Operation(summary = "모임 기록 전체 삭제", description = "일정에 대한 모임 기록 전체 삭제 API")
    @DeleteMapping("/all/{moimScheduleId}")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<Object> removeGroupDiary(
            @Parameter(description = "모임 일정 ID") @PathVariable Long moimScheduleId
    ) {
        return null;
    }

    @Operation(summary = "모임 활동 삭제", description = "모임 활동 삭제 API")
    @DeleteMapping("/{activityId}")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<Object> removeGroupActivity(
            @Parameter(description = "모임 활동 ID") @PathVariable Long activityId
    ) {
        return null;
    }

    @Operation(summary = "모임 기록 텍스트 추가 (모임 메모 추가)", description = "모임 기록 추가 API")
    @PatchMapping("/text/{moimScheduleId}")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<Object> createGroupMemo(
            @Parameter(description = "모임 일정 ID") @PathVariable Long moimScheduleId,
            @RequestBody GroupScheduleRequest.PostGroupScheduleTextDto moimScheduleText,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        return null;
    }
}
