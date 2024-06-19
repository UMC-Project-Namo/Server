package com.namo.spring.db.mysql.domains.individual.dto;

import com.namo.spring.db.mysql.domains.individual.domain.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class DiaryResponse {
    @AllArgsConstructor
    @Getter
    @Builder
    public static class GetDiaryByUserDto {
        private Long scheduleId;
        private String contents;
        private List<Image> images;
    }
}
