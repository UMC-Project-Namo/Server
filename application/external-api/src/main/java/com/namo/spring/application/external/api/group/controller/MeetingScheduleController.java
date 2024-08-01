package com.namo.spring.application.external.api.group.controller;

import com.namo.spring.application.external.api.group.api.MeetingScheduleApi;
import com.namo.spring.application.external.api.group.dto.GroupScheduleRequest;
import com.namo.spring.application.external.api.group.dto.GroupScheduleResponse;
import com.namo.spring.application.external.api.group.dto.MeetingScheduleRequest;
import com.namo.spring.application.external.api.group.facade.MeetingScheduleFacade;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.application.external.global.utils.Converter;
import com.namo.spring.core.common.response.ResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "7. Schedule (모임)", description = "모임 일정 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/group/schedules")
public class MeetingScheduleController implements MeetingScheduleApi {
    private final MeetingScheduleFacade meetingScheduleFacade;
    private final Converter converter;

    /**
     * 모임 일정 생성
     */
    @PostMapping("")
    public ResponseDto<Long> createMeetingSchedule(
            @AuthenticationPrincipal SecurityUserDetails user,
            @Valid @RequestBody MeetingScheduleRequest.PostMeetingScheduleDto scheduleReq
    ) {
        Long scheduleId = meetingScheduleFacade.createSchedule(user.getUserId(), scheduleReq);
        return ResponseDto.onSuccess(scheduleId);
    }

    /**
     * 모임 일정 수정
     */
    @PatchMapping("")
    public ResponseDto<Long> modifyGroupSchedule(
            @Valid @RequestBody GroupScheduleRequest.PatchGroupScheduleDto scheduleReq
    ) {
        meetingScheduleFacade.modifyGroupSchedule(scheduleReq);
        return ResponseDto.onSuccess(null);
    }

    /**
     * 모임 일정 카테고리 수정
     */
    @PatchMapping("/category")
    public ResponseDto<Long> modifyGroupScheduleCategory(
            @Valid @RequestBody GroupScheduleRequest.PatchGroupScheduleCategoryDto scheduleReq,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        meetingScheduleFacade.modifyGroupScheduleCategory(scheduleReq, user.getUserId());
        return ResponseDto.onSuccess(null);
    }

    /**
     * 모임 일정 삭제
     */
    @DeleteMapping("/{moimScheduleId}")
    public ResponseDto<Long> removeMeetingSchedule(
            @PathVariable(name = "moimScheduleId") Long moimScheduleId,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        meetingScheduleFacade.removeMeetingSchedule(moimScheduleId, user.getUserId());
        return ResponseDto.onSuccess(null);
    }

    /**
     * 월간 모임 일정 조회
     */
    @GetMapping("/{groupId}/{month}")
    public ResponseDto<List<GroupScheduleResponse.GetMonthlyGroupScheduleDto>> getMonthGroupSchedules(
            Long groupId,
            @PathVariable(name = "month") String month,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        List<LocalDateTime> localDateTimes = converter.convertLongToLocalDateTime(month);
        List<GroupScheduleResponse.GetMonthlyGroupScheduleDto> schedules = meetingScheduleFacade.getMonthGroupSchedules(groupId,
                localDateTimes, user.getUserId());
        return ResponseDto.onSuccess(schedules);
    }

    /**
     * 모든 모임 일정 조회
     */
    @GetMapping("/{groupId}/all")
    public ResponseDto<List<GroupScheduleResponse.GetAllGroupScheduleDto>> getAllGroupSchedules(
            @PathVariable(name = "groupId") Long groupId,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        List<GroupScheduleResponse.GetAllGroupScheduleDto> schedules
                = meetingScheduleFacade.getAllGroupSchedules(groupId, user.getUserId());
        return ResponseDto.onSuccess(schedules);
    }

    /**
     * 모임 일정 알림 생성
     */
    @PostMapping("/alarm")
    public ResponseDto<Void> createGroupScheduleAlarm(
            @Valid @RequestBody GroupScheduleRequest.PostGroupScheduleAlarmDto postGroupScheduleAlarmDto,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        meetingScheduleFacade.createGroupScheduleAlarm(postGroupScheduleAlarmDto, user.getUserId());
        return ResponseDto.onSuccess(null);
    }

    /**
     * 모임 일정 알림 수정
     */
    @PatchMapping("/alarm")
    public ResponseDto<Void> modifyGroupScheduleAlarm(
            @Valid @RequestBody GroupScheduleRequest.PostGroupScheduleAlarmDto postGroupScheduleAlarmDto,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        meetingScheduleFacade.modifyGroupScheduleAlarm(postGroupScheduleAlarmDto, user.getUserId());
        return ResponseDto.onSuccess(null);
    }
}
