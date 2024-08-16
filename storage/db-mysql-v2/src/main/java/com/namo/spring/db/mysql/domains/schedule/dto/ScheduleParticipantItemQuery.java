package com.namo.spring.db.mysql.domains.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ScheduleParticipantItemQuery {
    Long scheduleId;
    String title;
    String imageUrl;
    String participantName;
}
