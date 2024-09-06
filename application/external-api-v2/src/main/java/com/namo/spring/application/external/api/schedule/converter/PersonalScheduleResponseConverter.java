package com.namo.spring.application.external.api.schedule.converter;

import com.namo.spring.application.external.api.schedule.dto.PersonalScheduleResponse;
import com.namo.spring.application.external.global.utils.ReminderTimeUtils;
import com.namo.spring.core.common.utils.DateUtil;
import com.namo.spring.db.mysql.domains.category.entity.Category;
import com.namo.spring.db.mysql.domains.notification.dto.ScheduleNotificationQuery;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.type.Location;
import com.namo.spring.db.mysql.domains.schedule.type.ScheduleType;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PersonalScheduleResponseConverter {
    private PersonalScheduleResponseConverter() {
        throw new IllegalStateException("Util Class");
    }

    public static List<PersonalScheduleResponse.GetMonthlyScheduleDto> toGetMonthlyScheduleDtos(List<Participant> participants, List<ScheduleNotificationQuery> notifications) {
        Map<Long, List<ScheduleNotificationQuery>> scheduleIdAndNotification = notifications.stream()
                .collect(Collectors.groupingBy(ScheduleNotificationQuery::getScheduleId));

        return participants.stream()
                .map(participant -> {
                    Long scheduleId = participant.getSchedule().getId();
                    List<ScheduleNotificationQuery> notificationList = scheduleIdAndNotification.getOrDefault(scheduleId, null);
                    return toGetMonthlyScheduleDto(participant, notificationList);
                })
                .collect(Collectors.toList());
    }

    public static PersonalScheduleResponse.GetMonthlyScheduleDto toGetMonthlyScheduleDto(Participant participant, List<ScheduleNotificationQuery> notifications) {
        PersonalScheduleResponse.GetMonthlyScheduleDto.GetMonthlyScheduleDtoBuilder builder = PersonalScheduleResponse.GetMonthlyScheduleDto.builder()
                .scheduleId(participant.getSchedule().getId())
                .title(participant.getSchedule().getTitle())
                .categoryInfo(toCategoryDto(participant.getCategory()))
                .startDate(DateUtil.toSeconds(participant.getSchedule().getPeriod().getStartDate()))
                .endDate(DateUtil.toSeconds(participant.getSchedule().getPeriod().getEndDate()))
                .interval(participant.getSchedule().getPeriod().getDayInterval())
                .locationInfo(participant.getSchedule().getLocation() != null ? toLocationDto(participant.getSchedule().getLocation()) : null)
                .isMeetingSchedule(getIsMeetingSchedule(participant.getSchedule().getScheduleType()))
                .hasDiary(participant.isHasDiary())
                .notification(notifications != null ? toNotificationDtos(notifications, participant.getSchedule().getPeriod().getStartDate()) : null);
        if (getIsMeetingSchedule(participant.getSchedule().getScheduleType())) {
            return builder
                    .meetingInfo(toMeetingInfoDto(participant.getIsOwner(), participant.getSchedule()))
                    .build();
        } else return builder.build();
    }

    private static PersonalScheduleResponse.CategoryDto toCategoryDto(Category category) {
        return PersonalScheduleResponse.CategoryDto.builder()
                .categoryId(category.getId())
                .colorId(category.getPalette().getId())
                .name(category.getName())
                .isShared(category.isShared())
                .build();
    }

    private static PersonalScheduleResponse.LocationDto toLocationDto(Location location) {
        return PersonalScheduleResponse.LocationDto.builder()
                .longitude(location.getLongitude())
                .latitude(location.getLatitude())
                .kakaoLocationId(location.getKakaoLocationId())
                .locationName(location.getName())
                .build();
    }

    private static PersonalScheduleResponse.MeetingInfoDto toMeetingInfoDto(Integer isOwner, Schedule schedule) {
        return PersonalScheduleResponse.MeetingInfoDto.builder()
                .participantNicknames(schedule.getParticipantNicknames())
                .participantCount(schedule.getParticipantCount())
                .isOwner(getParticipantIsOwner(isOwner))
                .build();
    }

    private static List<PersonalScheduleResponse.NotificationDto> toNotificationDtos(List<ScheduleNotificationQuery> notifications, LocalDateTime startDate) {
        return notifications.stream()
                .sorted(Comparator.comparing(ScheduleNotificationQuery::getNotifyAt).reversed())
                .map(notificaiton -> toNotificationDto(notificaiton, startDate))
                .collect(Collectors.toList());
    }

    private static PersonalScheduleResponse.NotificationDto toNotificationDto(ScheduleNotificationQuery notification, LocalDateTime startDate) {
        return PersonalScheduleResponse.NotificationDto.builder()
                .notificationId(notification.getNotificationId())
                .trigger(ReminderTimeUtils.toReminderTrigger(notification.getNotifyAt(), startDate))
                .build();
    }

    public static List<PersonalScheduleResponse.GetFriendMonthlyScheduleDto> toGetFriendMonthlyScheduleDtos(List<Participant> participants) {
        return participants.stream()
                .map(PersonalScheduleResponseConverter::toGetFriendMonthlyScheduleDto)
                .collect(Collectors.toList());
    }

    public static PersonalScheduleResponse.GetFriendMonthlyScheduleDto toGetFriendMonthlyScheduleDto(Participant participant) {
        return PersonalScheduleResponse.GetFriendMonthlyScheduleDto.builder()
                .scheduleId(participant.getSchedule().getId())
                .title(participant.getSchedule().getTitle())
                .categoryInfo(toCategoryDto(participant.getCategory()))
                .startDate(DateUtil.toSeconds(participant.getSchedule().getPeriod().getStartDate()))
                .endDate(DateUtil.toSeconds(participant.getSchedule().getPeriod().getEndDate()))
                .interval(participant.getSchedule().getPeriod().getDayInterval())
                .build();
    }

    private static boolean getIsMeetingSchedule(int type) {
        return type == ScheduleType.MEETING.getValue();
    }

    private static boolean getParticipantIsOwner(int isOwner) {
        return isOwner == ScheduleType.MEETING.getValue();
    }
}
