package com.namo.spring.application.external.api.user.usecase;

import com.namo.spring.application.external.api.notification.service.NotificationManageService;
import com.namo.spring.application.external.api.user.dto.NotificationRequest;
import com.namo.spring.application.external.api.user.dto.NotificationResponse;
import com.namo.spring.db.mysql.domains.notification.entity.Device;
import com.namo.spring.db.mysql.domains.notification.type.ReceiverDeviceType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.application.external.api.user.service.MemberManageService;
import com.namo.spring.db.mysql.domains.category.entity.Palette;
import com.namo.spring.db.mysql.domains.category.service.PaletteService;
import com.namo.spring.db.mysql.domains.user.entity.Member;

import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.namo.spring.application.external.api.user.converter.NotificationResponseConverter.toGetNotificationSettingInfoDto;

@Component
@RequiredArgsConstructor
public class UserUseCase {

    private final MemberManageService memberManageService;
    private final NotificationManageService notificationManageService;
    private final PaletteService paletteService;

    @Transactional
    public void updatePreferenceColor(Long memberId, Long colorId) {
        Member target = memberManageService.getActiveMember(memberId);
        Palette palette = paletteService.getPalette(colorId);
        target.updatePalette(palette);
    }

    @Transactional
    public void createDeviceInfoAndNotificationEnabled(NotificationRequest.CreateDeviceInfoDto request, ReceiverDeviceType deviceType, Long memberId) {
        Member target = memberManageService.getActiveMember(memberId);
        notificationManageService.createDeviceInfoAndNotificationEnabled(request, deviceType, target);
    }

    @Transactional
    public void updateNotificationEnabled(boolean request, Long memberId) {
        Member target = memberManageService.getActiveMember(memberId);
        notificationManageService.updateNotificationSetting(request, target);
    }

    @Transactional(readOnly = true)
    public NotificationResponse.GetNotificationSettingInfoDto getNotificationSettingInfo(ReceiverDeviceType deviceType, String deviceToken, Long memberId){
        Member target = memberManageService.getActiveMember(memberId);
        Device device = notificationManageService.getNotificationSettingInfo(deviceType, deviceToken, target.getId());
        return toGetNotificationSettingInfoDto(target, device);
    }
}
