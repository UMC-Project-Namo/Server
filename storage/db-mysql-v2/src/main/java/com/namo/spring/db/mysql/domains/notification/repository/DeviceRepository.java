package com.namo.spring.db.mysql.domains.notification.repository;

import java.util.List;
import java.util.Optional;

import com.namo.spring.db.mysql.domains.notification.type.ReceiverDeviceType;
import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.db.mysql.domains.notification.entity.Device;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByMemberId(Long memberId);

    List<Device> findAllByMemberId(Long memberId);

    List<Device> findAllByMemberIdIn(List<Long> memberId);

    Optional<Device> findByReceiverDeviceTypeAndReceiverDeviceTokenAndMemberId(ReceiverDeviceType receiverDeviceType, String receiverDeviceToken, Long memberId);

}
