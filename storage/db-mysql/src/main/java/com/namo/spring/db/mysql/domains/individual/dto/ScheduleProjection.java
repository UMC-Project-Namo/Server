package com.namo.spring.db.mysql.domains.individual.dto;

import com.namo.spring.db.mysql.domains.individual.domain.Image;
import com.namo.spring.db.mysql.domains.individual.domain.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class ScheduleProjection {
    @Getter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class ScheduleDto {
        private Schedule schedule;
        private Long categoryId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class DiaryDto {
        private Schedule schedule;
        private Long categoryId;
        private Long color;
    }

    @AllArgsConstructor
    @Getter
    @Builder
    public static class DiaryByUserDto {
        private Long scheduleId;
        private String contents;
        private List<Image> images;
    }
}
