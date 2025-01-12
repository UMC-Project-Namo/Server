package com.namo.spring.application.external.api.schedule.usecase;

import static com.namo.spring.application.external.api.schedule.converter.PersonalScheduleResponseConverter.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.namo.spring.application.external.api.user.service.FriendManageService;
import com.namo.spring.db.mysql.domains.user.model.dto.FriendBirthdayListDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.application.external.api.notification.service.NotificationManageService;
import com.namo.spring.application.external.api.schedule.dto.PersonalScheduleRequest;
import com.namo.spring.application.external.api.schedule.dto.PersonalScheduleResponse;
import com.namo.spring.application.external.api.schedule.service.ScheduleManageService;
import com.namo.spring.application.external.api.user.service.MemberManageService;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.db.mysql.domains.notification.dto.ScheduleNotificationQuery;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.user.entity.Member;

import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
@Component
public class PersonalScheduleUsecase {
    private final MemberManageService memberManageService;
    private final ScheduleManageService scheduleManageService;
    private final NotificationManageService notificationManageService;
    private final FriendManageService friendManageService;

    @Transactional
    public Long createPersonalSchedule(PersonalScheduleRequest.PostPersonalScheduleDto dto,
            SecurityUserDetails memberInfo) {
        Member member = memberManageService.getActiveMember(memberInfo.getUserId());
        Schedule schedule = scheduleManageService.createPersonalSchedule(dto, member);
        if (!dto.getReminderTrigger().isEmpty()) {
            notificationManageService.createScheduleReminderNotification(schedule, member, dto.getReminderTrigger());
        }
        return schedule.getId();
    }

    @Transactional(readOnly = true)
    public List<PersonalScheduleResponse.GetMonthlyScheduleDto> getMyMonthlySchedules(LocalDate startDate, LocalDate endDate,
                                                                                      SecurityUserDetails memberInfo) {
        List<Participant> scheduleInfo = scheduleManageService.getMyMonthlySchedules(memberInfo.getUserId(),
                startDate, endDate);
        List<ScheduleNotificationQuery> scheduleNotifications = notificationManageService.getScheduleNotifications(
                memberInfo.getUserId(), scheduleInfo);
        return toGetMonthlyScheduleDtos(scheduleInfo, scheduleNotifications);
    }

    @Transactional(readOnly = true)
    public List<PersonalScheduleResponse.GetMonthlyFriendBirthdayDto> getMonthlyFriendsBirthday(LocalDate startDate, LocalDate endDate,
                                                                                                Long memberId) {
        List<FriendBirthdayListDto.FriendBirthdayDto> friendsBirthdays = friendManageService.getFriendsBirthday(memberId).friendsBirthdayDtoList();

        List<FriendBirthdayListDto.FriendBirthdayDto> friendsBirthdaysOfPeriod = friendsBirthdays.stream()
                .filter(friend -> {
                    int birthdayMonth = friend.birthday().getMonthValue();
                    return birthdayMonth >= startDate.getMonthValue() && birthdayMonth <= endDate.getMonthValue();
                })
                .collect(Collectors.toList());
        return toGetMonthlyFriendBirthdayListDto(friendsBirthdaysOfPeriod, startDate, endDate);
    }

    @Transactional
    public void updatePersonalSchedule(PersonalScheduleRequest.PatchPersonalScheduleDto patchPersonalScheduleDto,
            Long scheduleId, SecurityUserDetails memberInfo) {
        Schedule schedule = scheduleManageService.getPersonalSchedule(scheduleId);
        scheduleManageService.updatePersonalSchedule(patchPersonalScheduleDto, schedule, memberInfo.getUserId());
    }

    @Transactional(readOnly = true)
    public List<PersonalScheduleResponse.GetFriendMonthlyScheduleDto> getFriendMonthlySchedules(LocalDate startDate, LocalDate endDate,
            Long targetMemberId, SecurityUserDetails memberInfo) {
        friendManageService.checkMemberIsFriend(memberInfo.getUserId(), targetMemberId);
        Member targetMember = memberManageService.getActiveMember(targetMemberId);
        return toGetFriendMonthlyScheduleDtos(
                scheduleManageService.getMemberMonthlySchedules(targetMember, startDate, endDate));
    }

    @Transactional
    public void deleteSchedule(Long scheduleId, SecurityUserDetails memberInfo) {
        Schedule schedule= scheduleManageService.getSchedule(scheduleId);
        scheduleManageService.deleteSchedule(schedule, memberInfo.getUserId());
    }
}
