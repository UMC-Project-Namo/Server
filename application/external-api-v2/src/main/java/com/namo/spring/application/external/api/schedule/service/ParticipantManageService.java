package com.namo.spring.application.external.api.schedule.service;

import static com.namo.spring.db.mysql.domains.schedule.type.ParticipantStatus.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.namo.spring.application.external.api.schedule.dto.MeetingScheduleRequest;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.record.exception.DiaryException;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.exception.ParticipantException;
import com.namo.spring.db.mysql.domains.schedule.exception.ScheduleException;
import com.namo.spring.db.mysql.domains.schedule.service.ParticipantService;
import com.namo.spring.db.mysql.domains.user.entity.Friendship;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.exception.MemberException;
import com.namo.spring.db.mysql.domains.user.service.FriendshipService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipantManageService {
    private final ParticipantMaker participantMaker;
    private final ParticipationActionManager participationActionManager;
    private final FriendshipService friendshipService;
    private final ParticipantService participantService;

    public void createPersonalScheduleParticipant(Member member, Schedule schedule, Long categoryId) {
        participantMaker.makeScheduleOwner(schedule, member, categoryId, null);
    }

    public void createMeetingScheduleParticipants(Member owner, Schedule schedule, List<Member> participants) {
        participantMaker.makeScheduleOwner(schedule, owner, null, owner.getPalette().getId());
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

    public Participant getValidatedParticipantWithSchedule(Long memberId, Long scheduleId) {
        return participantService.readParticipantByScheduleIdAndMemberId(scheduleId, memberId).orElseThrow(
                () -> new ScheduleException(ErrorStatus.NOT_SCHEDULE_PARTICIPANT));
    }

    public void activateParticipant(Long memberId, Long scheduleId) {
        Participant participant = getValidatedParticipantWithSchedule(scheduleId, memberId);
        participationActionManager.activateParticipant(participant.getSchedule(), participant);
    }

    public void deleteParticipant(Long memberId, Long scheduleId) {
        Participant participant = getValidatedParticipantWithSchedule(scheduleId, memberId);
        participationActionManager.removeParticipant(participant);
    }

    public void updateMeetingScheduleParticipants(Long ownerId, Schedule schedule,
            MeetingScheduleRequest.PatchMeetingScheduleDto dto) {
        if (!dto.getParticipantsToAdd().isEmpty()) {
            List<Member> participantsToAdd = getFriendshipValidatedParticipants(ownerId, dto.getParticipantsToAdd());
            participantMaker.makeMeetingScheduleParticipants(schedule, participantsToAdd);
        }

        if (!dto.getParticipantsToRemove().isEmpty()) {
            List<Participant> participantsToRemove = participantService.readParticipantsByIdsAndScheduleIdFetchUser(
                    dto.getParticipantsToRemove(), schedule.getId(), ACTIVE);
            if (participantsToRemove.isEmpty()) {
                throw new ScheduleException(ErrorStatus.NOT_FOUND_PARTICIPANT_FAILURE);
            }
            participationActionManager.removeParticipants(schedule, participantsToRemove);
        }
    }

    /**
     * 나의 스케줄 참여 정보를 찾아서 반환하는 메서드입니다.
     * !! 스케줄의 존재여부, 스케줄의 참여여부를 검증합니다.
     *
     * @param memberId
     * @param scheduleId
     * @return
     */
    public Participant getParticipantByMemberAndSchedule(Long memberId, Long scheduleId) {
        return participantService.readActiveMemberParticipant(memberId, scheduleId, ACTIVE)
                .orElseThrow(() -> new MemberException(ErrorStatus.NOT_FOUND_PARTICIPANT_FAILURE));
    }

    /**
     * 필터에 따라 검색하여 일기가 존재하는 참여 정보 목록을 입력된 페이지 객체 만큼 반환하는 메서드입니다. 필터가 없다면 기본 조회됩니다.
     *
     * @param memberId   사용자 ID
     * @param pageable   페이징 할 양
     * @param filterType (ScheduleName, DiaryContent, MemberNickname) 필터
     * @param keyword    키워드
     * @return 일기가 작성된 참여 정보 목록 (필터에 따라 다름)
     */
    public List<Participant> getMyParticipationForDiary(Long memberId, Pageable pageable, String filterType,
            String keyword) {
        if (filterType == null || filterType.isEmpty())
            return participantService.readParticipantsForDiary(memberId, pageable);

        switch (filterType) {
            case "ScheduleName" -> {
                return participantService.readParticipantHasDiaryByScheduleName(memberId, pageable, keyword);
            }
            case "DiaryContent" -> {
                return participantService.readParticipantHasDiaryByDiaryContent(memberId, pageable, keyword);
            }
            case "MemberNickname" -> {
                return participantService.readParticipantHasDiaryByMember(memberId, pageable, keyword);
            }
            default -> {
                throw new DiaryException(ErrorStatus.NOT_FILTERTYPE_OF_ARCHIVE);
            }
        }
    }

    /**
     * 해당 월의 00:00:00 부터 해당 월의 마지막날 23:59:59 까지 스케줄에 대하여 작성된 일기가 있는 참여정보 목록을 가져오는 메서드 입니다.
     *
     * @param memberId  사용자 ID
     * @param yearMonth 년:월
     * @return 일기가 작성된 참여 정보 목록
     */
    public List<Participant> getMyParticipantByMonthForDiary(Long memberId, YearMonth yearMonth) {
        LocalDateTime startDateTime = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDateTime = yearMonth.atEndOfMonth().atTime(23, 59, 59);
        return participantService.readParticipantHasDiaryByDateRange(memberId, startDateTime, endDateTime);
    }

    /**
     * 해당 날짜의 시작 시각 00:00:00 부터 끝 시각 23:59:59 사이의 스케줄에 대하여 작성된 일기 정보가 있는 참여정보 목록을 가져오는 메서드입니다.
     *
     * @param memberId  사용자 ID
     * @param localDate 년:월:일
     * @return 일기가 작성된 참여 정보 목록
     */
    public List<Participant> getMyParticipantByDayForDiary(Long memberId, LocalDate localDate) {
        LocalDateTime startDateTime = localDate.atStartOfDay(); // 해당 날 00:00:00
        LocalDateTime endDateTime = localDate.atTime(23, 59, 59); // 해당 날 23:59:59
        return participantService.readParticipantHasDiaryByDateRange(memberId, startDateTime, endDateTime);
    }

    /**
     * 스케줄에 포함된 참여자 중 participantIdList에 해당하는 Participant 들을 조회하는 메서드입니다.
     * !! 요청한 참여자 만큼 스케줄의 참여자를 검색했는지를 검증합니다.
     *
     * @param participantIdList
     * @param scheduleId
     * @return
     */
    public List<Participant> getParticipantsInSchedule(List<Long> participantIdList, Long scheduleId) {
        List<Participant> participants = participantService.readParticipantsByIdsAndScheduleId(participantIdList,
                scheduleId, ACTIVE);
        if (participantIdList.size() != participants.size()){
            throw new ParticipantException(ErrorStatus.NOT_SCHEDULE_PARTICIPANT_OR_NOT_ACTIVE);
        }
        return participants;
    }
}
