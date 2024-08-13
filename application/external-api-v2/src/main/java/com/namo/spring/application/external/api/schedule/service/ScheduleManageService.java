package com.namo.spring.application.external.api.schedule.service;

import com.namo.spring.db.mysql.domains.schedule.dto.MeetingScheduleQueryDto;
import com.namo.spring.db.mysql.domains.schedule.service.ScheduleService;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleManageService {
    private ScheduleService scheduleService;

    public List<MeetingScheduleQueryDto> getMeetingSchedulesByMember(Member member) {
        return scheduleService.readMeetingSchedulesWithParticipantsByMember(member);
    }
}
