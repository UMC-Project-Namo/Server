package com.namo.spring.application.external.api.record.serivce.image;

import java.util.List;

import org.springframework.stereotype.Service;

import com.namo.spring.application.external.api.record.converter.ActivityImageConverter;
import com.namo.spring.application.external.api.record.dto.ActivityRequest;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.infra.common.aws.s3.FileUtils;
import com.namo.spring.core.infra.common.constant.FilePath;
import com.namo.spring.db.mysql.domains.record.entity.Activity;
import com.namo.spring.db.mysql.domains.record.entity.ActivityImage;
import com.namo.spring.db.mysql.domains.record.exception.ActivityImageException;
import com.namo.spring.db.mysql.domains.record.service.ActivityImageService;
import com.namo.spring.db.redis.util.RedisUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ActivityImageManageService extends AbstractImageManageService<Activity, ActivityRequest.UpdateActivityDto> {

    private final ActivityImageService activityImageService;

    public ActivityImageManageService(FileUtils fileUtils, RedisUtil redisUtil, ActivityImageService activityImageService) {
        super(fileUtils, redisUtil);
        this.activityImageService = activityImageService;
    }

    @Override
    public void updateImages(Activity activity, ActivityRequest.UpdateActivityDto request) {
        request.getDeleteImages().forEach(this::deleteFromCloud);
        deleteAllImages(activity);
        createImages(activity, request.getImageList());
    }

    @Override
    public void deleteImages(Activity activity) {
        deleteAllImages(activity);
    }

    @Override
    protected void deleteAllImages(Activity activity) {
        activityImageService.deleteActivityImages(activity.getActivityImages());
    }

    @Override
    protected Long createImage(Activity activity, String imageUrl) {
        ActivityImage image = ActivityImageConverter.toActivityImage(activity, imageUrl);
        activityImageService.createActivityImages(List.of(image));
        return image.getId();
    }

    @Override
    protected FilePath getOriginalFilePath() {
        return FilePath.ACTIVITY_IMG;
    }

    @Override
    protected String getImageUrl(Long imageId) {
        return activityImageService.readActivityImage(imageId)
                .map(ActivityImage::getImageUrl)
                .orElse(null);
    }

    public void updateImageUrl(Long imageId, String resizedUrl) {
        ActivityImage activityImage = activityImageService.readActivityImage(imageId)
                .orElseThrow(() -> new ActivityImageException(ErrorStatus.NOT_FOUND_ACTIVITY_IMG_FAILURE));
        activityImage.updateImageUrl(resizedUrl);

    }
}
