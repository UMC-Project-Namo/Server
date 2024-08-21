package com.namo.spring.application.external.api.schedule.service;

import com.namo.spring.db.mysql.domains.category.entity.Category;
import com.namo.spring.db.mysql.domains.category.entity.Palette;
import com.namo.spring.db.mysql.domains.category.service.CategoryService;
import com.namo.spring.db.mysql.domains.category.service.PaletteService;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.service.ParticipantService;
import com.namo.spring.db.mysql.domains.schedule.type.ParticipantRole;
import com.namo.spring.db.mysql.domains.schedule.type.ParticipantStatus;
import com.namo.spring.db.mysql.domains.user.entity.Anonymous;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.namo.spring.application.external.api.schedule.converter.ParticipantConverter.toParticipant;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParticipantMaker {
    private final PaletteService paletteService;
    private final CategoryService categoryService;
    private final ParticipantService participantService;

    @Transactional
    public void makeScheduleOwner(Schedule schedule, Member member, Long categoryId, Long paletteId) {
        Category category;
        if (categoryId != null) {
            category = categoryService.readCategoryByMemberAndId(categoryId, member);
        } else category = categoryService.readMeetingCategoryByMember(member);
        Palette palette = paletteService.getPalette(paletteId);
        Participant participant = toParticipant(member, ParticipantRole.OWNER, schedule, ParticipantStatus.ACTIVE, category, palette);
        participantService.createParticipant(participant);
    }

    @Transactional
    public void makeMeetingScheduleParticipant(Schedule schedule, User user) {
        Participant participant = null;
        if (user instanceof Member) {
            participant = toParticipant(user, ParticipantRole.NON_OWNER, schedule, ParticipantStatus.INACTIVE, null, null);
        } else if (user instanceof Anonymous) {
            participant = toParticipant(user, ParticipantRole.NON_OWNER, schedule, ParticipantStatus.ACTIVE, null, null);
            schedule.addActiveParticipant(participant.getMember().getNickname());
        }
        participantService.createParticipant(participant);
    }

}
