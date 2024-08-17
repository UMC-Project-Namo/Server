package com.namo.spring.application.external.global.utils;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.schedule.exception.ScheduleException;

public class MeetingValidationUtils {
    private MeetingValidationUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final int MIN_PARTICIPANTS = 1;
    private static final int MAX_PARTICIPANTS = 9;

    public static void validateParticipantNumber(int participantNumber) {
        if (!(participantNumber < MIN_PARTICIPANTS || participantNumber > MAX_PARTICIPANTS)) {
            throw new ScheduleException(ErrorStatus.MEETING_INVALID_PARTICIPANT_NUMBER);
        }
    }
}
