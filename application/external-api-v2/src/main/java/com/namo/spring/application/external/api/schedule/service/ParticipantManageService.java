package com.namo.spring.application.external.api.schedule.service;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.category.type.ColorChip;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.exception.ScheduleException;
import com.namo.spring.db.mysql.domains.schedule.service.ParticipantService;
import com.namo.spring.db.mysql.domains.schedule.type.ParticipantStatus;
import com.namo.spring.db.mysql.domains.schedule.type.ScheduleType;
import com.namo.spring.db.mysql.domains.user.entity.Friendship;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.exception.MemberException;
import com.namo.spring.db.mysql.domains.user.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipantManageService {
    private static final Long MEETING_SCHEDULE_OWNER_PALETTE_ID = ColorChip.getMeetingScheduleOwnerPaletteId();
    private final ParticipantMaker participantMaker;
    private final FriendshipService friendshipService;
    private final ParticipantService participantService;

    public void createPersonalScheduleParticipant(Member member, Schedule schedule, Long categoryId) {
        participantMaker.makeScheduleOwner(schedule, member, categoryId, null);
    }

    public void createMeetingScheduleParticipants(Member owner, Schedule schedule, List<Member> participants) {
        participantMaker.makeScheduleOwner(schedule, owner, null, MEETING_SCHEDULE_OWNER_PALETTE_ID);
        schedule.addActiveParticipant(owner.getNickname());
        participants.forEach(participant -> participantMaker.makeMeetingScheduleParticipant(schedule, participant));
    }

    public List<Member> getValidatedParticipants(Member owner, List<Long> memberIds) {
        List<Member> friends = friendshipService.readFriendshipsByMemberIdAndFriendIds(owner.getId(), memberIds).stream()
                .map(Friendship::getFriend)
                .collect(Collectors.toList());
        if (memberIds.size() != friends.size()) {
            throw new MemberException(ErrorStatus.NOT_FOUND_FRIENDSHIP_FAILURE);
        }
        return friends;
    }

    public List<Participant> getMeetingScheduleParticipants(Long scheduleId) {
        List<Participant> participants = participantService.readParticipantsByScheduleIdAndScheduleType(scheduleId, ScheduleType.MEETING, ParticipantStatus.ACTIVE);
        if (participants == null) {
            throw new ScheduleException(ErrorStatus.NOT_FOUND_SCHEDULE_OR_PARTICIPANT_FAILURE);
        } else return participants;
    }

    private void checkMemberIsOwner(Long scheduleId, Long memberId) {
        if (!participantService.existsParticipantByMemberIdAndScheduleId(scheduleId, memberId)) {
            throw new ScheduleException(ErrorStatus.NOT_SCHEDULE_OWNER);
        }
    }

}
