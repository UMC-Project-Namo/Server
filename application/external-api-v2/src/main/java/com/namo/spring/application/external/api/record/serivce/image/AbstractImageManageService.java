package com.namo.spring.application.external.api.record.serivce.image;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.core.infra.common.aws.s3.FileUtils;
import com.namo.spring.core.infra.common.constant.FilePath;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractImageManageService<T, R> implements ImageManageService<T, R> {

    protected final FileUtils fileUtils;

    protected abstract void deleteAllImages(T entity);
    protected abstract void createImage(T entity, String imageUrl);
    protected abstract FilePath getOriginalFilePath();
    protected abstract String getImageUrl(Long imageId);

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteFromCloud(Long imageId) {
        String imageUrl = getImageUrl(imageId);
        if (imageUrl != null) {
            try {
                fileUtils.delete(imageUrl, getOriginalFilePath());
                log.info("클라우드에서 이미지 ID {} <<<<< 삭제 성공", imageId);
            } catch (Exception e) {
                log.error("이미지 URL {} <<<<<<< 삭제 실패: {}", imageUrl, e.getMessage());
            }
        } else {
            log.info("이미지 ID {} 가 존재하지 않습니다.", imageId);
        }
    }

    @Override
    public void createImages(T entity, List<String> imageUrls) {
        imageUrls.forEach(url -> createImage(entity, url));
    }
}
