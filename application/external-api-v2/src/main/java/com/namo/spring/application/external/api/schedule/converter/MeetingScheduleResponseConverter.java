package com.namo.spring.application.external.api.schedule.converter;

import com.namo.spring.application.external.api.schedule.dto.MeetingScheduleResponse;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MeetingScheduleResponseConverter {
    private MeetingScheduleResponseConverter() {
        throw new IllegalStateException("Util Class");
    }

    public static List<MeetingScheduleResponse.GetMeetingScheduleItemDto> toGetMeetingScheduleItemDtos(List<Participant> participants) {
        Map<Schedule, List<Participant>> scheduleParticipantsMap = participants.stream()
                .collect(Collectors.groupingBy(
                        Participant::getSchedule,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        return scheduleParticipantsMap.entrySet().stream()
                .map(map -> toGetMeetingScheduleItemDto(map.getKey(), map.getValue()))
                .collect(Collectors.toList());
    }

    public static MeetingScheduleResponse.GetMeetingScheduleItemDto toGetMeetingScheduleItemDto(Schedule schedule, List<Participant> participants) {
        return MeetingScheduleResponse.GetMeetingScheduleItemDto.builder()
                .meetingScheduleId(schedule.getId())
                .title(schedule.getTitle())
                .participantsNum(participants.size())
                .participantsNickname(toParticipantsNickname(participants))
                .build();
    }

    private static String toParticipantsNickname(List<Participant> participantList) {
        return participantList.stream()
                .map(MeetingScheduleResponseConverter::getParticipantNickName)
                .collect(Collectors.joining(", "));
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
