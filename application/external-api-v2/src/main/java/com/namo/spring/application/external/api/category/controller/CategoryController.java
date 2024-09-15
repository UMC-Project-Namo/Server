package com.namo.spring.application.external.api.category.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.namo.spring.application.external.api.category.dto.CategoryRequest;
import com.namo.spring.application.external.api.category.dto.CategoryResponse;
import com.namo.spring.application.external.api.category.usecase.CategoryUseCase;
import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCodes;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.response.ResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "카테고리", description = "카테고리 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/categories")
public class CategoryController {

    private final CategoryUseCase categoryUseCase;

    @Operation(summary = "나의 카테고리 목록 조회", description = "나의 카테고리 목록이 조회됩니다. 이후 뎁스의 내용도 포함됩니다.")
    @ApiErrorCodes(value = {
            ErrorStatus.NOT_FOUND_USER_FAILURE,
    })
    @GetMapping("")
    public ResponseDto<List<CategoryResponse.MyCategoryInfoDto>> getCategories(
            @AuthenticationPrincipal SecurityUserDetails memberInfo
    ){
        return ResponseDto.onSuccess(categoryUseCase
                .getMyCategoryList(memberInfo.getUserId()));
    }

    @Operation(summary = "나의 카테고리 생성", description = "나의 일반 카테고리를 생성 합니다.")
    @ApiErrorCodes(value = {
            ErrorStatus.NOT_FOUND_USER_FAILURE,
            ErrorStatus.NOT_FOUND_CATEGORY_FAILURE,
            ErrorStatus.NOT_FOUND_PALETTE_FAILURE
    })
    @PostMapping("")
    public ResponseDto<String> createCategory(
            @AuthenticationPrincipal SecurityUserDetails memberInfo,
            @RequestBody CategoryRequest.CategoryCreateDto request
    ){
        categoryUseCase.createCategory(memberInfo.getUserId(), request);
        return ResponseDto.onSuccess("카테고리 생성 완료");
    }

    @Operation(summary = "나의 카테고리 수정", description = "나의 카테고리를 수정 합니다.")
    @ApiErrorCodes(value = {
            ErrorStatus.NOT_FOUND_USER_FAILURE,
            ErrorStatus.NOT_FOUND_CATEGORY_FAILURE,
            ErrorStatus.NOT_USERS_CATEGORY,
            ErrorStatus.NOT_FOUND_PALETTE_FAILURE
    })
    @PatchMapping("/{categoryId}")
    public ResponseDto<String> updateCategory(
            @AuthenticationPrincipal SecurityUserDetails memberInfo,
            @Parameter(description = "수정할 카테고리 ID 입니다.", example = "3")
            @PathVariable Long categoryId,
            @RequestBody CategoryRequest.CategoryUpdateDto request
    ){
        categoryUseCase.updateCategory(memberInfo.getUserId(), categoryId, request);
        return ResponseDto.onSuccess("카테고리 수정 완료");
    }

    @DeleteMapping("/{categoryId}")
    public ResponseDto<String> deleteCategory(
            @AuthenticationPrincipal SecurityUserDetails memberInfo,
            @Parameter(description = "삭제할 카테고리 ID 입니다.", example = "3")
            @PathVariable Long categoryId
    ){
        return ResponseDto.onSuccess("카테고리 삭제 완료");
    }
}
