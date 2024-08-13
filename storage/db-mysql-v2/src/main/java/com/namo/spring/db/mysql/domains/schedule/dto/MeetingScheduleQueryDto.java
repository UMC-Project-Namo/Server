package com.namo.spring.db.mysql.domains.schedule.dto;

import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MeetingScheduleQueryDto {
    private Long meetingScheduleId;
    private String title;
    private List<Participant> participantList;
    private Integer participantNumber;
}
