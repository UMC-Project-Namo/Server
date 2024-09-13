package com.namo.spring.application.external.api.record.serivce;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.application.external.api.record.dto.DiaryRequest;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.record.entity.Diary;
import com.namo.spring.db.mysql.domains.record.exception.DiaryException;
import com.namo.spring.db.mysql.domains.record.service.DiaryService;
import com.namo.spring.db.mysql.domains.schedule.entity.Participant;
import com.namo.spring.db.mysql.domains.user.exception.MemberException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DiaryManageService {

    private final DiaryService diaryService;
    private final DiaryImageManageService diaryImageManageService;

    /**
     * 나의 일기를 가져오는 메서드입니다.
     * !!다이어리의 존재 여부, 나의 다이어리 여부를 검증합니다.
     *
     * @param diaryId  일기 ID
     * @param memberId 사용자 ID
     * @return Diary
     */
    public Diary getMyDiary(Long diaryId, Long memberId) {
        Diary diary = diaryService.readDiary(diaryId)
                .orElseThrow(() -> new DiaryException(ErrorStatus.NOT_FOUND_DIARY_FAILURE));
        if (!diary.getParticipant().getMember().getId().equals(memberId))
            throw new DiaryException(ErrorStatus.NOT_MY_DIARY_FAILURE);
        return diary;
    }

    /**
     * 참여자의 다이어리를 가져오는 메서드입니다.
     * !! 다이어리가 작성되었는지 여부를 검증합니다.
     *
     * @param participant 참여자
     * @return Diary
     */
    public Diary getParticipantDiary(Participant participant) {
        if (!participant.isHasDiary())
            throw new DiaryException(ErrorStatus.NOT_WRITTEN_DIARY_FAILURE);
        return participant.getDiary();
    }

    /**
     * 다이어리를 만드는 메서드입니다.
     * !! 다이어리가 이미 존재하는지를 검증합니다.
     *
     * @param request
     * @param participant 참여자
     */
    public void makeDiary(DiaryRequest.CreateDiaryDto request, Participant participant) {
        if (participant.isHasDiary())
            throw new MemberException(ErrorStatus.ALREADY_WRITTEN_DIARY_FAILURE);
        Diary diary = Diary.of(participant, request.getContent(), request.getEnjoyRating());
        diaryService.createDiary(diary);
        diaryImageManageService.createDiaryImages(diary, request.getDiaryImages());
    }

    /**
     * 다이어리를 업데이트 하는 메서드입니다.
     * !! 일기 이미지에 대한 업데이트 방식은 DiaryImageManageService가 책임을 가집니다.
     *
     * @param diary   타겟 일기
     * @param request 업데이트 정보
     */
    public void updateDiary(Diary diary, DiaryRequest.UpdateDiaryDto request) {
        diary.update(request.getContent(), request.getEnjoyRating());
        diaryImageManageService.updateDiaryImage(diary, request);
    }

    /**
     * 다이어리를 삭제하는 메서드입니다.
     * !! 클라우드에서 이미지를 삭제후 일기를 삭제합니다.
     *
     * @param diary
     */
    @Transactional
    public void deleteDiary(Diary diary) {
        diary.getImages().forEach(diaryImage -> diaryImageManageService.deleteFromCloud(diaryImage.getId()));
        diaryImageManageService.deleteDiaryImage(diary);
        diaryService.deleteDiary(diary);
    }
}
