package com.namo.spring.application.external.api.user.service;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TagGenerator {

    private static final int TAG_LENGTH = 4;
    private static final int MAX_TAG_VALUE = 10000;

    private final MemberManageService memberManageService;
    private final Random random = new Random();

    public String generateTag(String nickname) {
        Set<String> existingTags = new HashSet<>(memberManageService.getUserTagsByNicname(nickname));
        return generateUniqueTag(existingTags);
    }

    private String generateUniqueTag(Set<String> existingTags) {
        String newTag;
        do {
            newTag = generateRandomTag();
        } while (existingTags.contains(newTag));
        return newTag;
    }

    private String generateRandomTag() {
        return String.format("%0" + TAG_LENGTH + "d", this.random.nextInt(MAX_TAG_VALUE));
    }
}
