package com.namo.spring.db.mysql.common.converter;

import com.namo.spring.db.mysql.domains.notification.type.PublisherType;

public class PublisherTypeConverter extends AbstractEnumAttributeConverter<PublisherType> {
    private static final String ENUM_NAME = "알림 발행자 타입";

    public PublisherTypeConverter() {
        super(PublisherType.class, false, ENUM_NAME);
    }
}
