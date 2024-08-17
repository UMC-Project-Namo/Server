package com.namo.spring.application.external.api.schedule.converter;

import com.namo.spring.application.external.api.schedule.dto.MeetingScheduleResponse;
import com.namo.spring.core.common.utils.DateUtil;
import com.namo.spring.db.mysql.domains.schedule.dto.ScheduleParticipantItemQuery;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MeetingScheduleResponseConverter {
    private MeetingScheduleResponseConverter() {
        throw new IllegalStateException("Util Class");
    }

    public static List<MeetingScheduleResponse.GetMeetingScheduleItemDto> toGetMeetingScheduleItemDtos(List<ScheduleParticipantItemQuery> participants) {
        Map<Long, List<ScheduleParticipantItemQuery>> scheduleParticipantsMap = participants.stream()
                .collect(Collectors.groupingBy(
                        ScheduleParticipantItemQuery::getScheduleId,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        return scheduleParticipantsMap.entrySet().stream()
                .map(entry -> toGetMeetingScheduleItemDto(
                        entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public static MeetingScheduleResponse.GetMeetingScheduleItemDto toGetMeetingScheduleItemDto(Long id, List<ScheduleParticipantItemQuery> participants) {
        ScheduleParticipantItemQuery scheduleInfo = participants.get(0);
        return MeetingScheduleResponse.GetMeetingScheduleItemDto.builder()
                .meetingScheduleId(id)
                .title(scheduleInfo.getTitle())
                .startDate(DateUtil.toSeconds(scheduleInfo.getStartDate()))
                .imageUrl(scheduleInfo.getImageUrl())
                .participantsNum(participants.size())
                .participantsNickname(toParticipantsNickname(participants))
                .build();
    }

    private static String toParticipantsNickname(List<ScheduleParticipantItemQuery> participantList) {
        return participantList.stream()
                .map(ScheduleParticipantItemQuery::getParticipantName)
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
