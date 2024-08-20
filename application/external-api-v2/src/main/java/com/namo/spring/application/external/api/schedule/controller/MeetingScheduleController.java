package com.namo.spring.application.external.api.schedule.controller;

import com.namo.spring.application.external.api.schedule.api.MeetingScheduleApi;
import com.namo.spring.application.external.api.schedule.dto.MeetingScheduleResponse;
import com.namo.spring.application.external.api.schedule.dto.ScheduleRequest;
import com.namo.spring.application.external.api.schedule.usecase.MeetingScheduleUsecase;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.core.common.response.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDto<Long> createMeetingSchedule(
            @Valid @RequestPart ScheduleRequest.PostMeetingScheduleDto dto,
            @RequestPart(required = false) MultipartFile image,
            @AuthenticationPrincipal SecurityUserDetails member) {
        return ResponseDto.onSuccess(meetingScheduleUsecase.createMeetingSchedule(dto, image, member.getUserId()));
    }

    /**
     * 모임 일정 목록 조회 API
     */
    @GetMapping("")
    public ResponseDto<List<MeetingScheduleResponse.GetMeetingScheduleItemDto>> findMyMeetingSchedules(@AuthenticationPrincipal SecurityUserDetails member) {
        return ResponseDto.onSuccess(meetingScheduleUsecase.getMeetingSchedules(member.getUserId()));
    }

    /**
     * 모임 생성 전/ 참여자 일정 조회 API
     */
    @PostMapping(path = "/preview")
    public ResponseDto<List<MeetingScheduleResponse.GetMonthlyParticipantScheduleDto>> findMonthlyParticipantSchedules(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam List<Long> participantIds,
            @AuthenticationPrincipal SecurityUserDetails member) {
        return ResponseDto.onSuccess(meetingScheduleUsecase.getMeetingParticiantsSchedules(participantIds, year, month, member.getUserId()));
    }

}
