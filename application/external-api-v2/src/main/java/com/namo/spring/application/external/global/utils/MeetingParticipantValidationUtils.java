package com.namo.spring.application.external.global.utils;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.schedule.exception.ScheduleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MeetingParticipantValidationUtils {
    private static final int MIN_PARTICIPANTS = 1;
    private static final int MAX_PARTICIPANTS = 9;

    /**
     * 참석자 인원 수를 검증합니다.
     * 최소 1명 이상 / 최대 9명 이하
     *
     * @param participantNumber
     */
    public static void validateParticipantCount(int participantNumber) {
        if (participantNumber < MIN_PARTICIPANTS || participantNumber > MAX_PARTICIPANTS) {
            throw new ScheduleException(ErrorStatus.INVALID_MEETING_PARTICIPANT_COUNT);
        }
    }

    /**
     * 모임 일정에 초대할 참석자에
     * 모임 일정 생성자가 포함되어있는지,
     * 중복되는 ID가 있는지 검증합니다.
     *
     * @param ownerId        모임 일정 생정자 ID
     * @param participantIds 참석자 ID 배열
     */
    public static void validateUniqueParticipantIds(Long ownerId, List<Long> participantIds) {
        if (participantIds.contains(ownerId)) {
            throw new ScheduleException(ErrorStatus.DUPLICATE_MEETING_PARTICIPANT);
        }
        Set<Long> uniqueIds = new HashSet<>();
        Set<Long> duplicates = participantIds.stream()
                .filter(id -> !uniqueIds.add(id))
                .collect(Collectors.toSet());
        if (!duplicates.isEmpty()) {
            throw new ScheduleException(ErrorStatus.DUPLICATE_MEETING_PARTICIPANT);
        }
    }

}
