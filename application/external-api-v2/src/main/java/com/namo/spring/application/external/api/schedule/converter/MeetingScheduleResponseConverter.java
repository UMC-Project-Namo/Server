package com.namo.spring.application.external.api.schedule.converter;

import com.namo.spring.application.external.api.schedule.dto.MeetingScheduleResponse;
import com.namo.spring.core.common.utils.DateUtil;
import com.namo.spring.db.mysql.domains.schedule.dto.ScheduleParticipantQuery;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.schedule.type.Location;
import com.namo.spring.db.mysql.domains.schedule.type.ParticipantStatus;
import com.namo.spring.db.mysql.domains.schedule.type.ScheduleType;
import com.namo.spring.db.mysql.domains.user.entity.Anonymous;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MeetingScheduleResponseConverter {
    private MeetingScheduleResponseConverter() {
        throw new IllegalStateException("Util Class");
    }

    public static List<MeetingScheduleResponse.GetMeetingScheduleSummaryDto> toGetMeetingScheduleSummaryDtos(
            List<Schedule> schedules) {
        return schedules.stream()
                .map(MeetingScheduleResponseConverter::toGetMeetingScheduleSummaryDto)
                .collect(Collectors.toList());
    }

    public static MeetingScheduleResponse.GetMeetingScheduleSummaryDto toGetMeetingScheduleSummaryDto(Schedule schedule) {
        return MeetingScheduleResponse.GetMeetingScheduleSummaryDto.builder()
                .meetingScheduleId(schedule.getId())
                .title(schedule.getTitle())
                .startDate(DateUtil.toSeconds(schedule.getPeriod().getStartDate()))
                .imageUrl(schedule.getImageUrl())
                .participantCount(schedule.getParticipantCount())
                .participantNicknames(schedule.getParticipantNicknames())
                .build();
    }

    public static List<MeetingScheduleResponse.GetMonthlyMembersScheduleDto> toGetMonthlyParticipantScheduleDtos(
            List<ScheduleParticipantQuery> participantsWithSchedule, List<Long> participantIds, Long ownerId) {
        Map<Long, List<ScheduleParticipantQuery>> scheduleIdAndParticipant
                = participantsWithSchedule.stream()
                // 다른 유저의 일정일 경우 공유 여부로 필터링
                .filter(participant -> {
                    boolean isSharedSchedule = true;
                    if (!participant.getMemberId().equals(ownerId)) isSharedSchedule = participant.getIsShared();
                    return isSharedSchedule;
                })
                .collect(Collectors.groupingBy(participant -> participant.getSchedule().getId()));
        return scheduleIdAndParticipant.entrySet().stream()
                .map(entry -> toGetMonthlyParticipantScheduleDto(entry.getValue().get(0).getSchedule(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public static MeetingScheduleResponse.GetMonthlyMembersScheduleDto toGetMonthlyParticipantScheduleDto(
            Schedule schedule, List<ScheduleParticipantQuery> participant) {
        return MeetingScheduleResponse.GetMonthlyMembersScheduleDto.builder()
                .scheduleId(schedule.getId())
                .name(schedule.getTitle())
                .startDate(DateUtil.toSeconds(schedule.getPeriod().getStartDate()))
                .endDate(DateUtil.toSeconds(schedule.getPeriod().getEndDate()))
                .interval(schedule.getPeriod().getDayInterval())
                .participants(toMemberParticipantDtos(participant))
                .build();
    }

    private static List<MeetingScheduleResponse.MemberParticipantDto> toMemberParticipantDtos(
            List<ScheduleParticipantQuery> participant) {
        return participant.stream()
                .map(MeetingScheduleResponseConverter::toMemberParticipantDto)
                .collect(Collectors.toList());
    }

    private static MeetingScheduleResponse.MemberParticipantDto toMemberParticipantDto(ScheduleParticipantQuery participant) {
        return MeetingScheduleResponse.MemberParticipantDto.builder()
                .userId(participant.getMemberId())
                .nickname(participant.getNickname())
                .build();
    }

    public static List<MeetingScheduleResponse.GetMonthlyMeetingParticipantScheduleDto> toGetMonthlyMeetingParticipantScheduleDtos(
            List<ScheduleParticipantQuery> participantsWithSchedule, Schedule curSchedule) {
        Map<Long, List<ScheduleParticipantQuery>> scheduleIdAndParticipant
                = participantsWithSchedule.stream()
                .collect(Collectors.groupingBy(participant -> participant.getSchedule().getId()));
        return scheduleIdAndParticipant.entrySet().stream()
                .map(entry -> toGetMonthlyMeetingParticipantScheduleDto(entry.getValue().get(0).getSchedule(),
                        entry.getValue(), curSchedule))
                .collect(Collectors.toList());
    }

    public static MeetingScheduleResponse.GetMonthlyMeetingParticipantScheduleDto toGetMonthlyMeetingParticipantScheduleDto(
            Schedule schedule, List<ScheduleParticipantQuery> participant, Schedule curSchedule) {
        return MeetingScheduleResponse.GetMonthlyMeetingParticipantScheduleDto.builder()
                .scheduleId(schedule.getId())
                .name(schedule.getTitle())
                .startDate(DateUtil.toSeconds(schedule.getPeriod().getStartDate()))
                .endDate(DateUtil.toSeconds(schedule.getPeriod().getEndDate()))
                .interval(schedule.getPeriod().getDayInterval())
                .participants(toUserParticipantDtos(participant))
                .isCurMeetingSchedule(schedule.getId().equals(curSchedule.getId()))
                .build();
    }

    private static List<MeetingScheduleResponse.UserParticipantDto> toUserParticipantDtos(
            List<ScheduleParticipantQuery> participant) {
        return participant.stream()
                .map(MeetingScheduleResponseConverter::toUserParticipantDto)
                .collect(Collectors.toList());
    }

    private static MeetingScheduleResponse.UserParticipantDto toUserParticipantDto(ScheduleParticipantQuery
                                                                                           participant) {
        return MeetingScheduleResponse.UserParticipantDto.builder()
                .participantId(participant.getParticipantId())
                .userId(participant.getMemberId())
                .nickname(participant.getNickname())
                .colorId(participant.getParticipantPaletteId())
                .build();
    }

    public static MeetingScheduleResponse.GetMeetingScheduleDto toGetMeetingScheduleDto(Schedule
                                                                                                schedule, List<Participant> participants) {
        return MeetingScheduleResponse.GetMeetingScheduleDto.builder()
                .scheduleId(schedule.getId())
                .name(schedule.getTitle())
                .imageUrl(schedule.getImageUrl())
                .startDate(DateUtil.toSeconds(schedule.getPeriod().getStartDate()))
                .endDate(DateUtil.toSeconds(schedule.getPeriod().getEndDate()))
                .interval(schedule.getPeriod().getDayInterval())
                .locationInfo(schedule.getLocation() != null ? toLocationDto(schedule.getLocation()) : null)
                .participants(toUserParticipantDetailDtos(participants))
                .build();
    }

    private static MeetingScheduleResponse.LocationDto toLocationDto(Location location) {
        return MeetingScheduleResponse.LocationDto.builder()
                .longitude(location.getLongitude())
                .latitude(location.getLatitude())
                .kakaoLocationId(location.getKakaoLocationId())
                .locationName(location.getName())
                .build();
    }

    private static List<MeetingScheduleResponse.UserParticipantDetailDto> toUserParticipantDetailDtos
            (List<Participant> participant) {
        return participant.stream()
                .map(MeetingScheduleResponseConverter::toUserParticipantDetailDto)
                .collect(Collectors.toList());
    }

    private static MeetingScheduleResponse.UserParticipantDetailDto toUserParticipantDetailDto(Participant
                                                                                                       participant) {
        return MeetingScheduleResponse.UserParticipantDetailDto.builder()
                .participantId(participant.getId())
                .userId(participant.getUser().getId())
                .isGuest(participant.getUser() instanceof Anonymous)
                .nickname(participant.getUser().getNickname())
                .colorId(participant.getPalette() != null ? participant.getPalette().getId() : null)
                .isActive(getParticipantIsActive(participant.getStatus()))
                .isOwner(getParticipantIsOwner(participant.getIsOwner()))
                .build();
    }

    private static boolean getParticipantIsOwner(int isOwner) {
        return isOwner == ScheduleType.MEETING.getValue();
    }

    private static boolean getParticipantIsActive(ParticipantStatus status) {
        return status == ParticipantStatus.ACTIVE;
    }
}
