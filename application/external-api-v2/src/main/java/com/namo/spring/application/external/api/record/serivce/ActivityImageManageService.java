package com.namo.spring.application.external.api.record.serivce;

import java.util.List;

import org.springframework.stereotype.Service;

import com.namo.spring.application.external.api.record.converter.ActivityImageConverter;
import com.namo.spring.db.mysql.domains.record.entity.Activity;
import com.namo.spring.db.mysql.domains.record.entity.ActivityImage;
import com.namo.spring.db.mysql.domains.record.service.ActivityImageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityImageManageService {

    private final ActivityImageService activityImageService;

    public void createActiveImages(Activity activity, List<String> imageUrls) {
        List<ActivityImage> activityImages = imageUrls.stream()
                .map(url -> ActivityImageConverter.toActivityImage(activity, url))
                .toList();
        activityImageService.createActivityImages(activityImages);
    }
}
