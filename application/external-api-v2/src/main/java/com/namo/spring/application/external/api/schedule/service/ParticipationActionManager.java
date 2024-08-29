package com.namo.spring.application.external.api.schedule.service;

import com.namo.spring.db.mysql.domains.category.entity.Category;
import com.namo.spring.db.mysql.domains.category.entity.Palette;
import com.namo.spring.db.mysql.domains.category.service.CategoryService;
import com.namo.spring.db.mysql.domains.category.service.PaletteService;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.service.ParticipantService;
import com.namo.spring.db.mysql.domains.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParticipationActionManager {
    private final CategoryService categoryService;
    private final PaletteService paletteService;
    private final ParticipantService participantService;

    public void activateParticipant(Schedule schedule, Participant participant, Long paletteId) {
        Category category = categoryService.readMeetingCategoryByMember(participant.getMember());
        Palette palette = paletteService.getPalette(paletteId);
        schedule.addActiveParticipant(participant.getMember().getNickname());
        participant.activateStatus(category, palette);
    }

    public void removeParticipants(Schedule schedule, List<Participant> participants) {
        List<User> users = participants.stream().map(Participant::getUser).collect(Collectors.toList());
        schedule.removeParticipants(users.stream().map(User::getNickname).collect(Collectors.toList()));
        participantService.deleteByIdIn(participants.stream().map(Participant::getId).collect(Collectors.toList()));
    }

}
