package com.namo.spring.application.external.api.schedule.controller;

import com.namo.spring.application.external.api.schedule.dto.MeetingScheduleResponse;
import com.namo.spring.application.external.api.schedule.usecase.MeetingScheduleUsecase;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.core.common.response.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "모임", description = "모임 일정 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/schedules/meeting")
public class MeetingScheduleController {
    private final MeetingScheduleUsecase meetingScheduleUsecase;

    /**
     * 모임 일정 목록 조회 API
     */
    @GetMapping("")
    public ResponseDto<List<MeetingScheduleResponse.GetMeetingScheduleDto>> findMeetings(@AuthenticationPrincipal SecurityUserDetails member) {
        return ResponseDto.onSuccess(meetingScheduleUsecase.getMeetingSchedules(member.getUserId()));
    }


}