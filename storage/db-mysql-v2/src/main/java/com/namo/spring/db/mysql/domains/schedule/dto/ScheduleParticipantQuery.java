package com.namo.spring.db.mysql.domains.schedule.dto;

import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleParticipantQuery {
    private Long participantId;
    private Long participantPaletteId;
    private Long memberId;
    private String nickname;
    private Schedule schedule;
    private String customTitle;
    private String customImage;
    private Boolean isShared;
    private Boolean birthdayVisible;
}
