package com.namo.spring.application.external.api.schedule.api;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.namo.spring.application.external.api.schedule.dto.MeetingScheduleRequest;
import com.namo.spring.application.external.api.schedule.dto.MeetingScheduleResponse;
import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCodes;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.response.ResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "모임 일정", description = "모임 일정 관련 API")
public interface MeetingScheduleApi {
    @Operation(summary = "모임 일정 생성", description = "모임 일정을 생성합니다. 요청 성공 시 모임 일정 ID를 전송합니다.")
    @ApiErrorCodes(value = {
            ErrorStatus.NOT_INCLUDE_OWNER_IN_REQUEST,
            ErrorStatus.DUPLICATE_MEETING_PARTICIPANT,
            ErrorStatus.NOT_FOUND_USER_FAILURE,
            ErrorStatus.INVALID_MEETING_PARTICIPANT_COUNT,
            ErrorStatus.NOT_FOUND_FRIENDSHIP_FAILURE,
            ErrorStatus.INVALID_DATE,
            ErrorStatus.NOT_FOUND_CATEGORY_FAILURE,
            ErrorStatus.NOT_FOUND_PALETTE_FAILURE,

    })
    ResponseDto<Long> createMeetingSchedule(
            @Parameter(description = "생성 요청 dto") @Valid @RequestPart MeetingScheduleRequest.PostMeetingScheduleDto dto,
            @AuthenticationPrincipal SecurityUserDetails member);

    @Operation(summary = "모임 일정 목록 조회", description = "모임 일정 목록을 조회합니다.")
    @ApiErrorCodes(value = {ErrorStatus.NOT_FOUND_PARTICIPANT_FAILURE})
    ResponseDto<List<MeetingScheduleResponse.GetMeetingScheduleSummaryDto>> getMyMeetingSchedules(
            @AuthenticationPrincipal SecurityUserDetails member);

    @Operation(summary = "모임 초대자 월간 일정 조회", description = "모임에 초대할 유저들의 월간 일정을 조회합니다.")
    @ApiErrorCodes(value = {
            ErrorStatus.INVALID_FORMAT_FAILURE,
            ErrorStatus.INVALID_MEETING_PARTICIPANT_COUNT,
            ErrorStatus.DUPLICATE_MEETING_PARTICIPANT,
            ErrorStatus.NOT_FOUND_FRIENDSHIP_FAILURE,
    })
    ResponseDto<List<MeetingScheduleResponse.GetMonthlyMembersScheduleDto>> getMonthlyParticipantSchedules(
            @Parameter(description = "연도") @RequestParam Integer year,
            @Parameter(description = "월") @RequestParam Integer month,
            @Parameter(description = "초대자 ID 목록") @RequestParam List<Long> participantIds,
            @AuthenticationPrincipal SecurityUserDetails member);

    @Operation(summary = "모임 월간 일정 조회", description = "모임에 있는 유저들의 월간 일정을 조회합니다.")
    @ApiErrorCodes(value = {
            ErrorStatus.INVALID_FORMAT_FAILURE,
            ErrorStatus.NOT_SCHEDULE_PARTICIPANT,
            ErrorStatus.NOT_FOUND_SCHEDULE_FAILURE,
            ErrorStatus.NOT_MEETING_SCHEDULE,
    })
    ResponseDto<List<MeetingScheduleResponse.GetMonthlyMeetingParticipantScheduleDto>> getMonthlyMeetingParticipantSchedules(
            @Parameter(description = "모임 일정 ID") @PathVariable Long meetingScheduleId,
            @Parameter(description = "연도") @RequestParam Integer year,
            @Parameter(description = "월") @RequestParam Integer month,
            @AuthenticationPrincipal SecurityUserDetails member);

    @Operation(summary = "모임 일정 수정", description = "모임 일정을 수정합니다. 수정 권한은 모임의 방장에게만 있습니다.")
    @ApiErrorCodes(value = {
            ErrorStatus.NOT_INCLUDE_OWNER_IN_REQUEST,
            ErrorStatus.DUPLICATE_MEETING_PARTICIPANT,
            ErrorStatus.NOT_FOUND_SCHEDULE_FAILURE,
            ErrorStatus.NOT_MEETING_SCHEDULE,
            ErrorStatus.NOT_FOUND_FRIENDSHIP_FAILURE,
            ErrorStatus.NOT_SCHEDULE_OWNER,
            ErrorStatus.NOT_SCHEDULE_PARTICIPANT,
            ErrorStatus.NOT_FOUND_PARTICIPANT_FAILURE
    })
    ResponseDto<String> updateMeetingSchedule(
            @Parameter(description = "모임 일정 ID") @PathVariable Long meetingScheduleId,
            @Parameter(description = "수정 요청 dto") @RequestBody @Valid MeetingScheduleRequest.PatchMeetingScheduleDto dto,
            @AuthenticationPrincipal SecurityUserDetails memberInfo);

    @Operation(summary = "모임 일정 프로필 수정", description = "모임 일정의 이미지와 제목을 커스텀합니다.")
    ResponseDto<String> updateMeetingScheduleProfile(
            @Parameter(description = "모임 일정 ID") @PathVariable Long meetingScheduleId,
            @Parameter(description = "수정 요청 dto") @RequestBody @Valid MeetingScheduleRequest.PatchMeetingScheduleProfileDto dto,
            @AuthenticationPrincipal SecurityUserDetails memberInfo);

    @Operation(summary = "모임 상세 조회", description = "모임 일정을 상세 조회합니다.")
    @ApiErrorCodes(value = {
            ErrorStatus.NOT_MEETING_SCHEDULE,
            ErrorStatus.NOT_FOUND_FRIENDSHIP_FAILURE,
            ErrorStatus.NOT_SCHEDULE_PARTICIPANT,
    })
    ResponseDto<MeetingScheduleResponse.GetMeetingScheduleDto> getMeetingSchedule(
            @Parameter(description = "모임 일정 ID") @PathVariable Long meetingScheduleId,
            @AuthenticationPrincipal SecurityUserDetails memberInfo);

    @Operation(summary = "게스트 초대용 링크 조회", description = "게스트 초대용 링크를 조회합니다. 초대된 인원이 최대인 경우에 조회되지 않습니다.")
    @ApiErrorCodes(value = {
            ErrorStatus.NOT_SCHEDULE_OWNER,
            ErrorStatus.NOT_SCHEDULE_PARTICIPANT,
            ErrorStatus.NOT_FOUND_SCHEDULE_FAILURE,
            ErrorStatus.NOT_MEETING_SCHEDULE,
            ErrorStatus.INVALID_MEETING_PARTICIPANT_COUNT
    })
    ResponseDto<String> getGuestInvitationUrl(
            @Parameter(description = "모임 일정 ID") @PathVariable Long meetingScheduleId,
            @AuthenticationPrincipal SecurityUserDetails memberInfo
    );
}
