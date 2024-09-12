package com.namo.spring.application.external.api.user.service;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TagGenerator {

    private final MemberManageService memberManageService;

    public String generateTag(String nickname) {
        List<String> existTags = memberManageService.getUserTagsByNicname(nickname);
        Random random = new Random();
        String newTag;
        do {
            newTag = String.format("%04d", random.nextInt(10000));
        } while (existTags.contains(newTag));
        return newTag;
    }

}
