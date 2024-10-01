package com.namo.spring.application.external.api.schedule.converter;


import static com.namo.spring.db.mysql.domains.schedule.service.SettlementCalculator.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.namo.spring.application.external.api.schedule.dto.MeetingScheduleResponse;
import com.namo.spring.application.external.api.schedule.dto.ScheduleResponse;
import com.namo.spring.db.mysql.domains.category.entity.Category;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.user.entity.Anonymous;
import com.namo.spring.db.mysql.domains.user.entity.User;

public class ScheduleResponseConverter {
    public static ScheduleResponse.ScheduleSummaryDto toScheduleSummaryDto(Participant participant) {
        Schedule schedule = participant.getSchedule();
        return ScheduleResponse.ScheduleSummaryDto.builder()
                .scheduleId(schedule.getId())
                .scheduleTitle(schedule.getTitle())
                .scheduleStartDate(schedule.getPeriod().getStartDate())
                .locationInfo(LocationConverter.toLocationInfoDto(schedule.getLocation()))
                .categoryInfo(toCategoryInfoDto(participant.getCategory()))
                .participantCount(schedule.getParticipantCount())
                .participantInfo(schedule.getParticipantList().stream()
                        .map(ScheduleResponseConverter::toParticipantInfoDto)
                        .toList())
                .hasDiary(participant.isHasDiary())
                .build();
    }

    private static ScheduleResponse.CategoryInfoDto toCategoryInfoDto(Category category) {
        return ScheduleResponse.CategoryInfoDto.builder()
                .name(category.getName())
                .colorId(category.getPalette().getId())
                .build();
    }
    private static ScheduleResponse.ParticipantInfoDto toParticipantInfoDto(Participant participant){
        User user = participant.getUser();
        return ScheduleResponse.ParticipantInfoDto.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .isGuest(user instanceof Anonymous)
                .build();
    }

    public static MeetingScheduleResponse.ScheduleSettlementDto toScheduleSettlementDto(Schedule schedule) {
        BigDecimal totalAmount = calculateTotalAmount(schedule);
        Map<String, BigDecimal> participantAmounts = calculateMemberAmounts(schedule);

        // 가져온 데이터로 DTO 생성
        List<MeetingScheduleResponse.SettlementUserDto> settlementUserList = participantAmounts.entrySet().stream()
                .map(entry -> toSettlementUserDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return MeetingScheduleResponse.ScheduleSettlementDto.builder()
                .totalAmount(totalAmount)
                .settlementUserList(settlementUserList)
                .build();
    }

    // 개별 참여자 정산 정보를 위한 DTO 생성
    public static MeetingScheduleResponse.SettlementUserDto toSettlementUserDto(String nickname, BigDecimal amount) {
        return MeetingScheduleResponse.SettlementUserDto.builder()
                .nickname(nickname)
                .amount(amount)
                .build();
    }
}
