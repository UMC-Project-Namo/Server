package com.namo.spring.db.mysql.domains.schedule.service;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.schedule.dto.MeetingScheduleQueryDto;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.repository.ScheduleRepository;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@DomainService
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public Schedule createSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    @Transactional(readOnly = true)
    public Optional<Schedule> readSchedule(Long id) {
        return scheduleRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<MeetingScheduleQueryDto> readMeetingSchedulesWithParticipantsByMember(Member member) {
        return scheduleRepository.findMeetingSchedulesWithParticipantsByMember(member);
    }

    @Transactional
    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }
}
