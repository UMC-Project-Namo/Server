package com.namo.spring.db.mysql.domains.notification.repository;

import com.namo.spring.db.mysql.domains.notification.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByMemberId(Long memberId);

    List<Device> findAllByMemberId(Long memberId);

    List<Device> findAllByMemberIdIn(List<Long> memberId);
}
