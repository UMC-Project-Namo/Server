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

import java.util.List;
import java.util.stream.Collectors;

import static com.namo.spring.application.external.api.schedule.converter.ParticipantConverter.toParticipant;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParticipantMaker {
    private final PaletteService paletteService;
    private final CategoryService categoryService;
    private final ParticipantService participantService;

    public void makeScheduleOwner(Schedule schedule, Member member, Long categoryId, Long paletteId) {
        Category category;
        Palette palette = null;
        if (categoryId != null) {
            category = categoryService.readCategoryByMemberAndId(categoryId, member);
        } else category = categoryService.readMeetingCategoryByMember(member);
        if (paletteId != null) {
            palette = paletteService.getPalette(paletteId);
        }
        Participant participant = toParticipant(member, ParticipantRole.OWNER, schedule, ParticipantStatus.ACTIVE, category, palette);
        participantService.createParticipant(participant);
    }

    public void makeMeetingScheduleParticipants(Schedule schedule, List<Member> members) {
        List<Participant> participants = members.stream()
                .map(member -> toMeetingScheduleParticipant(schedule, member))
                .collect(Collectors.toList());
        participantService.createParticipants(participants);
    }

    public Participant toMeetingScheduleParticipant(Schedule schedule, User user) {
        Participant participant = null;
        if (user instanceof Member) {
            participant = toParticipant(user, ParticipantRole.NON_OWNER, schedule, ParticipantStatus.INACTIVE, null, null);
        } else if (user instanceof Anonymous) {
            participant = toParticipant(user, ParticipantRole.NON_OWNER, schedule, ParticipantStatus.ACTIVE, null, null);
        }
        return participant;
    }

}
