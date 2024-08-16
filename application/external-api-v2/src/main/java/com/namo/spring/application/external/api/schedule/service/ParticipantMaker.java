package com.namo.spring.application.external.api.schedule.service;

import com.namo.spring.db.mysql.domains.category.entity.Category;
import com.namo.spring.db.mysql.domains.category.entity.Palette;
import com.namo.spring.db.mysql.domains.category.service.PaletteService;
import com.namo.spring.db.mysql.domains.category.type.ColorChip;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.service.ParticipantService;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.namo.spring.application.external.api.schedule.converter.ParticipantConverter.toParticipant;

@Component
@RequiredArgsConstructor
public class ParticipantMaker {
    private static final Long MEETING_SCHEDULE_OWNER_PALETTE_ID = ColorChip.getMeetingScheduleOwnerPaletteId();

    private final ParticipantService participantService;
    private final PaletteService paletteService;

    @Transactional
    public void makePersonalScheduleOwner(Schedule schedule, Member member, Category category) {
        Participant participant = toParticipant(member, schedule, category, null);
        participantService.createParticipant(participant);
    }

    @Transactional
    public void makeMeetingScheduleOwner(Schedule schedule, Member member, Category category) {
        Palette palette = paletteService.getPalette(MEETING_SCHEDULE_OWNER_PALETTE_ID);
        Participant participant = toParticipant(member, schedule, category, palette);
        participantService.createParticipant(participant);
    }

    @Transactional
    public void makeMeetingScheduleParticipant(Schedule schedule, User user) {
        Participant participant = toParticipant(user, schedule, null, null);
        participantService.createParticipant(participant);
    }

}
