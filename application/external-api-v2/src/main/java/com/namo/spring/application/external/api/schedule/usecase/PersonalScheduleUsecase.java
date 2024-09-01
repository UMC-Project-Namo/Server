package com.namo.spring.application.external.api.schedule.usecase;

import com.namo.spring.application.external.api.schedule.dto.PersonalScheduleResponse;
import com.namo.spring.application.external.api.schedule.dto.ScheduleRequest;
import com.namo.spring.application.external.api.schedule.service.ScheduleManageService;
import com.namo.spring.application.external.api.user.service.MemberManageService;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.namo.spring.application.external.global.utils.SchedulePeriodValidationUtils.getExtendedPeriod;

@Service
@RequiredArgsConstructor
@Component
public class PersonalScheduleUsecase {
    private final MemberManageService memberManageService;
    private final ScheduleManageService scheduleManageService;

    @Transactional
    public Long createPersonalSchedule(ScheduleRequest.PostPersonalScheduleDto dto, Long memberId) {
        Member member = memberManageService.getMember(memberId);
        Schedule schedule = scheduleManageService.createPersonalSchedule(dto, member);
        return schedule.getId();
    }

    @Transactional(readOnly = true)
    public List<PersonalScheduleResponse.GetMonthlyScheduleDto> getMyMonthlySchedules(int year, int month, SecurityUserDetails member) {
        return scheduleManageService.getMyMonthlySchedules(member.getUserId(), getExtendedPeriod(year, month));
    }
}
