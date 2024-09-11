package com.namo.spring.application.external.api.schedule.converter;

import com.namo.spring.application.external.api.schedule.dto.GuestParticipantResponse;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;

public class GuestParticipantResponseConverter {

    public static GuestParticipantResponse.PostGuestParticipantDto toPostGuestParticipantDto(Participant participant) {
        return GuestParticipantResponse.PostGuestParticipantDto.builder()
                .userId(participant.getAnonymous().getId())
                .tag(participant.getAnonymous().getTag())
                .participantId(participant.getId())
                .scheduleId(participant.getSchedule().getId())
                .build();
    }
}
