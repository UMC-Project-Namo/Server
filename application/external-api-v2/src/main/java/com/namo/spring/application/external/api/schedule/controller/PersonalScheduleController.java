package com.namo.spring.application.external.api.schedule.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.namo.spring.application.external.api.schedule.api.PersonalScheduleApi;
import com.namo.spring.application.external.api.schedule.dto.PersonalScheduleResponse;
import com.namo.spring.application.external.api.schedule.dto.ScheduleRequest;
import com.namo.spring.application.external.api.schedule.usecase.PersonalScheduleUsecase;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.core.common.response.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "개인 일정", description = "개인 일정 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/schedules")
public class PersonalScheduleController implements PersonalScheduleApi {
    private final PersonalScheduleUsecase personalScheduleUsecase;

    /**
     * 일정 생성 API
     */
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDto<Long> createPersonalSchedule(
            @Valid @RequestBody ScheduleRequest.PostPersonalScheduleDto dto,
            @AuthenticationPrincipal SecurityUserDetails member) throws JsonProcessingException {
        return ResponseDto.onSuccess(personalScheduleUsecase.createPersonalSchedule(dto, member));
    }

    /**
     * 내 월간 일정 조회 API
     */
    @GetMapping("/calendar")
    public ResponseDto<List<PersonalScheduleResponse.GetMonthlyScheduleDto>> getMyMonthlySchedules(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @AuthenticationPrincipal SecurityUserDetails member) {
        return ResponseDto.onSuccess(personalScheduleUsecase.getMyMonthlySchedules(year, month, member));
    }

    /**
     * 친구 월간 일정 조회 API
     */
    @GetMapping("/calendar/friends")
    public ResponseDto<List<PersonalScheduleResponse.GetFriendMonthlyScheduleDto>> getFriendMonthlySchedules(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam Long memberId,
            @AuthenticationPrincipal SecurityUserDetails member) {
        return ResponseDto.onSuccess(personalScheduleUsecase.getFriendMonthlySchedules(year, month, memberId, member));
    }

    /**
     * 개인 일정 수정 API
     */
    @PatchMapping("/{scheduleId}")
    public ResponseDto<String> updatePersonalSchedules(@PathVariable Long scheduleId,
                                                       @Valid @RequestBody ScheduleRequest.PatchPersonalScheduleDto dto,
                                                       @AuthenticationPrincipal SecurityUserDetails member) {
        personalScheduleUsecase.updatePersonalSchedule(dto, scheduleId, member);
        return ResponseDto.onSuccess("일정 수정 성공");
    }
}
