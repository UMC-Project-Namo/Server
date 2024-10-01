package com.namo.spring.application.external.global.utils;


import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

import com.namo.spring.db.mysql.domains.record.entity.Activity;
import com.namo.spring.db.mysql.domains.record.entity.ActivityParticipant;
import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;
import com.namo.spring.db.mysql.domains.user.entity.User;

public class SettlementCalculator {

    /**
     * 스케줄의 전체 활동의 총 금액 계산하는 메서드입니다.
     * @param schedule
     * @return
     */
    public static BigDecimal calculateTotalAmount(Schedule schedule) {
        return schedule.getActivityList().stream()
                .map(Activity::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 사용자 별로 스케줄의 모든 활동의 총 금액을 계산하는 메서드 입니다.
     * @param schedule
     * @return
     */
    public static Map<String, BigDecimal> calculateMemberAmounts(Schedule schedule) {
        return schedule.getActivityList().stream()
                .flatMap(activity -> activity.getActivityParticipants().stream())
                .collect(Collectors.groupingBy(
                        activityParticipant -> {
                            User user = activityParticipant.getParticipant().getUser();
                            return user.getNickname() + "#" + user.getTag();
                        },
                        Collectors.reducing(BigDecimal.ZERO, ActivityParticipant::getAmount, BigDecimal::add)
                ));

    }
}
