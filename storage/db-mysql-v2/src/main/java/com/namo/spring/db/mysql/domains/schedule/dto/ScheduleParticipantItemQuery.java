package com.namo.spring.db.mysql.domains.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ScheduleParticipantItemQuery {
    Long scheduleId;
    String title;
    LocalDateTime startDate;
    String imageUrl;
    String participantName;
}
