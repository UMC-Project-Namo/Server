package com.namo.spring.application.external.api.schedule.service;

import com.namo.spring.db.mysql.domains.category.entity.Category;
import com.namo.spring.db.mysql.domains.category.entity.Palette;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.service.ParticipantService;
import com.namo.spring.db.mysql.domains.schedule.type.ParticipantRole;
import com.namo.spring.db.mysql.domains.schedule.type.ParticipantStatus;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.namo.spring.application.external.api.schedule.converter.ParticipantConverter.toParticipant;

@Component
@RequiredArgsConstructor
public class ParticipantMaker {

    private final ParticipantService participantService;

    @Transactional
    public void makeScheduleOwner(Schedule schedule, Member member, Category category, Palette palette) {
        Participant participant = toParticipant(member, ParticipantRole.OWNER, schedule, ParticipantStatus.ACTIVE, category, palette);
        participantService.createParticipant(participant);
    }

    @Transactional
    public void makeMeetingScheduleParticipant(Schedule schedule, User user) {
        Participant participant = toParticipant(user, ParticipantRole.NON_OWNER, schedule, ParticipantStatus.INACTIVE, null, null);
        participantService.createParticipant(participant);
    }

}
