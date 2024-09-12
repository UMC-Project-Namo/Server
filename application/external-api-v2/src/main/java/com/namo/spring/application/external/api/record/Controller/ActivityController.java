package com.namo.spring.application.external.api.record.Controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.namo.spring.application.external.api.record.dto.ActivityResponse;
import com.namo.spring.application.external.api.record.usecase.ActivityUseCase;
import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCodes;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.response.ResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "활동", description = "활동 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/activities")
public class ActivityController {

    private final ActivityUseCase activityUseCase;

    @Operation(summary = "모임 기록 - 활동 조회", description = "모임 스케줄에 대한 활동 목록이 조회됩니다.")
    @ApiErrorCodes(value = {
        ErrorStatus.NOT_FOUND_SCHEDULE_FAILURE,
    })
    @GetMapping("/{scheduleId}")
    public ResponseDto<List<ActivityResponse.ActivityInfoDto>> getActivity(
        @AuthenticationPrincipal SecurityUserDetails memberInfo,
        @Parameter(description = "활동을 조회할 스케줄 ID 입니다..", example = "1")
        @PathVariable Long scheduleId) {

        return ResponseDto.onSuccess(activityUseCase
            .getActivities(memberInfo.getUserId(), scheduleId));
    }

    @Operation(summary = "모임 기록 활동에 대한 정산 팝업 조회", description = "모임 활동의 정산 내역을 조회합니다. (팝업 내용에 해당합니다)")
    @GetMapping("/settlement/{activityId}")
    public ResponseDto<ActivityResponse.ActivitySettlementInfoDto> getActivitySettlement(
        @AuthenticationPrincipal SecurityUserDetails memberInfo,
        @Parameter(description = "정산할 활동(activity) ID 입니다.", example = "1")
        @PathVariable Long activityId) {
        return ResponseDto.onSuccess(activityUseCase.getSettlement(memberInfo.getUserId(), activityId));
    }
}
