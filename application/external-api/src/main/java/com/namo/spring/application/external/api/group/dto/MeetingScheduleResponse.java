package com.namo.spring.application.external.api.group.dto;

import com.namo.spring.core.common.utils.DateUtil;
import com.namo.spring.db.mysql.domains.group.domain.MoimSchedule;
import com.namo.spring.db.mysql.domains.individual.domain.Schedule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class MeetingScheduleResponse {
    private MeetingScheduleResponse() {
        throw new IllegalStateException("Util Class");
    }

    @AllArgsConstructor
    @Getter
    @Builder
    @Schema(title = "[네임 규칙 적용] 월간 모임 일정 조회 응답 DTO")
    public static class GetMonthlyMeetingScheduleDto {
        @Schema(description = "모임 일정 이름", example = "나모 정기 회의")
        private String name;
        @Schema(description = "일정 시작일, unix 타임스탬프 형식")
        private Long startDate;
        @Schema(description = "일정 종료일, unix 타임스탬프 형식")
        private Long endDate;
        @Schema(description = "시작일과 종료일 차이")
        private Integer interval;
        @Schema(description = "모임 일정 참여 유저 목록")
        private List<MeetingScheduleResponse.MeetingScheduleUserDto> users = new ArrayList<>();
        @Schema(description = "그룹 ID")
        private Long groupId;
        @Schema(description = "모임 일정 ID")
        private Long meetingScheduleId;
        @Schema(description = "현재 조회하는 그룹의 모임 일정인지의 여부")
        private boolean isCurMeetingSchedule = false;
        @Schema(description = "장소 위치 경도")
        private Double x;
        @Schema(description = "장소 위치 위도")
        private Double y;
        @Schema(description = "장소 이름", name = "스타벅스 강남역점")
        private String locationName;
        @Schema(description = "장소 카카오 맵 ID")
        private String kakaoLocationId;
        @Schema(description = "모임 일정에 대한 기록이 존재하는지 여부")
        private boolean hasDiaryPlace;

        public static MeetingScheduleResponse.GetMonthlyMeetingScheduleDto fromSchedule(Schedule schedule, List<MeetingScheduleResponse.MeetingScheduleUserDto> MeetingScheduleUserDtos) {
            return MeetingScheduleResponse.GetMonthlyMeetingScheduleDto.builder()
                    .name(schedule.getName())
                    .startDate(DateUtil.toSeconds(schedule.getPeriod().getStartDate()))
                    .endDate(DateUtil.toSeconds(schedule.getPeriod().getEndDate()))
                    .interval(schedule.getPeriod().getDayInterval())
                    .users(MeetingScheduleUserDtos)
                    .hasDiaryPlace(false)
                    .build();
        }

        public static MeetingScheduleResponse.GetMonthlyMeetingScheduleDto fromMoimSchedule(MoimSchedule moimSchedule,
                                                                                            List<MeetingScheduleResponse.MeetingScheduleUserDto> MeetingScheduleUserDtos, boolean isCurMoimSchedule) {
            return GetMonthlyMeetingScheduleDto.builder()
                    .name(moimSchedule.getName())
                    .startDate(DateUtil.toSeconds(moimSchedule.getPeriod().getStartDate()))
                    .endDate(DateUtil.toSeconds(moimSchedule.getPeriod().getEndDate()))
                    .interval(moimSchedule.getPeriod().getDayInterval())
                    .groupId(moimSchedule.getMoim().getId())
                    .meetingScheduleId(moimSchedule.getId())
                    .x(moimSchedule.getLocation().getX())
                    .y(moimSchedule.getLocation().getY())
                    .locationName(moimSchedule.getLocation().getLocationName())
                    .kakaoLocationId(moimSchedule.getLocation().getKakaoLocationId())
                    .users(MeetingScheduleUserDtos)
                    .isCurMeetingSchedule(isCurMoimSchedule)
                    .hasDiaryPlace(moimSchedule.getMoimMemo() != null)
                    .build();
        }
    }

    @AllArgsConstructor
    @Getter
    @Builder
    @Schema(title = "[네임 규칙 적용] 모든 모임 일정 조회 응답 DTO")
    public static class GetAllMeetingScheduleDto {
        @Schema(description = "모임 일정 이름", example = "나모 정기 회의")
        private String name;
        @Schema(description = "일정 시작일, unix 타임스탬프 형식")
        private Long startDate;
        @Schema(description = "일정 종료일, unix 타임스탬프 형식")
        private Long endDate;
        @Schema(description = "시작일과 종료일 차이")
        private Integer interval;
        @Schema(description = "모임 일정 참여 유저 목록")
        private List<MeetingScheduleResponse.MeetingScheduleUserDto> users = new ArrayList<>();
        @Schema(description = "그룹 ID")
        private Long groupId;
        @Schema(description = "모임 일정 ID")
        private Long moimScheduleId;
        @Schema(description = "현재 조회하는 그룹의 모임 일정인지의 여부")
        private boolean isCurMoimSchedule = false;
        @Schema(description = "장소 위치 경도")
        private Double x;
        @Schema(description = "장소 위치 위도")
        private Double y;
        @Schema(description = "장소 이름", name = "스타벅스 강남역점")
        private String locationName;
        @Schema(description = "장소 카카오 맵 ID")
        private String kakaoLocationId;
        @Schema(description = "모임 일정에 대한 기록이 존재하는지 여부")
        private boolean hasDiaryPlace;

        public static MeetingScheduleResponse.GetAllMeetingScheduleDto fromSchedule(Schedule schedule, List<MeetingScheduleResponse.MeetingScheduleUserDto> MeetingScheduleUserDtos) {
            return MeetingScheduleResponse.GetAllMeetingScheduleDto.builder()
                    .name(schedule.getName())
                    .startDate(DateUtil.toSeconds(schedule.getPeriod().getStartDate()))
                    .endDate(DateUtil.toSeconds(schedule.getPeriod().getEndDate()))
                    .interval(schedule.getPeriod().getDayInterval())
                    .users(MeetingScheduleUserDtos)
                    .hasDiaryPlace(false)
                    .build();
        }

        public static MeetingScheduleResponse.GetAllMeetingScheduleDto fromMoimSchedule(MoimSchedule moimSchedule,
                                                                                        List<MeetingScheduleResponse.MeetingScheduleUserDto> MeetingScheduleUserDtos, boolean isCurMoimSchedule) {
            return MeetingScheduleResponse.GetAllMeetingScheduleDto.builder()
                    .name(moimSchedule.getName())
                    .startDate(DateUtil.toSeconds(moimSchedule.getPeriod().getStartDate()))
                    .endDate(DateUtil.toSeconds(moimSchedule.getPeriod().getEndDate()))
                    .interval(moimSchedule.getPeriod().getDayInterval())
                    .groupId(moimSchedule.getMoim().getId())
                    .moimScheduleId(moimSchedule.getId())
                    .x(moimSchedule.getLocation().getX())
                    .y(moimSchedule.getLocation().getY())
                    .locationName(moimSchedule.getLocation().getLocationName())
                    .kakaoLocationId(moimSchedule.getLocation().getKakaoLocationId())
                    .users(MeetingScheduleUserDtos)
                    .isCurMoimSchedule(isCurMoimSchedule)
                    .hasDiaryPlace(moimSchedule.getMoimMemo() != null)
                    .build();
        }
    }

    @AllArgsConstructor
    @Getter
    @Builder
    @Schema(title = "[네임 규칙 적용] 모임 일정 조회 - 모임 일정의 참여자 목록")
    public static class MeetingScheduleUserDto {
        @Schema(description = "유저 ID")
        private Long userId;
        @Schema(description = "유저 이름")
        private String userName;
        @Schema(description = "유저 색상")
        private Integer color;
    }

}
