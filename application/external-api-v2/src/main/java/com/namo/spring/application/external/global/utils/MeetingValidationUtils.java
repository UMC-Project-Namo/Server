package com.namo.spring.application.external.global.utils;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.schedule.exception.ScheduleException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MeetingValidationUtils {
    private MeetingValidationUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final int MIN_PARTICIPANTS = 1;
    private static final int MAX_PARTICIPANTS = 9;

    public static void validateParticipantCount(int participantNumber) {
        if (participantNumber < MIN_PARTICIPANTS || participantNumber > MAX_PARTICIPANTS) {
            throw new ScheduleException(ErrorStatus.INVALID_MEETING_PARTICIPANT_COUNT);
        }
    }

    public static void validateParticipantIds(Long ownerId, List<Long> participantIds) {
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
