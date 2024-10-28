package com.namo.spring.db.mysql.domains.notification.service;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.notification.entity.Device;
import com.namo.spring.db.mysql.domains.notification.repository.DeviceRepository;
import com.namo.spring.db.mysql.domains.notification.type.ReceiverDeviceType;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@DomainService
@RequiredArgsConstructor
public class DeviceService {
    private final DeviceRepository deviceRepository;

    public Optional<Device> readById(Long id) {
        return deviceRepository.findById(id);
    }

    public List<Device> readByMemberId(Long memberId) {
        return deviceRepository.findAllByMemberId(memberId);
    }

    public List<Device> readByMemberIds(List<Long> memberIds) {
        return deviceRepository.findAllByMemberIdIn(memberIds);
    }

    public void createDevice(Device device) {
        deviceRepository.save(device);
    }

    public Optional<Device> readDeviceByTokenAndMemberId(ReceiverDeviceType deviceType, String token, Long memberId) {
        return deviceRepository.findByReceiverDeviceTypeAndReceiverDeviceTokenAndMemberId(deviceType, token, memberId);
    }


}
