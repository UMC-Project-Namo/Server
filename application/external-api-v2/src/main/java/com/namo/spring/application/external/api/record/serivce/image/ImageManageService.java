package com.namo.spring.application.external.api.record.serivce.image;

import java.util.List;

public interface ImageManageService<T, R> {
    void updateImages(T entity, R request);
    void deleteImages(T entity);
    void deleteFromCloud(Long imageId);
    void createImages(T entity, List<String> imageUrls);
}
