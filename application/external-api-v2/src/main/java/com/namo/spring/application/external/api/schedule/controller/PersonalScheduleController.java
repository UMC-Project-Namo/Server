package com.namo.spring.application.external.api.schedule.controller;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.namo.spring.application.external.api.schedule.api.PersonalScheduleApi;
import com.namo.spring.application.external.api.schedule.dto.PersonalScheduleRequest;
import com.namo.spring.application.external.api.schedule.dto.PersonalScheduleResponse;
import com.namo.spring.application.external.api.schedule.usecase.PersonalScheduleUsecase;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.core.common.response.ResponseDto;

import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
            @Valid @RequestBody PersonalScheduleRequest.PostPersonalScheduleDto dto,
            @AuthenticationPrincipal SecurityUserDetails member) throws JsonProcessingException {
        return ResponseDto.onSuccess(personalScheduleUsecase.createPersonalSchedule(dto, member));
    }

    /**
     * 내 캘린더 조회 API
     */
    @GetMapping("/calendar")
    public ResponseDto<List<PersonalScheduleResponse.GetMonthlyScheduleDto>> getMyMonthlySchedules(
            @RequestParam @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate endDate,
            @AuthenticationPrincipal SecurityUserDetails member) {
        return ResponseDto.onSuccess(personalScheduleUsecase.getMyMonthlySchedules(startDate, endDate, member));
    }

    /**
     * 내 월간 캘린더 -
     * 친구의 생일 목록 조회 API
     */
    @GetMapping("/calendar/friends/birthdays")
    public ResponseDto<List<PersonalScheduleResponse.GetMonthlyFriendBirthdayDto>> getMonthlyFriendsBirthday(
            @RequestParam @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate endDate,
            @AuthenticationPrincipal SecurityUserDetails member) {
        return ResponseDto.onSuccess(personalScheduleUsecase.getMonthlyFriendsBirthday(startDate, endDate, member));
    }

    /**
     * 친구 캘린더 조회 API
     */
    @GetMapping("/calendar/friends")
    public ResponseDto<List<PersonalScheduleResponse.GetFriendMonthlyScheduleDto>> getFriendMonthlySchedules(
            @RequestParam @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam Long memberId,
            @AuthenticationPrincipal SecurityUserDetails member) {
        return ResponseDto.onSuccess(personalScheduleUsecase.getFriendMonthlySchedules(startDate, endDate, memberId, member));
    }

    /**
     * 개인 일정 수정 API
     */
    @PatchMapping("/{scheduleId}")
    public ResponseDto<String> updatePersonalSchedules(@PathVariable Long scheduleId,
            @Valid @RequestBody PersonalScheduleRequest.PatchPersonalScheduleDto dto,
            @AuthenticationPrincipal SecurityUserDetails member) {
        personalScheduleUsecase.updatePersonalSchedule(dto, scheduleId, member);
        return ResponseDto.onSuccess("일정 수정 성공");
    }
}
