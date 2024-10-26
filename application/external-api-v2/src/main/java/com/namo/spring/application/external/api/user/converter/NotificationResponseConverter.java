package com.namo.spring.application.external.api.user.converter;

import com.namo.spring.application.external.api.user.dto.NotificationResponse;
import com.namo.spring.db.mysql.domains.notification.entity.Device;
import com.namo.spring.db.mysql.domains.user.entity.Member;

import java.util.List;

public class NotificationResponseConverter {

    public static NotificationResponse.GetNotificationSettingInfoDto toGetNotificationSettingInfoDto(Member member, Device device) {
        return NotificationResponse.GetNotificationSettingInfoDto.builder()
                .deviceId(device.getId())
                .notificationEnabled(member.isNotificationEnabled())
                .deviceType(device.getReceiverDeviceType().name())
                .deviceToken(device.getReceiverDeviceToken())
                .build();
    }
}
