package com.namo.spring.application.external.api.record.converter;

import com.namo.spring.db.mysql.domains.record.entity.Activity;
import com.namo.spring.db.mysql.domains.record.entity.ActivityParticipant;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;

public class ActivityParticipantConverter {

    public static ActivityParticipant toActivityParticipant(Participant participant, Activity activity){
        return ActivityParticipant.builder()
                .participant(participant)
                .activity(activity)
                .build();
    }

}
