package com.namo.spring.application.external.api.schedule.controller;

import java.util.List;

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
import org.springframework.web.multipart.MultipartFile;

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
            @RequestPart(required = false) MultipartFile image,
            @AuthenticationPrincipal SecurityUserDetails memberInfo) {
        return ResponseDto.onSuccess(meetingScheduleUsecase.createMeetingSchedule(dto, image, memberInfo));
    }

    /**
     * 모임 일정 목록 조회 API
     */
    @GetMapping("")
    public ResponseDto<List<MeetingScheduleResponse.GetMeetingScheduleSummaryDto>> getMyMeetingSchedules(
            @AuthenticationPrincipal SecurityUserDetails memberInfo) {
        return ResponseDto.onSuccess(meetingScheduleUsecase.getMeetingSchedules(memberInfo));
    }

    /**
     * 모임 생성 전/ 초대자 월간 일정 조회 API
     */
    @GetMapping(path = "/preview")
    public ResponseDto<List<MeetingScheduleResponse.GetMonthlyMembersScheduleDto>> getMonthlyParticipantSchedules(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam List<Long> participantIds,
            @AuthenticationPrincipal SecurityUserDetails memberInfo) {
        return ResponseDto.onSuccess(
                meetingScheduleUsecase.getMonthlyMemberSchedules(participantIds, year, month, memberInfo));
    }

    /**
     * 모임 생성 후/ 참여자 월간 일정 조회 API
     */
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

    /**
     * 모임 일정 상세보기 API
     */
    @GetMapping(path = "/{meetingScheduleId}")
    public ResponseDto<MeetingScheduleResponse.GetMeetingScheduleDto> getMeetingSchedule(
            @PathVariable Long meetingScheduleId,
            @AuthenticationPrincipal SecurityUserDetails memberInfo) {
        return ResponseDto.onSuccess(meetingScheduleUsecase.getMeetingSchedule(meetingScheduleId, memberInfo));
    }

    /**
     * 모임 일정 수정 API
     */
    @PatchMapping(path = "/{meetingScheduleId}")
    public ResponseDto<String> updateMeetingSchedule(
            @PathVariable Long meetingScheduleId,
            @RequestBody @Valid MeetingScheduleRequest.PatchMeetingScheduleDto dto,
            @AuthenticationPrincipal SecurityUserDetails memberInfo) {
        meetingScheduleUsecase.updateMeetingSchedule(dto, meetingScheduleId, memberInfo);
        return ResponseDto.onSuccess("모임 일정 수정 성공");
    }

    /**
     * 게스트 초대용 링크 조회 API
     */
    @Operation(summary = "게스트 초대용 링크 조회 API", description = "게스트 초대용 링크를 조회합니다. 초대 인원이 최대인 경우에 조회되지 않습니다.")
    @PostMapping(path = "/{meetingScheduleId}/invitations")
    public ResponseDto<String> getGuestInviteCode(
            @Parameter(description = "모임 일정 ID") @PathVariable Long meetingScheduleId,
            @AuthenticationPrincipal SecurityUserDetails memberInfo
    ) {
        return ResponseDto.onSuccess(meetingScheduleUsecase.getGuestInviteCode(meetingScheduleId, memberInfo));
    }

}
