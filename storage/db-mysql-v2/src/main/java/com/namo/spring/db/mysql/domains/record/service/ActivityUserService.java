package com.namo.spring.db.mysql.domains.record.service;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.record.entity.ActivityParticipant;
import com.namo.spring.db.mysql.domains.record.repository.ActivityParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@DomainService
@RequiredArgsConstructor
public class ActivityUserService {
    private final ActivityParticipantRepository activityUserRepository;

    @Transactional
    public ActivityParticipant createActivityParticipant(ActivityParticipant activityParticipant) {
        return activityUserRepository.save(activityParticipant);
    }

    @Transactional(readOnly = true)
    public Optional<ActivityParticipant> readActivityParticipant(Long activityParticipantId) {
        return activityUserRepository.findById(activityParticipantId);
    }

    @Transactional
    public void deleteActivityParticipant(Long activityParticipantId) {
        activityUserRepository.deleteById(activityParticipantId);
    }
}
