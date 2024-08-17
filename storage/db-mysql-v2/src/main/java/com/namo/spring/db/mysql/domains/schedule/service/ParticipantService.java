package com.namo.spring.db.mysql.domains.schedule.service;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.repository.ParticipantRepository;
import com.namo.spring.db.mysql.domains.schedule.type.ScheduleType;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

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
    public List<Participant> findScheduleParticipantItemsByScheduleIds(Member member) {
        return participantRepository.findParticipantsByMemberAndScheduleType(member, ScheduleType.MEETING.getValue());
    }

    @Transactional
    public void deleteParticipant(Long id) {
        participantRepository.deleteById(id);
    }
}
