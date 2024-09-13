package com.namo.spring.application.external.api.record.usecase;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.application.external.api.record.converter.DiaryResponseConverter;
import com.namo.spring.application.external.api.record.dto.DiaryRequest;
import com.namo.spring.application.external.api.record.dto.DiaryResponse;
import com.namo.spring.application.external.api.record.serivce.DiaryManageService;
import com.namo.spring.application.external.api.schedule.service.ParticipantManageService;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.db.mysql.domains.record.entity.Diary;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DiaryUseCase {

    private final DiaryManageService diaryManageService;
    private final ParticipantManageService participantManageService;

    @Transactional
    public void createDiary(SecurityUserDetails memberInfo, DiaryRequest.CreateDiaryDto request) {
        Participant participant = participantManageService.getScheduleParticipant(memberInfo.getUserId(),
                request.getScheduleId());
        diaryManageService.makeDiary(request, participant);
    }

    @Transactional
    public void updateDiary(Long diaryId, SecurityUserDetails memberInfo, DiaryRequest.UpdateDiaryDto request) {
        Diary diary = diaryManageService.getMyDiary(diaryId, memberInfo.getUserId());
        diaryManageService.updateDiary(diary, request);
    }

    @Transactional(readOnly = true)
    public DiaryResponse.DiaryDto getScheduleDiary(Long scheduleId, SecurityUserDetails memberInfo) {
        Participant participant = participantManageService.getScheduleParticipant(memberInfo.getUserId(),
                scheduleId);
        Diary diary = diaryManageService.getParticipantDiary(participant);
        return DiaryResponseConverter.toDiaryDto(diary);
    }

    @Transactional(readOnly = true)
    public List<DiaryResponse.DiaryArchiveDto> getDiaryArchive(Long memberId, int page, String filterType,
            String keyword) {
        Pageable pageable = PageRequest.of(page - 1, 5);
        List<Participant> allMyParticipant = participantManageService.getMyParticipationForDiary(memberId, pageable,
                filterType, keyword);
        return allMyParticipant.stream()
                .map(DiaryResponseConverter::toDiaryArchiveDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public DiaryResponse.DiaryExistDateDto getExistDiaryDate(Long memberId, YearMonth yearMonth) {
        List<Participant> participants = participantManageService.getMyParticipantByMonthForDiary(memberId, yearMonth
        );
        return DiaryResponseConverter.toDiaryExistDateDto(participants, yearMonth);
    }

    @Transactional(readOnly = true)
    public List<DiaryResponse.DayOfDiaryDto> getDayDiaryList(Long memberId, LocalDate localDate) {
        List<Participant> participants = participantManageService.getMyParticipantByDayForDiary(memberId, localDate);
        return participants.stream()
                .map(DiaryResponseConverter::toDayOfDiaryDto)
                .toList();
    }

    @Transactional
    public void deleteDiary(Long memberId, Long diaryId) {
        Diary myDiary = diaryManageService.getMyDiary(diaryId, memberId);
        diaryManageService.deleteDiary(myDiary);
    }
}
