package com.namo.spring.application.external.api.schedule.controller;

import com.namo.spring.application.external.api.schedule.api.PersonalScheduleApi;
import com.namo.spring.application.external.api.schedule.dto.PersonalScheduleRequest;
import com.namo.spring.application.external.api.schedule.dto.PersonalScheduleResponse;
import com.namo.spring.application.external.api.schedule.usecase.PersonalScheduleUsecase;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.core.common.response.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.namo.spring.application.external.global.utils.PeriodValidationUtils.validatePeriod;

@Tag(name = "4. 개인 일정", description = "개인 일정 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/schedules")
public class PersonalScheduleController implements PersonalScheduleApi {
    private final PersonalScheduleUsecase personalScheduleUsecase;

    /**
     * 일정 생성 API
     */
    @PostMapping(value = "")
    public ResponseDto<Long> createPersonalSchedule(
            @Valid @RequestBody PersonalScheduleRequest.PostPersonalScheduleDto request,
            @AuthenticationPrincipal SecurityUserDetails member){
        return ResponseDto.onSuccess(personalScheduleUsecase.createPersonalSchedule(request, member));
    }

    /**
     * 내 캘린더 조회 API
     */
    @GetMapping("/calendar")
    public ResponseDto<List<PersonalScheduleResponse.GetMonthlyScheduleDto>> getMyMonthlySchedules(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @AuthenticationPrincipal SecurityUserDetails member) {
        validatePeriod(startDate, endDate);
        return ResponseDto.onSuccess(personalScheduleUsecase.getMyMonthlySchedules(startDate, endDate, member));
    }

    /**
     * 내 월간 캘린더 -
     * 친구의 생일 목록 조회 API
     */
    @GetMapping("/calendar/friends/birthdays")
    public ResponseDto<List<PersonalScheduleResponse.GetMonthlyFriendBirthdayDto>> getMonthlyFriendsBirthday(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @AuthenticationPrincipal SecurityUserDetails member) {
        validatePeriod(startDate, endDate);
        return ResponseDto.onSuccess(personalScheduleUsecase.getMonthlyFriendsBirthday(startDate, endDate, member.getUserId()));
    }

    /**
     * 친구 캘린더 조회 API
     */
    @GetMapping("/calendar/friends")
    public ResponseDto<List<PersonalScheduleResponse.GetFriendMonthlyScheduleDto>> getFriendMonthlySchedules(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam Long memberId,
            @AuthenticationPrincipal SecurityUserDetails member) {
        validatePeriod(startDate, endDate);
        return ResponseDto.onSuccess(personalScheduleUsecase.getFriendMonthlySchedules(startDate, endDate, memberId, member));
    }

    /**
     * 개인 일정 수정 API
     */
    @PatchMapping("/{scheduleId}")
    public ResponseDto<String> updatePersonalSchedules(@PathVariable Long scheduleId,
            @Valid @RequestBody PersonalScheduleRequest.PatchPersonalScheduleDto request,
            @AuthenticationPrincipal SecurityUserDetails member) {
        personalScheduleUsecase.updatePersonalSchedule(request, scheduleId, member);
        return ResponseDto.onSuccess("일정 수정 성공");
    }

    /**
     * 일정 삭제 API
     */
    @DeleteMapping("/{scheduleId}")
    public ResponseDto<String> deleteSchedule(@PathVariable Long scheduleId,
                                              @AuthenticationPrincipal SecurityUserDetails member) {
        personalScheduleUsecase.deleteSchedule(scheduleId, member);
        return ResponseDto.onSuccess("일정 삭제 성공");
    }
}
