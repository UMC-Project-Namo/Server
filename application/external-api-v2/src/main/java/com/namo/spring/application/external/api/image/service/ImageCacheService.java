package com.namo.spring.application.external.api.image.service;

import org.springframework.stereotype.Component;

import com.namo.spring.application.external.api.image.util.ImageKeyParser;
import com.namo.spring.application.external.api.record.serivce.image.ActivityImageManageService;
import com.namo.spring.application.external.api.record.serivce.image.DiaryImageManageService;
import com.namo.spring.core.infra.common.constant.FilePath;
import com.namo.spring.db.redis.util.RedisUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageCacheService {

    private final ActivityImageManageService activityImageManageService;
    private final DiaryImageManageService diaryImageManageService;
    private final RedisUtil redisUtil;

    public void convertToResizedImage(String redisKey, ImageKeyParser.ParsedKey parsedKey) {
        try {
            String originalUrl = redisUtil.get(redisKey);
            if (originalUrl != null) {
                String resizedUrl = convertResizedUrl(parsedKey.getEntityType(), originalUrl);
                updateImageUrl(parsedKey.getEntityType(), parsedKey.getImageId(), resizedUrl);
                log.info("이미지 주소 변환 성공 : Redis Key = {}, Resized URL = {}", redisKey, resizedUrl);
            }
        } catch (Exception e) {
            log.error("이미지 주소 변환에 실패했습니다 : Redis Key = {}, Error = {}", redisKey, e.getMessage());
        }
    }

    /**
     * 각 이미지 엔티티를 찾아 URL 업데이트를 매니저에게 위임하여 실행합니다.
     */
    public void updateImageUrl(String entityType, Long imageId, String resizedUrl) {
        switch (entityType) {
            case "activity":
                activityImageManageService.updateImageUrl(imageId, resizedUrl);
                break;
            case "diary":
                diaryImageManageService.updateImageUrl(imageId, resizedUrl);
                break;
            default:
                throw new IllegalArgumentException("지원되지 않는 이미지 엔티티 타입입니다 : " + entityType);
        }
    }

    /**
     * 원본 이미지를 리사이징 주소로 변환하여 반환하는 메서드입니다.
     * @param entityType 이미지 엔티티
     * @param originalUrl 원본 이미지 URL
     * @return
     */
    public String convertResizedUrl(String entityType, String originalUrl) {
        FilePath originalPath = findOriginalFilePath(entityType);
        FilePath resizedPath = findResizedFilePath(entityType);

        return originalUrl.replace(originalPath.getPath(), resizedPath.getPath());
    }

    private FilePath findOriginalFilePath(String entityType) {
        return switch (entityType) {
            case "activity" -> FilePath.ACTIVITY_IMG;
            case "diary" -> FilePath.DIARY_IMG;
            default -> throw new IllegalArgumentException("지원되지 않는 이미지 엔티티 타입입니다 : " + entityType);
        };
    }

    private FilePath findResizedFilePath(String entityType) {
        return switch (entityType) {
            case "activity" -> FilePath.RESIZED_ACTIVITY_IMG;
            case "diary" -> FilePath.RESIZED_DIARY_IMG;
            default -> throw new IllegalArgumentException("지원되지 않는 이미지 엔티티 타입입니다 : " + entityType);
        };
    }
}
