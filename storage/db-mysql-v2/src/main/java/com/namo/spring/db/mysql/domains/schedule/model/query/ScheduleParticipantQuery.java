package com.namo.spring.db.mysql.domains.schedule.model.query;

import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;

import lombok.AllArgsConstructor;
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
    private Boolean categoryIsShared;
    private Boolean birthdayVisible;
}
