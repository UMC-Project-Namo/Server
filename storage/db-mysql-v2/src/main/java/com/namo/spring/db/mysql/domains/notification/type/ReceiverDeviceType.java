package com.namo.spring.db.mysql.domains.notification.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.namo.spring.db.mysql.common.converter.CodedEnum;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReceiverDeviceType implements CodedEnum {
    WEB("1", "웹"),
    IOS("2", "IOS"),
    ANDROID("3", "ANDROID"),
    ;

    private final String code;
    private final String type;

    @JsonCreator
    public static ReceiverDeviceType fromString(String value) {
        for (ReceiverDeviceType deviceType : values()) {
            if (deviceType.code.equals(value) || deviceType.type.equalsIgnoreCase(value) || deviceType.name().equalsIgnoreCase(value)) {
                return deviceType;
            }
        }
        return null;
    }

    @Override
    public String getCode() {
        return code;
    }

    @JsonValue
    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
