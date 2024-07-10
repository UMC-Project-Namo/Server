package com.namo.spring.application.external.api.individual.controller;

import com.namo.spring.application.external.api.individual.dto.ScheduleRequest;
import com.namo.spring.application.external.api.individual.dto.ScheduleResponse;
import com.namo.spring.application.external.api.individual.facade.ScheduleFacade;
import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCodes;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.application.external.global.utils.Converter;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.response.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "3. Schedule (개인)", description = "개인 일정 관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/schedules")
public class ScheduleController {
    private final ScheduleFacade scheduleFacade;
    private final Converter converter;

    @Operation(summary = "일정 생성", description = "일정 생성 API")
    @PostMapping("")
    @ApiErrorCodes({
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<ScheduleResponse.ScheduleIdDto> createSchedule(
            @Valid @RequestBody ScheduleRequest.PostScheduleDto postScheduleDto,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        ScheduleResponse.ScheduleIdDto scheduleIddto = scheduleFacade.createSchedule(
                postScheduleDto,
                user.getUserId()
        );
        return ResponseDto.onSuccess(scheduleIddto);
    }

    @Operation(summary = "일정 월별 조회", description = "개인 일정 & 모임 일정 월별 조회 API")
    @GetMapping("/{month}")
    @ApiErrorCodes({
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<List<ScheduleResponse.GetScheduleDto>> getSchedulesByUser(
            @Parameter(description = "조회 일자", example = "{년},{월}") @PathVariable("month") String month,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        List<LocalDateTime> localDateTimes = converter.convertLongToLocalDateTime(month);
        List<ScheduleResponse.GetScheduleDto> userSchedule = scheduleFacade.getSchedulesByUser(
                user.getUserId(), localDateTimes);
        return ResponseDto.onSuccess(userSchedule);
    }

    @Operation(summary = "모임 일정 월별 조회", description = "모임 일정 월별 조회 API")
    @GetMapping("/group/{month}")
    @ApiErrorCodes({
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<List<ScheduleResponse.GetScheduleDto>> getGroupSchedulesByUser(
            @Parameter(description = "조회 일자", example = "{년},{월}") @PathVariable("month") String month,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        List<LocalDateTime> localDateTimes = converter.convertLongToLocalDateTime(month);
        List<ScheduleResponse.GetScheduleDto> userSchedule = scheduleFacade.getGroupSchedulesByUser(
                user.getUserId(),
                localDateTimes
        );
        return ResponseDto.onSuccess(userSchedule);
    }

    @Operation(summary = "모든 일정 조회", description = "유저의 모든 개인 일정과 모임 일정 조회 API")
    @GetMapping("/all")
    @ApiErrorCodes({
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<List<ScheduleResponse.GetScheduleDto>> getAllSchedulesByUser(
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        List<ScheduleResponse.GetScheduleDto> userSchedule = scheduleFacade.getAllSchedulesByUser(
                user.getUserId()
        );
        return ResponseDto.onSuccess(userSchedule);
    }

    @Operation(summary = "모든 모임 일정 조회", description = "모든 모임 일정 조회 API")
    @GetMapping("/group/all")
    @ApiErrorCodes({
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<List<ScheduleResponse.GetScheduleDto>> getAllGroupSchedulesByUser(
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        List<ScheduleResponse.GetScheduleDto> groupSchedule = scheduleFacade.getAllGroupSchedulesByUser(
                user.getUserId()
        );
        return ResponseDto.onSuccess(groupSchedule);
    }

    @Operation(summary = "일정 수정", description = "일정 수정 API")
    @PatchMapping("/{scheduleId}")
    @ApiErrorCodes({
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<ScheduleResponse.ScheduleIdDto> modifyUserSchedule(
            @AuthenticationPrincipal SecurityUserDetails user,
            @Parameter(description = "일정 ID") @PathVariable("scheduleId") Long scheduleId,
            @RequestBody ScheduleRequest.PostScheduleDto req
    ) {
        ScheduleResponse.ScheduleIdDto dto = scheduleFacade.modifySchedule(
                scheduleId,
                req,
                user.getUserId()
        );
        return ResponseDto.onSuccess(dto);
    }

    /**
     * kind 0 은 개인 일정
     * kind 1 은 모임 일정
     */
    @Operation(summary = "일정 삭제", description = "개인 캘린더에서 개인 혹은 모임 일정 삭제 API")
    @DeleteMapping("/{scheduleId}/{kind}")
    @ApiErrorCodes({
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    public ResponseDto<String> deleteUserSchedule(
            @Parameter(description = "일정 ID") @PathVariable("scheduleId") Long scheduleId,
            @Parameter(description = "일정 타입", example = "0(개인 일정), 1(모임 일정)") @PathVariable("kind") Integer kind,
            @AuthenticationPrincipal SecurityUserDetails user
    ) {
        scheduleFacade.removeSchedule(scheduleId, kind, user.getUserId());
        return ResponseDto.onSuccess("삭제에 성공하였습니다.");
    }

}
