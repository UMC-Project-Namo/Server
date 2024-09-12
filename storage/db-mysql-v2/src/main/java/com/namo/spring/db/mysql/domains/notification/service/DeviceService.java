package com.namo.spring.db.mysql.domains.notification.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.notification.entity.Device;
import com.namo.spring.db.mysql.domains.notification.repository.DeviceRepository;

import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class DeviceService {
    private final DeviceRepository deviceRepository;

    @Transactional(readOnly = true)
    public List<Device> readByMemberId(Long memberId) {
        return deviceRepository.findAllByMemberId(memberId);
    }

    @Transactional(readOnly = true)
    public List<Device> readByMemberIds(List<Long> memberIds) {
        return deviceRepository.findAllByMemberIdIn(memberIds);
    }
}
