package com.namo.spring.application.external.api.image.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Redis Image Resizing에 사용되는 Parser 값입니다.
 * !! 상수 변경에 유의해 주세요. !!
 */
public class ImageKeyParser {

    private static final int EXPECTED_PART_COUNT = 5;
    private static final int ENTITY_TYPE_INDEX = 0;
    private static final int IMAGE_ID_INDEX = 2;
    private static final int TIMESTAMP_INDEX = 4;
    private static final String IMAGE_ID_MARKER = "imageId";
    private static final String TIMESTAMP_MARKER = "Timestamp";

    @Getter
    @RequiredArgsConstructor
    public static class ParsedKey {
        private final String entityType;
        private final Long imageId;
        private final Long timestamp;
    }

    public static ParsedKey parse(String redisKey) {
        String[] parts = redisKey.split(":");

        validateKeyFormat(parts, redisKey);
        try {
            String entityType = parts[ENTITY_TYPE_INDEX];
            Long imageId = Long.parseLong(parts[IMAGE_ID_INDEX]);
            Long timestamp = Long.parseLong(parts[TIMESTAMP_INDEX]);

            return new ParsedKey(entityType, imageId, timestamp);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Failed to parse Redis key: " + redisKey, e);
        }
    }

    private static void validateKeyFormat(String[] parts, String originalKey) {
        if (parts.length != EXPECTED_PART_COUNT
                || !parts[1].equals(IMAGE_ID_MARKER)
                || !parts[3].equals(TIMESTAMP_MARKER)) {
            throw new IllegalArgumentException("Invalid Redis key format: " + originalKey);
        }
    }
}
