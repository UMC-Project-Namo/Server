package com.namo.spring.application.external.api.guest.converter;

import com.namo.spring.application.external.api.guest.dto.GuestParticipantResponse;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;

public class GuestParticipantResponseConverter {

    public static GuestParticipantResponse.PostGuestParticipantDto toPostGuestParticipantDto(Participant participant) {
        return GuestParticipantResponse.PostGuestParticipantDto.builder()
                .nickname(participant.getAnonymous().getNickname())
                .tag(participant.getAnonymous().getTag())
                .participantId(participant.getId())
                .scheduleId(participant.getSchedule().getId())
                .build();
    }
}
