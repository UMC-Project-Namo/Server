package com.namo.spring.application.external.api.schedule.controller;

import java.util.List;

import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCodes;
import com.namo.spring.core.common.code.status.ErrorStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.namo.spring.application.external.api.schedule.api.MeetingScheduleApi;
import com.namo.spring.application.external.api.schedule.dto.MeetingScheduleRequest;
import com.namo.spring.application.external.api.schedule.dto.MeetingScheduleResponse;
import com.namo.spring.application.external.api.schedule.usecase.MeetingScheduleUsecase;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.core.common.response.ResponseDto;

import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "모임 일정", description = "모임 일정 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/schedules/meeting")
public class MeetingScheduleController implements MeetingScheduleApi {
    private final MeetingScheduleUsecase meetingScheduleUsecase;

    /**
     * 모임 일정 생성 API
     */
    @PostMapping(path = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDto<Long> createMeetingSchedule(
            @Valid @RequestPart MeetingScheduleRequest.PostMeetingScheduleDto dto,
            @AuthenticationPrincipal SecurityUserDetails memberInfo) {
        return ResponseDto.onSuccess(meetingScheduleUsecase.createMeetingSchedule(dto, memberInfo));
    }

    @Operation(summary = "모임 일정 목록 조회", description = "모임 일정 목록을 조회합니다.")
    @GetMapping("")
    public ResponseDto<List<MeetingScheduleResponse.GetMeetingScheduleSummaryDto>> getMyMeetingSchedules(
            @AuthenticationPrincipal SecurityUserDetails memberInfo) {
        return ResponseDto.onSuccess(meetingScheduleUsecase.getMeetingSchedules(memberInfo));
    }

    @Operation(summary = "모임 초대자 월간 일정 조회", description = "모임에 초대할 유저들의 월간 일정을 조회합니다.")
    @GetMapping(path = "/preview")
    public ResponseDto<List<MeetingScheduleResponse.GetMonthlyMembersScheduleDto>> getMonthlyParticipantSchedules(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam List<Long> participantIds,
            @AuthenticationPrincipal SecurityUserDetails memberInfo) {
        return ResponseDto.onSuccess(
                meetingScheduleUsecase.getMonthlyMemberSchedules(participantIds, year, month, memberInfo));
    }

    @Operation(summary = "모임 월간 일정 조회", description = "모임에 있는 유저들의 월간 일정을 조회합니다.")
    @GetMapping(path = "/{meetingScheduleId}/calender")
    public ResponseDto<List<MeetingScheduleResponse.GetMonthlyMeetingParticipantScheduleDto>> getMonthlyMeetingParticipantSchedules(
            @PathVariable Long meetingScheduleId,
            @RequestParam Integer year,
            @RequestParam Integer month,
            @AuthenticationPrincipal SecurityUserDetails memberInfo) {
        return ResponseDto.onSuccess(
                meetingScheduleUsecase.getMonthlyMeetingParticipantSchedules(meetingScheduleId, year, month,
                        memberInfo));
    }

    @Operation(summary = "모임 일정 상세 조회", description = "모임 일정을 상세 조회합니다.")
    @GetMapping(path = "/{meetingScheduleId}")
    public ResponseDto<MeetingScheduleResponse.GetMeetingScheduleDto> getMeetingSchedule(
            @PathVariable Long meetingScheduleId,
            @AuthenticationPrincipal SecurityUserDetails memberInfo) {
        return ResponseDto.onSuccess(meetingScheduleUsecase.getMeetingSchedule(meetingScheduleId, memberInfo));
    }

    @Operation(summary = "모임 일정 수정", description = "모임 일정을 수정합니다. 수정 권한은 모임의 방장에게만 있습니다.")
    @PatchMapping(path = "/{meetingScheduleId}")
    public ResponseDto<String> updateMeetingSchedule(
            @PathVariable Long meetingScheduleId,
            @RequestBody @Valid MeetingScheduleRequest.PatchMeetingScheduleDto dto,
            @AuthenticationPrincipal SecurityUserDetails memberInfo) {
        meetingScheduleUsecase.updateMeetingSchedule(dto, meetingScheduleId, memberInfo);
        return ResponseDto.onSuccess("모임 일정 수정 성공");
    }

    @Operation(summary = "모임 일정 프로필 수정", description = "모임 일정의 이미지와 제목을 커스텀합니다.")
    @PatchMapping(path = "/{meetingScheduleId}/profile")
    public ResponseDto<String> updateMeetingScheduleProfile(
            @PathVariable Long meetingScheduleId,
            @RequestBody @Valid MeetingScheduleRequest.PatchMeetingScheduleProfileDto dto,
            @AuthenticationPrincipal SecurityUserDetails memberInfo) {
        meetingScheduleUsecase.updateMeetingScheduleProfile(dto, meetingScheduleId, memberInfo);
        return ResponseDto.onSuccess("모임 일정 프로필 수정 성공");
    }

    @Operation(summary = "게스트 초대용 링크 조회 API", description = "게스트 초대용 링크를 조회합니다. 초대 인원이 최대인 경우에 조회되지 않습니다.")
    @GetMapping(path = "/{meetingScheduleId}/invitations")
    @ApiErrorCodes(value = {
            ErrorStatus.NOT_SCHEDULE_OWNER,
            ErrorStatus.NOT_SCHEDULE_PARTICIPANT,
            ErrorStatus.NOT_FOUND_SCHEDULE_FAILURE,
            ErrorStatus.NOT_MEETING_SCHEDULE,
            ErrorStatus.INVALID_MEETING_PARTICIPANT_COUNT
    })
    public ResponseDto<String> getGuestInviteCode(
            @Parameter(description = "모임 일정 ID") @PathVariable Long meetingScheduleId,
            @AuthenticationPrincipal SecurityUserDetails memberInfo
    ) {
        return ResponseDto.onSuccess(meetingScheduleUsecase.getGuestInviteCode(meetingScheduleId, memberInfo));
    }
}