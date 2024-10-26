package com.namo.spring.application.external.api.user.controller;

import com.namo.spring.application.external.api.user.dto.NotificationRequest;
import com.namo.spring.application.external.api.user.dto.NotificationResponse;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.notification.exception.NotificationException;
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
    @PostMapping(value = "/settings/notifications")
    public ResponseDto<String> createInitialNotificationSetting(
            @AuthenticationPrincipal SecurityUserDetails memberInfo,
            @RequestBody NotificationRequest.CreateDeviceInfoDto request
            ){
        ReceiverDeviceType deviceType =  ReceiverDeviceType.fromString(request.getReceiverDeviceType());
        if (deviceType==null){
            throw new NotificationException(ErrorStatus.INVALID_DEVICE_TYPE);
        };
        userUseCase.createDeviceInfoAndNotificationEnabled(request, deviceType, memberInfo.getUserId());
        return ResponseDto.onSuccess("기기 정보 등록 및 푸시 알림 활성화 완료");
    }

    @Operation(summary = "푸시 알림 설정 정보 조회", description = "푸시 알림 설정 정보를 조회합니다")
    @GetMapping(value = "/settings/notifications")
    public ResponseDto<NotificationResponse.GetNotificationSettingInfoDto> getNotificationSettingInfo(
            @AuthenticationPrincipal SecurityUserDetails memberInfo,
            @Parameter(name = "기기 토큰") @RequestParam String deviceToken
    ){
        return ResponseDto.onSuccess(userUseCase.getNotificationSettingInfo(deviceToken, memberInfo.getUserId()));
    }

    @Operation(summary = "푸시 알림 설정", description = "푸시 알림 설정을 활성/ 비활성화합니다.")
    @PatchMapping(value = "/settings/notifications/{deviceId}")
    public ResponseDto<String> createInitialNotificationSetting(
            @AuthenticationPrincipal SecurityUserDetails memberInfo,
            @RequestParam boolean request,
            @PathVariable Long deviceId){
        userUseCase.updateNotificationEnabled(deviceId, request,  memberInfo.getUserId());
        return ResponseDto.onSuccess(request ? "푸시 알림이 활성화되었습니다." : "푸시 알림이 비활성화되었습니다.");
    }
}
