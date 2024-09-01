package com.namo.spring.db.mysql.domains.schedule.service;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.schedule.dto.ScheduleParticipantQuery;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.repository.ParticipantRepository;
import com.namo.spring.db.mysql.domains.schedule.type.ParticipantStatus;
import com.namo.spring.db.mysql.domains.schedule.type.ScheduleType;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DomainService
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantRepository participantRepository;

    @Transactional
    public Participant createParticipant(Participant participant) {
        return participantRepository.save(participant);
    }

    @Transactional(readOnly = true)
    public Optional<Participant> readParticipant(Long id) {
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
    public List<Participant> readParticipantsByIdAndScheduleId(List<Long> participantIds, Long scheduleId, ParticipantStatus status) {
        return participantRepository.findParticipantByIdAndScheduleId(participantIds, scheduleId, status);
    }

    @Transactional(readOnly = true)
    public List<Participant> readScheduleParticipantItemsByScheduleIds(Long memberId) {
        return participantRepository.findParticipantsByMemberAndScheduleType(memberId, ScheduleType.MEETING.getValue());
    }

    @Transactional(readOnly = true)
    public boolean existsByScheduleIdAndMemberId(Long scheduleId, Long memberId) {
        return participantRepository.existsByScheduleIdAndMemberId(scheduleId, memberId);
    }

    @Transactional(readOnly = true)
    public List<Participant> readParticipantsWithScheduleAndCategoryByPeriod(Long memberId, LocalDateTime startDate, LocalDateTime endDate) {
        return participantRepository.findParticipantsWithScheduleAndCategoryByPeriod(memberId, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<ScheduleParticipantQuery> readParticipantsWithUserAndScheduleByPeriod(List<Long> memberIds, LocalDateTime startDate, LocalDateTime endDate) {
        return participantRepository.findParticipantsWithUserAndScheduleByPeriod(memberIds, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<Participant> readParticipantsByScheduleIdAndStatusAndType(Long scheduleId, ScheduleType type, ParticipantStatus status) {
        return participantRepository.findParticipantsByScheduleIdAndStatusAndType(scheduleId, type.getValue(), status);
    }

    public void deleteParticipant(Long id) {
        participantRepository.deleteById(id);
    }

    public void deleteByIdIn(List<Long> Ids) {
        participantRepository.deleteByIdIn(Ids);
    }

    public Optional<Participant> readParticipants(Long memberId, Long scheduleId) {
        return participantRepository.findParticipantByMemberIdAndScheduleId(memberId, scheduleId);
    }

}
