package com.namo.spring.application.external.api.group.controller;

import com.namo.spring.application.external.api.group.api.TempMeetingScheduleApi;
import com.namo.spring.application.external.api.group.dto.MeetingScheduleRequest;
import com.namo.spring.application.external.api.group.dto.MeetingScheduleResponse;
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

@Tag(name = "7. Schedule (모임) - 네임 규칙 적용", description = "모임 일정 관련 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/temp/group/schedules")
public class TempMeetingScheduleController implements TempMeetingScheduleApi {
    private final MeetingScheduleFacade meetingScheduleFacade;
    private final Converter converter;

    /**
     * 네임 규칙 적용 - 모임 일정 수정
     */
    @PatchMapping("")
    public ResponseDto<Long> modifyMeetingSchedule(
            @Valid @RequestBody MeetingScheduleRequest.PatchMeetingScheduleDto scheduleReq
    ) {
        meetingScheduleFacade.modifyMeetingSchedule(scheduleReq);
        return ResponseDto.onSuccess(null);
    }

    /**
     * 네임 규칙 적용 - 모임 일정 카테고리 수정
     */
    @PatchMapping("/category")
    public ResponseDto<Long> modifyMeetingScheduleCategory(
            @Valid @RequestBody MeetingScheduleRequest.PatchMeetingScheduleCategoryDto scheduleReq,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        meetingScheduleFacade.modifyMeetingScheduleCategory(scheduleReq, user.getUserId());
        return ResponseDto.onSuccess(null);
    }

    /**
     * 네임 규칙 적용 - 월간 모임 일정 조회
     */
    @GetMapping("/{groupId}/{month}")
    public ResponseDto<List<MeetingScheduleResponse.GetMonthlyMeetingScheduleDto>> getMonthlyMeetingSchedules(
            @PathVariable(name = "groupId") Long groupId,
            @PathVariable(name = "month") String month,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        List<LocalDateTime> localDateTimes = converter.convertLongToLocalDateTime(month);
        List<MeetingScheduleResponse.GetMonthlyMeetingScheduleDto> schedules = meetingScheduleFacade.getMonthlyMeetingSchedules(
                groupId,
                localDateTimes, user.getUserId());
        return ResponseDto.onSuccess(schedules);
    }

    /**
     * 네임 규칙 적용 - 모든 모임 일정 조회
     */
    @GetMapping("/{groupId}/all")
    public ResponseDto<List<MeetingScheduleResponse.GetAllMeetingScheduleDto>> getAllMeetingSchedules(
            @PathVariable(name = "groupId") Long groupId,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        List<MeetingScheduleResponse.GetAllMeetingScheduleDto> schedules
                = meetingScheduleFacade.getAllMeetingSchedules(groupId, user.getUserId());
        return ResponseDto.onSuccess(schedules);
    }

    /**
     * 네임 규칙 적용 - 모임 일정 삭제
     */
    @DeleteMapping("/{meetingScheduleId}")
    public ResponseDto<Long> removeMeetingSchedule(
            @PathVariable(name = "meetingScheduleId") Long meetingScheduleId,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        meetingScheduleFacade.removeMeetingSchedule(meetingScheduleId, user.getUserId());
        return ResponseDto.onSuccess(null);
    }

    /**
     * 네임 규칙 적용 - 모임 일정 알림 생성
     */
    @PostMapping("/alarm")
    public ResponseDto<Void> createMeetingScheduleAlarm(
            @Valid @RequestBody MeetingScheduleRequest.PostMeetingScheduleAlarmDto postMeetingScheduleAlarmDto,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        meetingScheduleFacade.createMeetingScheduleAlarm(postMeetingScheduleAlarmDto, user.getUserId());
        return ResponseDto.onSuccess(null);
    }

    /**
     * 네임 규칙 적용 - 모임 일정 알림 수정
     */
    @PatchMapping("/alarm")
    public ResponseDto<Void> modifyMeetingScheduleAlarm(
            @Valid @RequestBody MeetingScheduleRequest.PatchMeetingScheduleAlarmDto patchMeetingScheduleAlarmDto,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        meetingScheduleFacade.modifyMeetingScheduleAlarm(patchMeetingScheduleAlarmDto, user.getUserId());
        return ResponseDto.onSuccess(null);
    }
}
