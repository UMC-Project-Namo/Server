package com.namo.spring.application.external.api.individual.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.namo.spring.application.external.api.individual.dto.CategoryRequest;
import com.namo.spring.application.external.api.individual.dto.CategoryResponse;
import com.namo.spring.application.external.api.individual.facade.CategoryFacade;
import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCodes;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.response.ResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "5. Category", description = "카테고리 관련 API")
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
	private final CategoryFacade categoryFacade;

	@Operation(summary = "카테고리 생성", description = "카테고리 생성 API")
	@PostMapping("")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<CategoryResponse.CategoryIdDto> createCategory(
		@Valid @RequestBody CategoryRequest.PostCategoryDto postcategoryDto,
		@AuthenticationPrincipal SecurityUserDetails user
	) {
		Long userId = user.getUserId();
		CategoryResponse.CategoryIdDto categoryIdDto = categoryFacade.createCategory(userId, postcategoryDto);
		return ResponseDto.onSuccess(categoryIdDto);
	}

	@Operation(summary = "카테고리 조회", description = "카테고리 조회 API")
	@GetMapping("")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<List<CategoryResponse.CategoryDto>> findAllCategory(
		@AuthenticationPrincipal SecurityUserDetails user
	) {
		Long userId = user.getUserId();
		List<CategoryResponse.CategoryDto> categories = categoryFacade.getCategories(userId);
		return ResponseDto.onSuccess(categories);
	}

	@Operation(summary = "카테고리 수정", description = "카테고리 수정 API")
	@PatchMapping("/{categoryId}")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<CategoryResponse.CategoryIdDto> updateCategory(
		@Parameter(description = "카테고리 ID") @PathVariable("categoryId") Long categoryId,
		@Valid @RequestBody CategoryRequest.PostCategoryDto postcategoryDto,
		@AuthenticationPrincipal SecurityUserDetails user
	) {
		Long userId = user.getUserId();
		CategoryResponse.CategoryIdDto categoryIdDto = categoryFacade.modifyCategory(categoryId, postcategoryDto,
			userId);
		return ResponseDto.onSuccess(categoryIdDto);
	}

	@Operation(summary = "카테고리 삭제", description = "카테고리 삭제 API")
	@DeleteMapping("/{categoryId}")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<String> deleteCategory(
		@Parameter(description = "카테고리 ID") @PathVariable("categoryId") Long categoryId,
		@AuthenticationPrincipal SecurityUserDetails user
	) {
		Long userId = user.getUserId();
		categoryFacade.deleteCategory(categoryId, userId);
		return ResponseDto.onSuccess("삭제에 성공하셨습니다.");
	}

}
