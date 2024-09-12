package com.namo.spring.db.mysql.domains.record.service;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.record.entity.ActivityImage;
import com.namo.spring.db.mysql.domains.record.repository.ActivityImageRepository;

import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class ActivityImageService {
    private final ActivityImageRepository activityImageRepository;

    @Transactional
    public ActivityImage createActivityImage(ActivityImage activityImage) {
        return activityImageRepository.save(activityImage);
    }

    @Transactional(readOnly = true)
    public Optional<ActivityImage> readActivityImage(Long activityImageId) {
        return activityImageRepository.findById(activityImageId);
    }

    @Transactional
    public void deleteActivityImage(Long activityImageId) {
        activityImageRepository.deleteById(activityImageId);
    }
}
