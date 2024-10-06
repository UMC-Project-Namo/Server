package com.namo.spring.application.external.api.category.service;

import static org.mockito.Mockito.*;


import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.namo.spring.db.mysql.domains.category.entity.Category;
import com.namo.spring.db.mysql.domains.category.entity.Palette;
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

}
