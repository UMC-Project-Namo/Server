package com.namo.spring.application.external.api.record.serivce;

import org.springframework.stereotype.Service;

import com.namo.spring.db.mysql.domains.record.entity.Activity;
import com.namo.spring.db.mysql.domains.record.entity.ActivityImage;
import com.namo.spring.db.mysql.domains.record.service.ActivityImageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityImageManageService {

    private final ActivityImageService activityImageService;

    public void createActiveImage(Activity activity, String image) {
        ActivityImage activityImage = ActivityImage.builder()
                .activity(activity)
                .imageUrl(image)
                .imageOrder(0)
                .build();
        activityImageService.createActivityImage(activityImage);
    }
}
