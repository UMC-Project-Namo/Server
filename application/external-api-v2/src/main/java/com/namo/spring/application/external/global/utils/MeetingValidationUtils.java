package com.namo.spring.application.external.global.utils;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.schedule.exception.ScheduleException;

import java.util.List;

public class MeetingValidationUtils {
    private MeetingValidationUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final int MIN_PARTICIPANTS = 1;
    private static final int MAX_PARTICIPANTS = 9;

    public static void validateParticipantCount(int participantNumber) {
        if (participantNumber < MIN_PARTICIPANTS || participantNumber > MAX_PARTICIPANTS) {
            throw new ScheduleException(ErrorStatus.MEETING_INVALID_PARTICIPANT_COUNT);
        }
    }

    public static void validateOwnerNotInParticipants(Long ownerId, List<Long> participantIds) {
        if (participantIds.contains(ownerId)) {
            throw new ScheduleException(ErrorStatus.MEETING_DUPLICATE_PARTICIPANT);
        }
    }
}
