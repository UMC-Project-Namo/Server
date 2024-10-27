package com.namo.spring.application.external.global.utils;

import com.namo.spring.db.mysql.domains.notification.exception.NotificationException;
import com.namo.spring.db.mysql.domains.notification.type.ReceiverDeviceType;

import static com.namo.spring.core.common.code.status.ErrorStatus.INVALID_DEVICE_TYPE;

public class DeviceTypeValidationUtil {

    public static ReceiverDeviceType validatedDeviceType(String request) {
        ReceiverDeviceType deviceType =  ReceiverDeviceType.fromString(request);
        if (deviceType==null){
            throw new NotificationException(INVALID_DEVICE_TYPE);
        };
        return deviceType;
    }
}
