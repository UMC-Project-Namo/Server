package com.namo.spring.application.external.api.record.serivce;

import java.util.List;

import org.springframework.stereotype.Service;

import com.namo.spring.application.external.api.record.converter.ActivityParticipantConverter;
import com.namo.spring.application.external.api.record.dto.ActivityRequest;
import com.namo.spring.application.external.api.schedule.service.ParticipantManageService;
import com.namo.spring.db.mysql.domains.record.entity.Activity;
import com.namo.spring.db.mysql.domains.record.entity.ActivityParticipant;
import com.namo.spring.db.mysql.domains.record.service.ActivityUserService;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityParticipantManageService {

    private final ActivityUserService activityUserService;
    private final ParticipantManageService participantManageService;

    public List<ActivityParticipant> createActivityParticipant(Activity activity, List<Long> participantIdList) {
        List<Participant> participants = participantIdList.stream()
                .map(participantManageService::getParticipant)
                .toList();
        List<ActivityParticipant> activityParticipants = participants.stream()
                .map(participant -> ActivityParticipantConverter.toActivityParticipant(participant, activity))
                .toList();

        return activityUserService.createActivityParticipants(activityParticipants);
    }

    public void addSettlement(ActivityRequest.ActivitySettlementDto settlement, List<ActivityParticipant> activityParticipants) {
        activityParticipants.stream()
                .filter(activityParticipant ->
                        settlement.getParticipantIdList().contains(activityParticipant.getParticipant().getId()))
                .forEach(activityParticipant -> {
                    activityParticipant.setSettlementInfo(settlement.getAmountPerPerson());
                });
    }
}
