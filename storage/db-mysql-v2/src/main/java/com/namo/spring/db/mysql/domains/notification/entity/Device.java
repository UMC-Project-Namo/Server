package com.namo.spring.db.mysql.domains.notification.entity;

import com.namo.spring.db.mysql.common.converter.ReceiverDeviceTypeConverter;
import com.namo.spring.db.mysql.common.model.BaseTimeEntity;
import com.namo.spring.db.mysql.domains.notification.type.ReceiverDeviceType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Device extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Convert(converter = ReceiverDeviceTypeConverter.class)
    @Column(nullable = false, length = 50)
    private ReceiverDeviceType receiverDeviceType;

    private String receiverDeviceToken;

    private String receiverDeviceAgent;

    @Builder
    public Device(ReceiverDeviceType receiverDeviceType, String receiverDeviceToken, String receiverDeviceAgent) {
        this.receiverDeviceType = receiverDeviceType;
        this.receiverDeviceToken = receiverDeviceToken;
        this.receiverDeviceAgent = receiverDeviceAgent;
    }
}
