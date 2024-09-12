package com.namo.spring.db.mysql.domains.notification.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.namo.spring.db.mysql.common.converter.ReceiverDeviceTypeConverter;
import com.namo.spring.db.mysql.common.model.BaseTimeEntity;
import com.namo.spring.db.mysql.domains.notification.type.ReceiverDeviceType;
import com.namo.spring.db.mysql.domains.user.entity.Member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    public Device(Member member, ReceiverDeviceType receiverDeviceType, String receiverDeviceToken,
            String receiverDeviceAgent) {
        this.member = member;
        this.receiverDeviceType = receiverDeviceType;
        this.receiverDeviceToken = receiverDeviceToken;
        this.receiverDeviceAgent = receiverDeviceAgent;
    }
}
