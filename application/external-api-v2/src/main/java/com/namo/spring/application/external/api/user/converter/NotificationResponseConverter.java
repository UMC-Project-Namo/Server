package com.namo.spring.application.external.api.user.converter;

import com.namo.spring.application.external.api.user.dto.NotificationResponse;
import com.namo.spring.db.mysql.domains.notification.entity.Device;
import com.namo.spring.db.mysql.domains.user.entity.Member;

import java.util.List;

public class NotificationResponseConverter {

    public static NotificationResponse.GetNotificationSettingInfoDto toGetNotificationSettingInfoDto(Member member, Device device) {

        return NotificationResponse.GetNotificationSettingInfoDto.builder()
                .notificationEnabled(member.isNotificationEnabled())
                .deviceInfo(toDeviceInfo(device))
                .build();
    }

    public static NotificationResponse.DeviceInfo toDeviceInfo(Device device) {
        return device==null ? null :
                NotificationResponse.DeviceInfo.builder()
                        .deviceId(device.getId())
                        .deviceToken(device.getReceiverDeviceToken())
                        .deviceType(device.getReceiverDeviceType().name())
                        .build();
    }
}
