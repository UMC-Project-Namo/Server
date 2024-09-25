package com.namo.spring.application.external.api.schedule.converter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.namo.spring.application.external.api.schedule.dto.PersonalScheduleResponse;
import com.namo.spring.application.external.global.utils.ReminderTimeUtils;
import com.namo.spring.core.common.utils.DateUtil;
import com.namo.spring.db.mysql.domains.category.entity.Category;
import com.namo.spring.db.mysql.domains.notification.dto.ScheduleNotificationQuery;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.type.Location;
import com.namo.spring.db.mysql.domains.schedule.type.ScheduleType;
import com.namo.spring.db.mysql.domains.user.dto.FriendBirthdayQuery;

public class PersonalScheduleResponseConverter {
    private PersonalScheduleResponseConverter() {
        throw new IllegalStateException("Util Class");
    }

    public static List<PersonalScheduleResponse.GetMonthlyScheduleDto> toGetMonthlyScheduleDtos(
            List<Participant> participants, List<ScheduleNotificationQuery> notifications) {
        Map<Long, List<ScheduleNotificationQuery>> scheduleIdAndNotification = notifications.stream()
                .collect(Collectors.groupingBy(ScheduleNotificationQuery::getScheduleId));

        return participants.stream()
                .map(participant -> {
                    Long scheduleId = participant.getSchedule().getId();
                    List<ScheduleNotificationQuery> notificationList = scheduleIdAndNotification.getOrDefault(
                            scheduleId,
                            null);
                    return toGetMonthlyScheduleDto(participant, notificationList);
                })
                .collect(Collectors.toList());
    }

    public static PersonalScheduleResponse.GetMonthlyScheduleDto toGetMonthlyScheduleDto(Participant participant,
            List<ScheduleNotificationQuery> notifications) {
        boolean isMeetingSchedule = participant.getSchedule().getIsMeetingSchedule();
        return PersonalScheduleResponse.GetMonthlyScheduleDto.builder()
                .scheduleId(participant.getSchedule().getId())
                .title(participant.getScheduleTitle())
                .categoryInfo(toCategoryDto(participant.getCategory()))
                .startDate(DateUtil.toSeconds(participant.getSchedule().getPeriod().getStartDate()))
                .endDate(DateUtil.toSeconds(participant.getSchedule().getPeriod().getEndDate()))
                .interval(participant.getSchedule().getPeriod().getDayInterval())
                .locationInfo(participant.getSchedule().getLocation() != null ?
                        toLocationDto(participant.getSchedule().getLocation()) : null)
                .scheduleType(participant.getSchedule().getScheduleType())
                .hasDiary(participant.isHasDiary())
                .notificationInfo(notifications != null ?
                        toNotificationDtos(notifications, participant.getSchedule().getPeriod().getStartDate()) : null)
                .meetingInfo(isMeetingSchedule ?
                        toMeetingInfoDto(participant.getIsOwner(), participant.getSchedule()) : null)
                .build();
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

    private static List<PersonalScheduleResponse.NotificationDto> toNotificationDtos(
            List<ScheduleNotificationQuery> notifications, LocalDateTime startDate) {
        return notifications.stream()
                .sorted(Comparator.comparing(ScheduleNotificationQuery::getNotifyAt).reversed())
                .map(notificaiton -> toNotificationDto(notificaiton, startDate))
                .collect(Collectors.toList());
    }

    private static PersonalScheduleResponse.NotificationDto toNotificationDto(ScheduleNotificationQuery notification,
            LocalDateTime startDate) {
        return PersonalScheduleResponse.NotificationDto.builder()
                .notificationId(notification.getNotificationId())
                .trigger(ReminderTimeUtils.toReminderTrigger(notification.getNotifyAt(), startDate))
                .build();
    }

    public static List<PersonalScheduleResponse.GetFriendMonthlyScheduleDto> toGetFriendMonthlyScheduleDtos(
            List<Participant> participants) {
        return participants.stream()
                .map(PersonalScheduleResponseConverter::toGetFriendMonthlyScheduleDto)
                .collect(Collectors.toList());
    }

    public static PersonalScheduleResponse.GetFriendMonthlyScheduleDto toGetFriendMonthlyScheduleDto(
            Participant participant) {
        return PersonalScheduleResponse.GetFriendMonthlyScheduleDto.builder()
                .scheduleId(participant.getSchedule().getId())
                .title(participant.getSchedule().getTitle())
                .categoryInfo(toCategoryDto(participant.getCategory()))
                .startDate(DateUtil.toSeconds(participant.getSchedule().getPeriod().getStartDate()))
                .endDate(DateUtil.toSeconds(participant.getSchedule().getPeriod().getEndDate()))
                .interval(participant.getSchedule().getPeriod().getDayInterval())
                .scheduleType(participant.getSchedule().getScheduleType())
                .build();
    }

    public static List<PersonalScheduleResponse.GetMonthlyFriendBirthdayDto> toGetMonthlyFriendBirthdayDtos(List<FriendBirthdayQuery> friendBirthdays, int year){
        return friendBirthdays.stream()
                .map(friendBirthday -> toGetMonthlyFriendBirthdayDto(friendBirthday, year))
                .collect(Collectors.toList());
    }

    public static PersonalScheduleResponse.GetMonthlyFriendBirthdayDto toGetMonthlyFriendBirthdayDto(FriendBirthdayQuery friendBirthday, int year){
        return PersonalScheduleResponse.GetMonthlyFriendBirthdayDto.builder()
                .nickname(friendBirthday.getNickname())
                .birthdayDate(DateUtil.toSeconds(friendBirthday.getBirthday().withYear(year).atStartOfDay()))
                .categoryInfo(null)
                .build();
    }

    private static boolean getParticipantIsOwner(int isOwner) {
        return isOwner == ScheduleType.MEETING.getValue();
    }
}
