package com.namo.spring.application.external.api.category.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.namo.spring.application.external.api.category.dto.CategoryRequest;
import com.namo.spring.db.mysql.domains.category.entity.Category;
import com.namo.spring.db.mysql.domains.category.entity.Palette;
import com.namo.spring.db.mysql.domains.category.exception.CategoryException;
import com.namo.spring.db.mysql.domains.category.service.CategoryService;
import com.namo.spring.db.mysql.domains.category.service.PaletteService;
import com.namo.spring.db.mysql.domains.category.type.CategoryStatus;
import com.namo.spring.db.mysql.domains.category.type.CategoryType;
import com.namo.spring.db.mysql.domains.user.entity.Member;

class CategoryManageServiceTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private PaletteService paletteService;

    @Mock
    private CategoryMaker categoryMaker;

    @InjectMocks
    private CategoryManageService categoryManageService;

    private Member member;
    private Palette palette;
    private Category category;

    @BeforeEach
    void setUp() {
        // Mockito 초기화
        MockitoAnnotations.openMocks(this);

        // 공통으로 사용할 객체 설정
        member = mock(Member.class);
        when(member.getId()).thenReturn(1L);

        palette = new Palette(1L, "default", "#FFFFFF", "defaultPalette");

        category = new Category(member, palette, "testCategory", CategoryStatus.ACTIVE, CategoryType.COMMON, false, 3);
    }

    @Test
    @DisplayName("카테고리 조회 성공")
    void successGetMyCategory() {
        Long memberId = 1L;
        Long categoryId = 1L;

        when(categoryService.readCategory(categoryId)).thenReturn(Optional.of(category));

        Category result = categoryManageService.getMyCategory(memberId, categoryId);

        assertEquals(category, result);
    }

    @Test
    @DisplayName("카테고리 조회 실패 : 없는 카테고리에 대해 조회")
    void failGetMyCategory_WhenNotFoundCategory(){
        Long memberId = 1L;
        Long categoryId = 3L;

        when(categoryService.readCategory(categoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryException.class, () -> categoryManageService.getMyCategory(memberId, categoryId));
    }

    @Test
    @DisplayName("카테고리 조회 실패 : 존재하는 카테고리지만 내 카테고리가 아닐때")
    void failGetMyCategory_WhenCategoryDoesNotBelongToUser() {
        Long memberId = 2L;  // 요청한 사용자 ID
        Long categoryId = 1L;  // 조회할 카테고리 ID

        // 카테고리 조회 시 mock 설정 (해당 카테고리가 반환되도록)
        when(categoryService.readCategory(categoryId)).thenReturn(Optional.of(category));

        // 테스트 실행: 해당 카테고리가 다른 유저의 카테고리일 경우 예외가 발생하는지 확인
        assertThrows(CategoryException.class, () -> categoryManageService.getMyCategory(memberId, categoryId));
    }

}
