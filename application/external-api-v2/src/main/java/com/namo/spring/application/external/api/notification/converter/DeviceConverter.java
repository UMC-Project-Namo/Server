package com.namo.spring.application.external.api.notification.converter;

import com.namo.spring.application.external.api.user.dto.NotificationRequest;
import com.namo.spring.db.mysql.domains.notification.entity.Device;
import com.namo.spring.db.mysql.domains.notification.type.ReceiverDeviceType;
import com.namo.spring.db.mysql.domains.user.entity.Member;

public class DeviceConverter {
    public static Device toDevice(NotificationRequest.CreateDeviceInfoDto request, ReceiverDeviceType deviceType, Member member) {
        return Device.builder()
                .member(member)
                .receiverDeviceType(deviceType)
                .receiverDeviceToken(request.getReceiverDeviceToken())
                .receiverDeviceAgent(request.getReceiverDeviceAgent())
                .build();
    }

}
