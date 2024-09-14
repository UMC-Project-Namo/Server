package com.namo.spring.application.external.api.category.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.namo.spring.application.external.api.category.dto.CategoryResponse;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.core.common.response.ResponseDto;

import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "카테고리", description = "카테고리 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/categories")
public class CategoryController {

    @GetMapping("")
    public ResponseDto<List<CategoryResponse.MyCategoryInfoDto>> getCategories(
            @AuthenticationPrincipal SecurityUserDetails memberInfo
    ){
        return ResponseDto.onSuccess(null);
    }

}
