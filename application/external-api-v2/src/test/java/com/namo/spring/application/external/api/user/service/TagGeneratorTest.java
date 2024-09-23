package com.namo.spring.application.external.api.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.namo.spring.application.external.global.utils.TagGenerator;

public class TagGeneratorTest {

    @Mock
    private MemberManageService memberManageService;

    @InjectMocks
    private TagGenerator tagGenerator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGenerateTag_UniqueTagGenerated() {
        // Given
        String nickname = "testUser";
        List<String> existingTags = Arrays.asList("0001", "0002", "0003", "9999");

        when(memberManageService.getMemberTagsByNickname(nickname)).thenReturn(existingTags);

        // When
        String newTag = tagGenerator.generateTag(nickname);

        // Then
        assertNotNull(newTag);
        assertFalse(existingTags.contains(newTag), "The generated tag should not be in the existing tags");
        assertEquals(4, newTag.length(), "The generated tag should be 4 characters long");
    }

    @Test
    public void testGenerateTag_NoExistingTags() {
        // Given
        String nickname = "newUser";
        List<String> existingTags = Arrays.asList();

        when(memberManageService.getMemberTagsByNickname(nickname)).thenReturn(existingTags);

        // When
        String newTag = tagGenerator.generateTag(nickname);

        // Then
        assertNotNull(newTag);
        assertEquals(4, newTag.length(), "The generated tag should be 4 characters long");
    }

}
