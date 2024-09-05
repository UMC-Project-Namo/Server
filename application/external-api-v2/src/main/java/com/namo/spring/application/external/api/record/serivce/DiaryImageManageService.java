package com.namo.spring.application.external.api.record.serivce;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.application.external.api.record.dto.DiaryRequest;
import com.namo.spring.core.infra.common.aws.s3.FileUtils;
import com.namo.spring.core.infra.common.constant.FilePath;
import com.namo.spring.db.mysql.domains.record.entity.Diary;
import com.namo.spring.db.mysql.domains.record.entity.DiaryImage;
import com.namo.spring.db.mysql.domains.record.service.DiaryImageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DiaryImageManageService {

	private final DiaryImageService diaryImageService;
	private final FileUtils fileUtils;

	/**
	 * 일기의 이미지를 업데이트하는 메서드입니다.
	 * !! 현재 기존이미지를 모두 지우고 재생성합니다. 삭제가 지정된 이미지만 클라우드에서 제거합니다.
	 *
	 * @param diary   업데이트할 일기
	 * @param request
	 */
	public void updateDiaryImage(Diary diary, DiaryRequest.UpdateDiaryDto request) {
		request.getDeleteImages().forEach(this::deleteFromCloud);
		diaryImageService.deleteAll(diary);
		createDiaryImages(diary, request.getDiaryImages());
	}

	/**
	 * 클라우드(S3) 에서 이미지를 삭제하는 메서드입니다. (원본, 리사이징 본 모두 삭제)
	 * !! 만약 이미지가 존재하지 않으면 로그만 남기고 Continue 됩니다.
	 * + 새로운 트랜잭션에서 실행되어 클라우드에서 이미지 삭제에 실패해도 우선 삭제 됩니다.
	 *
	 * @param imageId 삭제하려는 Image ID
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	protected void deleteFromCloud(Long imageId) {
		diaryImageService.readDiaryImage(imageId).ifPresentOrElse(diaryImage -> {
			try {
				fileUtils.delete(diaryImage.getImageUrl(), FilePath.DIARY_IMG);
				fileUtils.delete(diaryImage.getImageUrl(), FilePath.RESIZED_DIARY_IMG);
				log.info("클라우드에서 이미지 ID {} <<<<< 삭제 성공", imageId);
			} catch (Exception e) {
				log.error("이미지 URL {} <<<<<<< 삭제 실패: {}", diaryImage.getImageUrl(), e.getMessage());
			}
		}, () -> {
			log.info("이미지 ID {} 가 존재하지 않습니다.", imageId);
		});
	}

	/**
	 * 일기 이미지를 생성하는 메서드입니다.
	 *
	 * @param diary       이미지가 연결될 일기
	 * @param diaryImages
	 */
	public void createDiaryImages(Diary diary, List<DiaryRequest.CreateDiaryImageDto> diaryImages) {
		if (!diaryImages.isEmpty()) {
			diaryImages.forEach(diaryImage -> {
				DiaryImage image = DiaryImage.of(diary, diaryImage.getImageUrl(), diaryImage.getOrderNumber());
				diaryImageService.createDiaryImage(image);
			});
		}
	}
}
