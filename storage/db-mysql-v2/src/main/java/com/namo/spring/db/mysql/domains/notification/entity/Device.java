package com.namo.spring.db.mysql.domains.notification.entity;

import com.namo.spring.db.mysql.common.converter.ReceiverDeviceTypeConverter;
import com.namo.spring.db.mysql.common.model.BaseTimeEntity;
import com.namo.spring.db.mysql.domains.notification.type.ReceiverDeviceType;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Device extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Convert(converter = ReceiverDeviceTypeConverter.class)
    @Column(nullable = false, length = 50)
    private ReceiverDeviceType receiverDeviceType;

    private String receiverDeviceToken;

    private String receiverDeviceAgent;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications;

    @Builder
    public Device(Member member, ReceiverDeviceType receiverDeviceType, String receiverDeviceToken, String receiverDeviceAgent) {
        this.member = member;
        this.receiverDeviceType = receiverDeviceType;
        this.receiverDeviceToken = receiverDeviceToken;
        this.receiverDeviceAgent = receiverDeviceAgent;
    }
}
