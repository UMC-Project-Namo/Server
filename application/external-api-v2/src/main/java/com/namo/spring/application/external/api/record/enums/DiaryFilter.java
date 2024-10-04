package com.namo.spring.application.external.api.record.enums;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.record.exception.DiaryException;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.schedule.service.ParticipantService;

public enum DiaryFilter {

    SCHEDULE_NAME {
        @Override
        public List<Participant> apply(ParticipantService participantService, Long memberId, Pageable pageable, String keyword) {
            return participantService.readParticipantHasDiaryByScheduleName(memberId, pageable, keyword);
        }
    },
    DIARY_CONTENT {
        @Override
        public List<Participant> apply(ParticipantService participantService, Long memberId, Pageable pageable, String keyword) {
            return participantService.readParticipantHasDiaryByDiaryContent(memberId, pageable, keyword);
        }
    },
    MEMBER_NICKNAME {
        @Override
        public List<Participant> apply(ParticipantService participantService, Long memberId, Pageable pageable, String keyword) {
            return participantService.readParticipantHasDiaryByMember(memberId, pageable, keyword);
        }
    };

    public abstract List<Participant> apply(ParticipantService participantService, Long memberId, Pageable pageable, String keyword);

    public static DiaryFilter from(String filterType) {
        try {
            return DiaryFilter.valueOf(filterType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new DiaryException(ErrorStatus.NOT_FILTERTYPE_OF_ARCHIVE);
        }
    }
}
