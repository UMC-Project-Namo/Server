package com.namo.spring.application.external.api.schedule.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

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

@Slf4j
@Component
@RequiredArgsConstructor
public class ParticipationActionManager {
    // todo : 기획 변경에 따라 활동 초대 수락, 거절이 없어짐 -> 미사용 class
    private final CategoryService categoryService;
    private final PaletteService paletteService;
    private final ParticipantService participantService;

    /**
     * 모임 요청을 수락했을 시, participant를 활성화
     */
    public void activateParticipant(Schedule schedule, Participant participant) {
        Category category = categoryService.readMeetingCategoryByMember(participant.getMember());
        Palette palette = participant.getMember().getPalette();
        schedule.addActiveParticipant(participant.getMember().getNickname());
        participant.activateStatus(category, palette);
    }

    /**
     * 모임 요청을 거절했을 시, participant를 삭제
     */
    public void removeParticipant(Participant participant) {
        participantService.deleteParticipant(participant.getId());
    }

    public void removeParticipants(Schedule schedule, List<Participant> participants) {
        List<User> users = participants.stream().map(Participant::getUser).collect(Collectors.toList());
        schedule.removeParticipants(users.stream().map(User::getNickname).collect(Collectors.toList()));
        participantService.deleteByIdIn(participants.stream().map(Participant::getId).collect(Collectors.toList()));
    }

}
