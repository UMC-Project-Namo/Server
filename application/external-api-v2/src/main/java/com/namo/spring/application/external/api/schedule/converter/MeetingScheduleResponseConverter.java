package com.namo.spring.application.external.api.schedule.converter;

import com.namo.spring.application.external.api.schedule.dto.MeetingScheduleResponse;
import com.namo.spring.core.common.utils.DateUtil;
import com.namo.spring.db.mysql.domains.schedule.dto.ScheduleParticipantQuery;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.namo.spring.db.mysql.domains.category.type.PaletteEnum.getPaletteColorIds;

public class MeetingScheduleResponseConverter {
    private MeetingScheduleResponseConverter() {
        throw new IllegalStateException("Util Class");
    }

    public static List<MeetingScheduleResponse.GetMeetingScheduleItemDto> toGetMeetingScheduleItemDtos(List<Schedule> schedules) {
        return schedules.stream()
                .map(MeetingScheduleResponseConverter::toGetMeetingScheduleItemDto)
                .collect(Collectors.toList());
    }

    public static MeetingScheduleResponse.GetMeetingScheduleItemDto toGetMeetingScheduleItemDto(Schedule schedule) {
        return MeetingScheduleResponse.GetMeetingScheduleItemDto.builder()
                .meetingScheduleId(schedule.getId())
                .title(schedule.getTitle())
                .startDate(DateUtil.toSeconds(schedule.getPeriod().getStartDate()))
                .imageUrl(schedule.getImageUrl())
                .participantCount(schedule.getParticipantCount())
                .participantNicknames(schedule.getParticipantNicknames())
                .build();
    }

    public static List<MeetingScheduleResponse.GetMonthlyMembersScheduleDto> toGetMonthlyParticipantScheduleDtos(List<ScheduleParticipantQuery> participantsWithSchedule, List<Long> participantIds, Long ownerId) {
        long[] palette = getPaletteColorIds();
        Map<Long, List<ScheduleParticipantQuery>> scheduleIdAndParticipant
                = participantsWithSchedule.stream()
                .collect(Collectors.groupingBy(participant -> participant.getSchedule().getId()));
        Map<Long, Long> participantAndPalette
                = IntStream.range(0, participantIds.size()).boxed()
                .collect(Collectors.toMap(participantIds::get, i -> palette[i + 1]));
        participantAndPalette.put(ownerId, palette[0]);
        return scheduleIdAndParticipant.entrySet().stream()
                .map(entry -> toGetMonthlyParticipantScheduleDto(entry.getValue().get(0).getSchedule(), entry.getValue(), participantAndPalette))
                .collect(Collectors.toList());
    }

    public static MeetingScheduleResponse.GetMonthlyMembersScheduleDto toGetMonthlyParticipantScheduleDto(Schedule schedule, List<ScheduleParticipantQuery> participant, Map<Long, Long> participantAndPalette) {
        return MeetingScheduleResponse.GetMonthlyMembersScheduleDto.builder()
                .scheduleId(schedule.getId())
                .name(schedule.getTitle())
                .startDate(DateUtil.toSeconds(schedule.getPeriod().getStartDate()))
                .endDate(DateUtil.toSeconds(schedule.getPeriod().getEndDate()))
                .interval(schedule.getPeriod().getDayInterval())
                .participants(toMemberParticipantDtos(participant, participantAndPalette))
                .build();
    }

    private static List<MeetingScheduleResponse.MemberParticipantDto> toMemberParticipantDtos(List<ScheduleParticipantQuery> participant, Map<Long, Long> participantAndPalette) {
        return participant.stream()
                .map(p -> toMemberParticipantDto(p, participantAndPalette))
                .collect(Collectors.toList());
    }

    private static MeetingScheduleResponse.MemberParticipantDto toMemberParticipantDto(ScheduleParticipantQuery participant, Map<Long, Long> participantAndPalette) {
        return MeetingScheduleResponse.MemberParticipantDto.builder()
                .memberId(participant.getMemberId())
                .nickname(participant.getNickname())
                .color(participantAndPalette.get(participant.getMemberId()))
                .build();
    }


    public static List<MeetingScheduleResponse.GetMonthlyMeetingParticipantScheduleDto> toGetMonthlyMeetingParticipantScheduleDtos(List<ScheduleParticipantQuery> participantsWithSchedule, Schedule curSchedule) {
        Map<Long, List<ScheduleParticipantQuery>> scheduleIdAndParticipant
                = participantsWithSchedule.stream()
                .collect(Collectors.groupingBy(participant -> participant.getSchedule().getId()));
        return scheduleIdAndParticipant.entrySet().stream()
                .map(entry -> toGetMonthlyMeetingParticipantScheduleDto(entry.getValue().get(0).getSchedule(), entry.getValue(), curSchedule))
                .collect(Collectors.toList());
    }

    public static MeetingScheduleResponse.GetMonthlyMeetingParticipantScheduleDto toGetMonthlyMeetingParticipantScheduleDto(Schedule schedule, List<ScheduleParticipantQuery> participant, Schedule curSchedule) {
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

    private static List<MeetingScheduleResponse.UserParticipantDto> toUserParticipantDtos(List<ScheduleParticipantQuery> participant) {
        return participant.stream()
                .map(MeetingScheduleResponseConverter::toUserParticipantDto)
                .collect(Collectors.toList());
    }

    private static MeetingScheduleResponse.UserParticipantDto toUserParticipantDto(ScheduleParticipantQuery participant) {
        return MeetingScheduleResponse.UserParticipantDto.builder()
                .participantId(participant.getParticipantId())
                .memberId(participant.getMemberId())
                .anonymousId(participant.getAnonymousId())
                .nickname(participant.getNickname())
                .color(participant.getParticipantPaletteId())
                .build();
    }

}
