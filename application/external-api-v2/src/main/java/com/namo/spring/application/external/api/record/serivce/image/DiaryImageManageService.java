package com.namo.spring.application.external.api.record.serivce.image;

import org.springframework.stereotype.Component;

import com.namo.spring.application.external.api.record.dto.DiaryRequest;
import com.namo.spring.core.infra.common.aws.s3.FileUtils;
import com.namo.spring.core.infra.common.constant.FilePath;
import com.namo.spring.db.mysql.domains.record.entity.Diary;
import com.namo.spring.db.mysql.domains.record.entity.DiaryImage;
import com.namo.spring.db.mysql.domains.record.service.DiaryImageService;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Getter
public class DiaryImageManageService extends AbstractImageManageService<Diary, DiaryRequest.UpdateDiaryDto> {

    private final DiaryImageService diaryImageService;

    public DiaryImageManageService(FileUtils fileUtils, DiaryImageService diaryImageService) {
        super(fileUtils);
        this.diaryImageService = diaryImageService;
    }

    @Override
    public void updateImages(Diary diary, DiaryRequest.UpdateDiaryDto request) {
        request.getDeleteImages().forEach(this::deleteFromCloud);
        deleteAllImages(diary);
        createImages(diary, request.getDiaryImages().stream()
                .map(DiaryRequest.CreateDiaryImageDto::getImageUrl)
                .toList());
    }

    @Override
    public void deleteImages(Diary diary) {
        deleteAllImages(diary);
    }

    @Override
    protected void deleteAllImages(Diary diary) {
        diaryImageService.deleteAll(diary);
    }

    @Override
    protected void createImage(Diary diary, String imageUrl) {
        DiaryImage image = DiaryImage.of(diary, imageUrl, 0);
        diaryImageService.createDiaryImage(image);
    }

    @Override
    protected FilePath getOriginalFilePath() {
        return FilePath.DIARY_IMG;
    }

    @Override
    protected String getImageUrl(Long imageId) {
        return diaryImageService.readDiaryImage(imageId)
                .map(DiaryImage::getImageUrl)
                .orElse(null);
    }
}
