package com.namo.spring.application.external.api.record.converter;

import java.util.List;

import com.namo.spring.db.mysql.domains.record.entity.Activity;
import com.namo.spring.db.mysql.domains.record.entity.ActivityImage;

public class ActivityImageConverter {

    public static ActivityImage toActivityImage(Activity activity, String imageUrl){
        return ActivityImage.builder()
                .activity(activity)
                .imageUrl(imageUrl)
                .imageOrder(0)
                .build();
    }
}
