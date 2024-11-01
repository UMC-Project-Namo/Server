package com.namo.spring.db.mysql.domains.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleSummaryQuery {
    private Long meetingScheduleId;
    private String title;
    private LocalDateTime startDate;
    private String imageUrl;
    private Integer participantCount;
    private String participantNicknames;
    private boolean hasDiary;
    private Long activityId;
}
