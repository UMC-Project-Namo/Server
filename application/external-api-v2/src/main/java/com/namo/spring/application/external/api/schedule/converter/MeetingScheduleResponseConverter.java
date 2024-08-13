package com.namo.spring.application.external.api.schedule.converter;

import com.namo.spring.application.external.api.schedule.dto.MeetingScheduleResponse;
import com.namo.spring.db.mysql.domains.schedule.dto.MeetingScheduleQueryDto;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;

import java.util.List;
import java.util.stream.Collectors;

public class MeetingScheduleResponseConverter {
    private MeetingScheduleResponseConverter() {
        throw new IllegalStateException("Util Class");
    }

    public static List<MeetingScheduleResponse.GetMeetingScheduleDto> toGetMeetingScheduleDtos(List<MeetingScheduleQueryDto> meetingScheduleQueryDtos) {
        return meetingScheduleQueryDtos.stream()
                .map(MeetingScheduleResponseConverter::toGetMeetingScheduleDto)
                .collect(Collectors.toList());
    }

    public static MeetingScheduleResponse.GetMeetingScheduleDto toGetMeetingScheduleDto(MeetingScheduleQueryDto meetingScheduleQueryDto) {
        return MeetingScheduleResponse.GetMeetingScheduleDto.builder()
                .meetingScheduleId(meetingScheduleQueryDto.getMeetingScheduleId())
                .title(meetingScheduleQueryDto.getTitle())
                .participantsNum(meetingScheduleQueryDto.getParticipantNumber())
                .participantsNickname(toParticipantsNickname(meetingScheduleQueryDto.getParticipantList()))
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
