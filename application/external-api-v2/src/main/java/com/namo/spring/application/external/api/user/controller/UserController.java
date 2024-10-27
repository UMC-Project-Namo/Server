package com.namo.spring.application.external.api.user.controller;

import com.namo.spring.application.external.api.user.dto.NotificationRequest;
import com.namo.spring.application.external.api.user.dto.NotificationResponse;
import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCodes;
import com.namo.spring.db.mysql.domains.notification.type.ReceiverDeviceType;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.namo.spring.application.external.api.user.usecase.UserUseCase;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.core.common.response.ResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import static com.namo.spring.application.external.global.utils.DeviceTypeValidationUtil.validatedDeviceType;
import static com.namo.spring.core.common.code.status.ErrorStatus.*;


@Tag(name = "1. 유저 정보 및 설정", description = "유저 정보 및 설정 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/users")
public class UserController {

    private final UserUseCase userUseCase;

    @Operation(summary = "선호 색상 수정", description = "회원가입 완료 이후 나의 선호 색상을 수정합니다.")
    @PostMapping(value = "/edit/color")
    public ResponseDto<String> updatePreferenceColor(
            @AuthenticationPrincipal SecurityUserDetails memberInfo,
            @RequestParam Long colorId
    ){
        userUseCase.updatePreferenceColor(memberInfo.getUserId(), colorId);
        return ResponseDto.onSuccess("선호 색상 수정 완료");
    }

    @Operation(summary = "기기 정보 등록", description = "기기 정보 등록 및 푸시 알림 설정을 활성화합니다.")
    @ApiErrorCodes(value = {INVALID_DEVICE_TYPE, NOT_FOUND_ACTIVE_USER_FAILURE})
    @PostMapping(value = "/settings/notifications")
    public ResponseDto<String> createInitialNotificationSetting(
            @AuthenticationPrincipal SecurityUserDetails memberInfo,
            @RequestBody NotificationRequest.CreateDeviceInfoDto request
            ){
        ReceiverDeviceType deviceType = validatedDeviceType(request.getDeviceType());
        userUseCase.createDeviceInfoAndNotificationEnabled(request, deviceType, memberInfo.getUserId());
        return ResponseDto.onSuccess("기기 정보 등록 및 푸시 알림 활성화 완료");
    }

    @Operation(summary = "푸시 알림 설정 정보 조회", description = "해당 기기에 대한 푸시 알림 설정 정보를 조회합니다")
    @ApiErrorCodes(value = {NOT_FOUND_MOBILE_DEVICE_FAILURE, NOT_FOUND_ACTIVE_USER_FAILURE})
    @GetMapping(value = "/settings/notifications")
    public ResponseDto<NotificationResponse.GetNotificationSettingInfoDto> getNotificationSettingInfo(
            @AuthenticationPrincipal SecurityUserDetails memberInfo,
            @Parameter(description = "기기 타입") @RequestParam ReceiverDeviceType deviceType,
            @Parameter(description = "기기 토큰") @RequestParam String deviceToken
    ){
        return ResponseDto.onSuccess(userUseCase.getNotificationSettingInfo(deviceType, deviceToken, memberInfo.getUserId()));
    }

    @Operation(summary = "푸시 알림 설정", description = "푸시 알림 설정을 활성/ 비활성화합니다.")
    @ApiErrorCodes(value = {NOT_FOUND_ACTIVE_USER_FAILURE, NOT_FOUND_MOBILE_DEVICE_FAILURE})
    @PatchMapping(value = "/settings/notifications")
    public ResponseDto<String> updateNotificationEnabled(
            @AuthenticationPrincipal SecurityUserDetails memberInfo,
            @Parameter(description = "활성화 여부")@RequestParam boolean request){
        userUseCase.updateNotificationEnabled(request,  memberInfo.getUserId());
        return ResponseDto.onSuccess(request ? "푸시 알림이 활성화되었습니다." : "푸시 알림이 비활성화되었습니다.");
    }
}
