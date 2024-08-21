package com.namo.spring.application.external.api.schedule.converter;

import com.namo.spring.application.external.api.schedule.dto.MeetingScheduleResponse;
import com.namo.spring.core.common.utils.DateUtil;
import com.namo.spring.db.mysql.domains.schedule.dto.ScheduleParticipantQuery;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;

import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    public static List<MeetingScheduleResponse.GetMonthlyParticipantScheduleDto> toGetMonthlyParticipantScheduleDtos(List<ScheduleParticipantQuery> participantsWithSchedule, List<Long> participantIds, Long ownerId) {
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

    public static MeetingScheduleResponse.GetMonthlyParticipantScheduleDto toGetMonthlyParticipantScheduleDto(Schedule schedule, List<ScheduleParticipantQuery> participant, Map<Long, Long> participantAndPalette) {
        return MeetingScheduleResponse.GetMonthlyParticipantScheduleDto.builder()
                .scheduleId(schedule.getId())
                .name(schedule.getTitle())
                .startDate(DateUtil.toSeconds(schedule.getPeriod().getStartDate()))
                .endDate(DateUtil.toSeconds(schedule.getPeriod().getEndDate()))
                .interval(schedule.getPeriod().getDayInterval())
                .participants(toParticipantDtos(participant, participantAndPalette))
                .latitude(schedule.getLocation().getLatitude())
                .longitude(schedule.getLocation().getLongitude())
                .locationName(schedule.getLocation().getName())
                .kakaoLocationId(schedule.getLocation().getKakaoLocationId())
                .build();
    }

    private static List<MeetingScheduleResponse.ParticipantDto> toParticipantDtos(List<ScheduleParticipantQuery> participant) {
        return participant.stream()
                .map(MeetingScheduleResponseConverter::toParticipantDto)
                .collect(Collectors.toList());
    }

    private static List<MeetingScheduleResponse.ParticipantDto> toParticipantDtos(List<ScheduleParticipantQuery> participant, Map<Long, Long> participantAndPalette) {
        return participant.stream()
                .map(p -> toParticipantDto(p, participantAndPalette))
                .collect(Collectors.toList());
    }

    private static MeetingScheduleResponse.ParticipantDto toParticipantDto(ScheduleParticipantQuery participant) {
        return MeetingScheduleResponse.ParticipantDto.builder()
                .memberId(participant.getMemberId())
                .nickname(participant.getNickname())
                .color(participant.getParticipantPaletteId())
                .build();
    }

    private static MeetingScheduleResponse.ParticipantDto toParticipantDto(ScheduleParticipantQuery participant, Map<Long, Long> participantAndPalette) {
        return MeetingScheduleResponse.ParticipantDto.builder()
                .memberId(participant.getMemberId())
                .nickname(participant.getNickname())
                .color(participantAndPalette.get(participant.getMemberId()))
                .build();
    }


    public static List<MeetingScheduleResponse.GetMonthlyMeetingParticipantScheduleDto> toGetMonthlyMeetingParticipantScheduleDtos(List<ScheduleParticipantQuery> participantsWithSchedule, Long curScheduleId) {
        Map<Long, List<ScheduleParticipantQuery>> scheduleIdAndParticipant
                = participantsWithSchedule.stream()
                .collect(Collectors.groupingBy(participant -> participant.getSchedule().getId()));
        return scheduleIdAndParticipant.entrySet().stream()
                .map(entry -> toGetMonthlyMeetingParticipantScheduleDto(entry.getValue().get(0).getSchedule(), entry.getValue(), Objects.equals(entry.getKey(), curScheduleId)))
                .collect(Collectors.toList());
    }

    public static MeetingScheduleResponse.GetMonthlyMeetingParticipantScheduleDto toGetMonthlyMeetingParticipantScheduleDto(Schedule schedule, List<ScheduleParticipantQuery> participant, boolean isCurMeetingSchedule) {
        return MeetingScheduleResponse.GetMonthlyMeetingParticipantScheduleDto.builder()
                .scheduleId(schedule.getId())
                .name(schedule.getTitle())
                .startDate(DateUtil.toSeconds(schedule.getPeriod().getStartDate()))
                .endDate(DateUtil.toSeconds(schedule.getPeriod().getEndDate()))
                .interval(schedule.getPeriod().getDayInterval())
                .participants(toParticipantDtos(participant))
                .isCurMeetingSchedule(isCurMeetingSchedule)
                .latitude(schedule.getLocation().getLatitude())
                .longitude(schedule.getLocation().getLongitude())
                .locationName(schedule.getLocation().getName())
                .kakaoLocationId(schedule.getLocation().getKakaoLocationId())
                .build();
    }

    private static String getParticipantNickName(Participant participant) {
        if (participant.getMember() != null) {
            return participant.getMember().getNickname();
        } else if (participant.getAnonymous() != null) {
            return participant.getAnonymous().getNickname();
        } else {
            return null;
        }
    }

}
