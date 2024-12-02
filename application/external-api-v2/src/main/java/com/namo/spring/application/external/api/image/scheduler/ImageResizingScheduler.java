package com.namo.spring.application.external.api.image.scheduler;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.application.external.api.image.service.ImageCacheService;
import com.namo.spring.application.external.api.image.util.ImageKeyParser;
import com.namo.spring.db.redis.util.RedisUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImageResizingScheduler {

    private static final Duration PROCESSING_DELAY = Duration.ofSeconds(25);
    private static final String IMAGE_KEY_ZSET = "imageKeys";

    private final ImageCacheService imageCacheService;
    private final RedisUtil redisUtil;

    /**
     * 원본 이미지 -> 리사이징 URL 변경 스케줄러
     * ** 원본 이미지 등록 25초 이후 리사이징 된 이미지로 변경합니다. **
     */
    @Scheduled(fixedRate = 20_000)
    @Transactional
    public void processResizedImages() {
        Instant now = Instant.now();
        long cutoffTime = now.minus(PROCESSING_DELAY).toEpochMilli();
        Set<String> redisKeys = redisUtil.zRangeByScore(IMAGE_KEY_ZSET, 0, cutoffTime);

        redisKeys.forEach(redisKey -> {
            try {
                ImageKeyParser.ParsedKey parsedKey = ImageKeyParser.parse(redisKey);
                imageCacheService.convertToResizedImage(redisKey, parsedKey);
                // 캐시 값 삭제
                redisUtil.zRem(IMAGE_KEY_ZSET, redisKey);
                redisUtil.delete(redisKey);
            } catch (Exception e) {
                log.error("원본 >>> 리사이징 이미지 변환 스케줄러 실패 : {}", redisKey, e);
            }
        });
    }
}
