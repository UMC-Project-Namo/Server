package com.namo.spring.application.external.global.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.namo.spring.application.external.api.schedule.dto.MeetingScheduleRequest;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.schedule.exception.ParticipantException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MeetingParticipantValidationUtils {
    private static final int MIN_PARTICIPANTS = 1;
    private static final int MAX_PARTICIPANTS = 9;

    /**
     * 참석자 인원 수를 검증합니다.
     * 최소 1명 이상 / 최대 9명 이하
     *
     * @param participantCount
     */
    public static void validateParticipantCount(int participantCount) {
        if (participantCount < MIN_PARTICIPANTS || participantCount > MAX_PARTICIPANTS) {
            throw new ParticipantException(ErrorStatus.INVALID_MEETING_PARTICIPANT_COUNT);
        }
    }

    /**
     * 모임 일정에 초대할 참석자에
     * 모임 일정 생성자가 포함되어있는지,
     * 또는 중복되는 ID가 있는지 검증합니다.
     *
     * @param ownerId        모임 일정 생정자 ID
     * @param participantIds 참석자 ID 배열
     */
    public static void validateUniqueParticipantIds(Long ownerId, List<Long> participantIds) {
        if (participantIds.contains(ownerId)) {
            throw new ParticipantException(ErrorStatus.NOT_INCLUDE_OWNER_IN_REQUEST);
        }
        validateDuplicateParticipantIds(participantIds);
    }

    /**
     * 모임 일정에 초대 / 삭제할 유저에
     * 모임 일정 생성자가 포함되어있는지,
     * 중복되는 ID가 있는지 검증합니다.
     *
     * @param ownerId 모임 일정 생정자 ID
     * @param dto     참석자 ID 배열
     */
    public static void validateUniqueParticipantIds(Long ownerId, MeetingScheduleRequest.PatchMeetingScheduleDto dto) {
        List<Long> participantIds = new ArrayList<>();
        participantIds.addAll(dto.getParticipantsToAdd());
        participantIds.addAll(dto.getParticipantsToRemove());
        validateUniqueParticipantIds(ownerId, participantIds);
    }

    /**
     * 기존에 초대된 참석자와 새로 초대할 참석자에
     * 중복되는 참석자가 있는지 검증합니다.
     *
     * @param existingParticipantIds 기존에 초대된 참석자 ID 배열
     * @param newParticipantIds      새로 초대할 참석자 ID 배열
     */

    public static void validateExistingAndNewParticipantIds(List<Long> existingParticipantIds,
            List<Long> newParticipantIds) {
        List<Long> duplicates = existingParticipantIds.stream()
                .filter(newParticipantIds::contains)
                .collect(Collectors.toList());
        if (!duplicates.isEmpty()) {
            throw new ParticipantException(ErrorStatus.DUPLICATE_MEETING_PARTICIPANT);
        }
    }

    /**
     * 참여자 정보에 중복되는 ID가 있는지 검증합니다.
     *
     * @param participantIds
     */
    public static void validateDuplicateParticipantIds(List<Long> participantIds) {
        Set<Long> uniqueIds = new HashSet<>();
        Set<Long> duplicates = participantIds.stream()
                .filter(id -> !uniqueIds.add(id))
                .collect(Collectors.toSet());
        if (!duplicates.isEmpty()) {
            throw new ParticipantException(ErrorStatus.DUPLICATE_MEETING_PARTICIPANT);
        }
    }

}
