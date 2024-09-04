package com.namo.spring.application.external.api.schedule.service;

import com.namo.spring.application.external.api.schedule.dto.ScheduleRequest;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.category.entity.Palette;
import com.namo.spring.db.mysql.domains.category.exception.PaletteException;
import com.namo.spring.db.mysql.domains.category.type.ColorChip;
import com.namo.spring.db.mysql.domains.category.type.PaletteEnum;
import com.namo.spring.db.mysql.domains.record.exception.DiaryException;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.exception.ScheduleException;
import com.namo.spring.db.mysql.domains.schedule.service.ParticipantService;
import com.namo.spring.db.mysql.domains.schedule.type.ParticipantRole;
import com.namo.spring.db.mysql.domains.schedule.type.ParticipantStatus;
import com.namo.spring.db.mysql.domains.schedule.type.ScheduleType;
import com.namo.spring.db.mysql.domains.user.entity.Friendship;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.exception.MemberException;
import com.namo.spring.db.mysql.domains.user.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipantManageService {
    private static final Long MEETING_SCHEDULE_OWNER_PALETTE_ID = ColorChip.getMeetingScheduleOwnerPaletteId();
    private static final long[] PALETTE_IDS = PaletteEnum.getPaletteColorIds();
    private final ParticipantMaker participantMaker;
    private final ParticipationActionManager participationActionManager;
    private final FriendshipService friendshipService;
    private final ParticipantService participantService;

    public void createPersonalScheduleParticipant(Member member, Schedule schedule, Long categoryId) {
        participantMaker.makeScheduleOwner(schedule, member, categoryId, null);
    }

    public void createMeetingScheduleParticipants(Member owner, Schedule schedule, List<Member> participants) {
        participantMaker.makeScheduleOwner(schedule, owner, null, MEETING_SCHEDULE_OWNER_PALETTE_ID);
        schedule.addActiveParticipant(owner.getNickname());
        participantMaker.makeMeetingScheduleParticipants(schedule, participants);
    }

    public List<Member> getFriendshipValidatedParticipants(Long ownerId, List<Long> memberIds) {
        List<Member> friends = friendshipService.readFriendshipsByMemberIdAndFriendIds(ownerId, memberIds).stream()
                .map(Friendship::getFriend)
                .collect(Collectors.toList());
        if (memberIds.size() != friends.size()) {
            throw new MemberException(ErrorStatus.NOT_FOUND_FRIENDSHIP_FAILURE);
        }
        return friends;
    }

    public void validateScheduleOwner(Schedule schedule, Long memberId) {
        Participant participant = getValidatedMeetingParticipantWithSchedule(memberId, schedule.getId());
        if (participant.getIsOwner() != ParticipantRole.OWNER.getValue()) {
            throw new ScheduleException(ErrorStatus.NOT_SCHEDULE_OWNER);
        }
    }

    public Participant getValidatedMeetingParticipantWithSchedule(Long memberId, Long scheduleId) {
        return participantService.readParticipantByScheduleIdAndMemberId(scheduleId, memberId).orElseThrow(
                () -> new ScheduleException(ErrorStatus.NOT_SCHEDULE_PARTICIPANT));
    }

    public void activateParticipant(Long memberId, Long scheduleId) {
        Participant participant = getValidatedMeetingParticipantWithSchedule(scheduleId, memberId);
        Long paletteId = selectPaletteColorId(scheduleId);
        participationActionManager.activateParticipant(participant.getSchedule(), participant, paletteId);
    }

    private Long selectPaletteColorId(Long scheduleId) {
        List<Long> participantsColors = getMeetingScheduleParticipants(scheduleId, ParticipantStatus.ACTIVE)
                .stream().map(Participant::getPalette).map(Palette::getId).collect(Collectors.toList());
        return Arrays.stream(PALETTE_IDS)
                .filter((color) -> !participantsColors.contains(color))
                .findFirst()
                .orElseThrow(() -> new PaletteException(ErrorStatus.NOT_FOUND_COLOR));
    }

    public void updateMeetingScheduleParticipants(Long ownerId, Schedule schedule, ScheduleRequest.PatchMeetingScheduleDto dto) {
        if (!dto.getParticipantsToAdd().isEmpty()) {
            List<Member> participantsToAdd = getFriendshipValidatedParticipants(ownerId, dto.getParticipantsToAdd());
            participantMaker.makeMeetingScheduleParticipants(schedule, participantsToAdd);
        }

        if (!dto.getParticipantsToRemove().isEmpty()) {
            List<Participant> participantsToRemove = participantService.readParticipantsByIdAndScheduleId(dto.getParticipantsToRemove(), schedule.getId(), ParticipantStatus.ACTIVE);
            if (participantsToRemove.isEmpty()) {
                throw new ScheduleException(ErrorStatus.NOT_FOUND_PARTICIPANT_FAILURE);
            }
            participationActionManager.removeParticipants(schedule, participantsToRemove);
        }
    }

    public List<Participant> getMeetingScheduleParticipants(Long scheduleId, ParticipantStatus status) {
        List<Participant> participants = participantService.readParticipantsByScheduleIdAndStatusAndType(scheduleId, ScheduleType.MEETING, status);
        if (participants.isEmpty()) {
            throw new ScheduleException(ErrorStatus.SCHEDULE_PARTICIPANT_IS_EMPTY_ERROR);
        } else return participants;
    }

    public Participant getScheduleParticipant(Long memberId, Long scheduleId) {
        return participantService.readParticipant(memberId, scheduleId)
                .orElseThrow(() -> new MemberException(ErrorStatus.NOT_FOUND_PARTICIPANT_FAILURE));
    }

    public List<Participant> getMyParticipationForArchive(Long memberId, int page, String filterType, String keyword) {
        Pageable pageable = PageRequest.of(page - 1, 5);
        if (filterType == null || filterType.isEmpty())
            return participantService.readParticipantsForDiary(memberId, pageable);

        switch (filterType) {
            case "ScheduleName" -> {
                return participantService.readParticipantByScheduleName(memberId, pageable, keyword);
            }
            case "DiaryContent" -> {
                return participantService.readParticipantByDiaryContent(memberId, pageable, keyword);
            }
            case "MemberNickname" -> {
                return participantService.readParticipantByMember(memberId, pageable, keyword);
            }
            default -> {
                throw new DiaryException(ErrorStatus.NOT_FILTERTYPE_OF_ARCHIVE);
            }
        }
    }
}
