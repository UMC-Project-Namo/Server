package com.namo.spring.application.external.api.schedule.service;

import com.namo.spring.application.external.api.record.enums.DiaryFilter;
import com.namo.spring.application.external.api.schedule.dto.MeetingScheduleRequest;
import com.namo.spring.application.external.global.utils.PeriodValidationUtils;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.exception.ParticipantException;
import com.namo.spring.db.mysql.domains.schedule.exception.ScheduleException;
import com.namo.spring.db.mysql.domains.schedule.service.ParticipantService;
import com.namo.spring.db.mysql.domains.schedule.type.Period;
import com.namo.spring.db.mysql.domains.user.entity.Friendship;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.entity.User;
import com.namo.spring.db.mysql.domains.user.exception.MemberException;
import com.namo.spring.db.mysql.domains.user.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipantManageService {
    private final ParticipantMaker participantMaker;
    private final FriendshipService friendshipService;
    private final ParticipantService participantService;

    public void createScheduleOwner(Member member, Schedule schedule, Long categoryId, Long paletteId) {
        participantMaker.makeScheduleOwner(schedule, member, categoryId, paletteId);
    }

    public void createMeetingParticipants(Member owner, Schedule schedule, List<Long> memberIds){
        List<Member> participants = getFriendshipValidatedParticipants(owner.getId(), memberIds);
        participantMaker.makeMeetingScheduleParticipants(schedule, participants);
        List<String> participantNicknames = new ArrayList<>();
        participantNicknames.addAll(participants.stream().map(Member::getNickname).collect(Collectors.toList()));
        schedule.setMemberParticipantsInfo(participantNicknames);
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

    /**
     * scheduleId와 memberId로 찾은
     * Participant 객체를 Member, Schedule과 함께 로딩하여 반환합니다.
     * schedule 에 대해 Fetch 조회 됩니다.
     * @param memberId
     * @param scheduleId
     * @return Participant
     */
    public Participant getParticipantWithScheduleAndMember(Long scheduleId, Long memberId) {
        return participantService.readParticipantByScheduleIdAndMemberId(scheduleId, memberId).orElseThrow(
                () -> new ScheduleException(ErrorStatus.NOT_SCHEDULE_PARTICIPANT));
    }

    public void deleteParticipant(Participant participant, Schedule schedule) {
        participantService.deleteParticipant(participant.getId());
        schedule.removeParticipant(participant.getMember().getNickname());
    }

    public void updateMeetingScheduleParticipants(Long ownerId, Schedule schedule,
            MeetingScheduleRequest.PatchMeetingScheduleDto dto) {
        if (!dto.getParticipantsToAdd().isEmpty()) {
            List<Member> participantsToAdd = getFriendshipValidatedParticipants(ownerId, dto.getParticipantsToAdd());
            participantMaker.makeMeetingScheduleParticipants(schedule, participantsToAdd);
        }
    }

    private void removeParticipants(Schedule schedule, List<Participant> participants) {
        List<User> users = participants.stream().map(Participant::getUser).collect(Collectors.toList());
        schedule.removeParticipants(users.stream().map(User::getNickname).collect(Collectors.toList()));
        participantService.deleteByIdIn(participants.stream().map(Participant::getId).collect(Collectors.toList()));
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
        return participantService.readMemberParticipant(memberId, scheduleId)
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

        DiaryFilter diaryFilter = DiaryFilter.from(filterType);
        return diaryFilter.apply(participantService, memberId, pageable, keyword);
    }

    /**
     * 해당 월의 00:00:00 부터 해당 월의 마지막날 23:59:59 까지 스케줄에 대하여 작성된 일기가 있는 참여정보 목록을 가져오는 메서드 입니다.
     *
     * @param memberId  사용자 ID
     * @param yearMonth 년:월
     * @return 일기가 작성된 참여 정보 목록
     */
    public List<Participant> getMyParticipantByMonthForDiary(Long memberId, YearMonth yearMonth) {
        Period period = PeriodValidationUtils.getMonthPeriod(yearMonth.getYear(), yearMonth.getMonthValue());
        return participantService.readParticipantHasDiaryByDateRange(memberId, period.getStartDate(), period.getEndDate());
    }

    /**
     * 해당 날짜의 시작 시각 00:00:00 부터 끝 시각 23:59:59 사이의 스케줄에 대하여 작성된 일기 정보가 있는 참여정보 목록을 가져오는 메서드입니다.
     *
     * @param memberId  사용자 ID
     * @param localDate 년:월:일
     * @return 일기가 작성된 참여 정보 목록
     */
    public List<Participant> getMyParticipantByDayForDiary(Long memberId, LocalDate localDate) {
        Period period = PeriodValidationUtils.getPeriodForDay(localDate);
        return participantService.readParticipantHasDiaryByDateRange(memberId, period.getStartDate(), period.getEndDate());
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
        List<Participant> participants = participantService.readParticipantsByIdsAndScheduleId(participantIdList, scheduleId);
        if (participantIdList.size() != participants.size()){
            throw new ParticipantException(ErrorStatus.NOT_SCHEDULE_PARTICIPANT);
        }
        return participants;
    }
}
