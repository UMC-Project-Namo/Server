package com.namo.spring.application.external.api.record.Controller;

import static com.namo.spring.core.common.code.status.ErrorStatus.*;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.namo.spring.application.external.api.record.dto.ActivityRequest;
import com.namo.spring.application.external.api.record.dto.ActivityResponse;
import com.namo.spring.application.external.api.record.usecase.ActivityUseCase;
import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCodes;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
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
            NOT_FOUND_SCHEDULE_FAILURE,
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
    @ApiErrorCodes(value = {
            NOT_FOUND_GROUP_ACTIVITY_FAILURE,
            NOT_PARTICIPATING_ACTIVITY
    })
    @GetMapping("/settlement/{activityId}")
    public ResponseDto<ActivityResponse.ActivitySettlementInfoDto> getActivitySettlement(
            @AuthenticationPrincipal SecurityUserDetails memberInfo,
            @Parameter(description = "정산할 활동(activity) ID 입니다.", example = "1")
            @PathVariable Long activityId) {
        return ResponseDto.onSuccess(activityUseCase.getSettlement(memberInfo.getUserId(), activityId));
    }

    @Operation(summary = "모임 기록 활동 생성", description = "모임 활동을 생성합니다.(스케줄 초대에 수락한 유저만 활동에 추가할 수 있습니다)")
    @ApiErrorCodes(value = {
            NOT_FOUND_SCHEDULE_FAILURE,
            NOT_SCHEDULE_PARTICIPANT_OR_NOT_ACTIVE
    })
    @PostMapping("/{scheduleId}")
    public ResponseDto<String> createActivity(
            @AuthenticationPrincipal SecurityUserDetails memberInfo,
            @Parameter(description = "활동을 생성할 스케줄 ID 입니다.", example = "1")
            @PathVariable Long scheduleId,
            @Valid @RequestBody ActivityRequest.CreateActivityDto request
    ){
        activityUseCase.createActivity(memberInfo.getUserId(), scheduleId, request);
        return ResponseDto.onSuccess("활동 생성 완료");
    }

    @Operation(summary = "모임 활동 수정", description = "모임 활동을 수정합니다. "
            + "활동(제목, 시작-종료시간, 장소, 이미지)를 수정하는 API 입니다."
            + "(모임 참가인원은 누구나 수정할 수 있습니다.) ")
    @ApiErrorCodes(value = {
            NOT_FOUND_GROUP_ACTIVITY_FAILURE,
            NOT_PARTICIPATING_ACTIVITY
    })
    @PatchMapping("/{activityId}")
    public ResponseDto<String> updateActivity(
            @AuthenticationPrincipal SecurityUserDetails memberInfo,
            @Parameter(description = "수정 할 활동 ID 입니다.", example = "1")
            @PathVariable Long activityId,
            @RequestBody ActivityRequest.UpdateActivityDto request
    ){
        activityUseCase.updateActivity(memberInfo.getUserId(), activityId, request);
        return ResponseDto.onSuccess("활동 수정 완료");
    }

    @Operation(summary = "모임 활동 참여자 수정", description = "활동의 참여자를 수정하는 API 입니다. "
            + "(정산 참여자가 삭제될 수 없습니다)")
    @ApiErrorCodes(value = {
            NOT_FOUND_GROUP_ACTIVITY_FAILURE,
            NOT_PARTICIPATING_ACTIVITY,
            IN_SETTLEMENT_ACTIVITY_MEMBER
    })
    @PatchMapping("/{activityId}/participants")
    public ResponseDto<String> updateActivityParticipants(
            @AuthenticationPrincipal SecurityUserDetails memberInfo,
            @Parameter(description = "활동 참여자 정보를 수정 할 활동 ID 입니다.", example = "1")
            @PathVariable Long activityId,
            @RequestBody ActivityRequest.UpdateActivityParticipantsDto request
    ){
        activityUseCase.updateActivityParticipants(memberInfo.getUserId(), activityId, request);
        return ResponseDto.onSuccess("활동 참여자 수정 완료");
    }

    @Operation(summary = "모임 활동 태그 수정", description = "활동의 태그를 수정하는 API 입니다.")
    @PatchMapping("/{activityId}/tag")
    @ApiErrorCodes(value = {
            NOT_FOUND_GROUP_ACTIVITY_FAILURE,
            NOT_PARTICIPATING_ACTIVITY,
    })
    public ResponseDto<String> updateActivityTag(
            @AuthenticationPrincipal SecurityUserDetails memberInfo,
            @Parameter(description = "활동 태그 정보를 수정 할 활동 ID 입니다.", example = "1")
            @PathVariable Long activityId,
            @Parameter(description = "수정 할 활동 Tag 정보 입니다.", example = "동아리")
            @RequestParam String tag
    ){
        activityUseCase.updateActivityTag(memberInfo.getUserId(), activityId, tag);
        return ResponseDto.onSuccess("활동 태그 수정 완료");
    }

    @Operation(summary = "모임 기록 활동 삭제", description = "모임 활동을 삭제합니다. "
            + "원본-리사이징 이미지 모두 즉시 삭제됩니다. "
            + "(모임 참가인원은 삭제할 수 있습니다) ")
    @ApiErrorCodes(value = {
            NOT_FOUND_GROUP_ACTIVITY_FAILURE,
            NOT_PARTICIPATING_ACTIVITY
    })
    @DeleteMapping("/{activityId}")
    public ResponseDto<String> deleteActivity(
            @AuthenticationPrincipal SecurityUserDetails memberInfo,
            @Parameter(description = "삭제 할 활동 ID 입니다.", example = "1")
            @PathVariable Long activityId
    ){
        activityUseCase.deleteActivity(memberInfo.getUserId(), activityId);
        return ResponseDto.onSuccess("활동 삭제 완료");
    }
}
