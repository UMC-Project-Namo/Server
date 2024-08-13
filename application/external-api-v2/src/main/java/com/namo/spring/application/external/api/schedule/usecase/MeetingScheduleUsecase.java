package com.namo.spring.application.external.api.schedule.usecase;

import com.namo.spring.application.external.api.schedule.dto.MeetingScheduleResponse;
import com.namo.spring.application.external.api.schedule.service.ScheduleSearchService;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.exception.MemberException;
import com.namo.spring.db.mysql.domains.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.namo.spring.application.external.api.schedule.converter.MeetingScheduleResponseConverter.toGetMeetingScheduleDtos;

@Service
@RequiredArgsConstructor
@Component
public class MeetingScheduleUsecase {
    private final MemberService memberService;
    private final ScheduleSearchService scheduleSearchService;

    private Member getMember(Long memberId) {
        return memberService.readMember(memberId)
                .orElseThrow(() -> new MemberException(ErrorStatus.NOT_FOUND_USER_FAILURE));
    }

    public List<MeetingScheduleResponse.GetMeetingScheduleDto> getMeetingSchedules(Long memberId) {
        return toGetMeetingScheduleDtos(scheduleSearchService.getMeetingSchedulesByMember(getMember(memberId)));
    }

}
