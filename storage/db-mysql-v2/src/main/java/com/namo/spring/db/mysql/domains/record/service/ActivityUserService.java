package com.namo.spring.db.mysql.domains.record.service;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.record.entity.Activity;
import com.namo.spring.db.mysql.domains.record.entity.ActivityParticipant;
import com.namo.spring.db.mysql.domains.record.repository.ActivityParticipantRepository;

import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class ActivityUserService {
    private final ActivityParticipantRepository activityUserRepository;

    @Transactional
    public ActivityParticipant createActivityParticipant(ActivityParticipant activityParticipant) {
        return activityUserRepository.save(activityParticipant);
    }

    @Transactional(readOnly = true)
    public Optional<ActivityParticipant> readActivityParticipants(Long activityParticipantId) {
        return activityUserRepository.findById(activityParticipantId);
    }

    @Transactional
    public void deleteActivityParticipant(Long activityParticipantId) {
        activityUserRepository.deleteById(activityParticipantId);
    }

    public void deleteAll(List<ActivityParticipant> activityParticipants){
        activityUserRepository.deleteAll(activityParticipants);
    }

    public List<ActivityParticipant> createActivityParticipants(List<ActivityParticipant> activityParticipants){
        return activityUserRepository.saveAll(activityParticipants);
    }

    /**
     * 정산 참여 여부에 따라 조회
     */
    public List<ActivityParticipant> readAllByActivityAndParticipantIdAndSettlementStatus(Activity activity, List<Long> participantIdList, boolean isInSettlement) {
        return activityUserRepository.findByActivityAndIncludedInSettlementAndParticipantIdIn(activity, isInSettlement, participantIdList);
    }

}
