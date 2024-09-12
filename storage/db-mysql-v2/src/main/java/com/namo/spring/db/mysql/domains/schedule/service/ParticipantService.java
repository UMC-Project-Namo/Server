package com.namo.spring.db.mysql.domains.schedule.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.schedule.dto.ScheduleParticipantQuery;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.repository.ParticipantRepository;
import com.namo.spring.db.mysql.domains.schedule.type.ParticipantStatus;
import com.namo.spring.db.mysql.domains.schedule.type.ScheduleType;

import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantRepository participantRepository;

    @Transactional
    public Participant createParticipant(Participant participant) {
        return participantRepository.save(participant);
    }

    @Transactional
    public void createParticipants(List<Participant> participants) {
        participantRepository.saveAll(participants);
    }

    @Transactional(readOnly = true)
    public Optional<Participant> readMemberParticipant(Long id) {
        return participantRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Participant> readParticipantByScheduleIdAndMemberId(Long scheduleId, Long memberId) {
        return participantRepository.findParticipantByScheduleIdAndMemberId(scheduleId, memberId);
    }

    public List<Participant> readParticipantsByScheduleId(Long scheduleId) {
        return participantRepository.findAllByScheduleId(scheduleId);
    }

    @Transactional(readOnly = true)
    public List<Participant> readParticipantsByIdAndScheduleId(List<Long> participantIds, Long scheduleId,
            ParticipantStatus status) {
        return participantRepository.findParticipantByIdAndScheduleId(participantIds, scheduleId, status);
    }

    @Transactional(readOnly = true)
    public List<Participant> readScheduleParticipantSummaryByScheduleIds(Long memberId) {
        return participantRepository.findParticipantsByMemberAndScheduleType(memberId, ScheduleType.MEETING.getValue());
    }

    @Transactional(readOnly = true)
    public boolean existsByScheduleIdAndMemberId(Long scheduleId, Long memberId) {
        return participantRepository.existsByScheduleIdAndMemberId(scheduleId, memberId);
    }

    @Transactional(readOnly = true)
    public boolean existsByScheduleIdAndAnonymousId(Long scheduleId, Long anonymousId) {
        return participantRepository.existsByScheduleIdAndAnonymousId(scheduleId, anonymousId);
    }

    @Transactional(readOnly = true)
    public List<Participant> readParticipantsWithScheduleAndCategoryByPeriod(Long memberId, Boolean isShared,
            LocalDateTime startDate, LocalDateTime endDate) {
        return participantRepository.findParticipantsWithScheduleAndCategoryByPeriod(memberId, isShared, startDate,
                endDate);
    }

    @Transactional(readOnly = true)
    public List<ScheduleParticipantQuery> readParticipantsWithUserAndScheduleByPeriod(List<Long> memberIds,
            LocalDateTime startDate, LocalDateTime endDate) {
        return participantRepository.findParticipantsWithUserAndScheduleByPeriod(memberIds, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<Participant> readParticipantsByScheduleIdAndStatusAndType(Long scheduleId, ScheduleType type,
            ParticipantStatus status) {
        return participantRepository.findParticipantsByScheduleIdAndStatusAndType(scheduleId, type.getValue(), status);
    }

    public void deleteParticipant(Long id) {
        participantRepository.deleteById(id);
    }

    public void deleteByIdIn(List<Long> Ids) {
        participantRepository.deleteByIdIn(Ids);
    }

    public Optional<Participant> readMemberParticipant(Long memberId, Long scheduleId) {
        return participantRepository.findParticipantByMemberIdAndScheduleId(memberId, scheduleId);
    }

    public Optional<Participant> readAnonymousParticipant(Long anonymousId, Long scheduleId) {
        return participantRepository.findParticipantByAnonymousIdAndScheduleId(anonymousId, scheduleId);
    }

    public List<Participant> readParticipantsForDiary(Long memberId, Pageable pageable) {
        return participantRepository.findAllByMemberIdAndHasDiary(memberId, pageable);
    }

    public List<Participant> readParticipantHasDiaryByScheduleName(Long memberId, Pageable pageable, String keyword) {
        return participantRepository.findAllByScheduleTitleAndHasDiary(memberId, keyword, pageable);
    }

    public List<Participant> readParticipantHasDiaryByDiaryContent(Long memberId, Pageable pageable, String keyword) {
        return participantRepository.findAllByDiaryContentAndHasDiary(memberId, keyword, pageable);
    }

    public List<Participant> readParticipantHasDiaryByMember(Long memberId, Pageable pageable, String keyword) {
        return participantRepository.findAllByMemberAndHasDiary(memberId, keyword, pageable);
    }

    public List<Participant> readParticipantHasDiaryByDateRange(Long memberId, LocalDateTime startDate,
            LocalDateTime endDate) {
        return participantRepository.findAllByDateRangeAndHasDiary(memberId, startDate, endDate);
    }
}
