package com.example.namo2.domain.individual.ui;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.example.namo2.domain.individual.application.CategoryFacade;
import com.example.namo2.domain.individual.ui.dto.CategoryRequest;
import com.example.namo2.domain.individual.ui.dto.CategoryResponse;

import com.example.namo2.global.annotation.swagger.ApiErrorCodes;
import com.example.namo2.global.common.response.BaseResponse;
import com.example.namo2.global.common.response.BaseResponseStatus;

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
		BaseResponseStatus.EMPTY_ACCESS_KEY,
		BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
		BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
		BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<CategoryResponse.CategoryIdDto> createCategory(
			@Valid @RequestBody CategoryRequest.PostCategoryDto postcategoryDto,
			HttpServletRequest request
	) {
		Long userId = (Long)request.getAttribute("userId");
		CategoryResponse.CategoryIdDto categoryIdDto = categoryFacade.create(userId, postcategoryDto);
		return new BaseResponse<>(categoryIdDto);
	}

	@Operation(summary = "카테고리 조회", description = "카테고리 조회 API")
	@GetMapping("")
	@ApiErrorCodes(value = {
		BaseResponseStatus.EMPTY_ACCESS_KEY,
		BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
		BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
		BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<List<CategoryResponse.CategoryDto>> findAllCategory(
			HttpServletRequest request
	) {
		Long userId = (Long)request.getAttribute("userId");
		List<CategoryResponse.CategoryDto> categories = categoryFacade.getCategories(userId);
		return new BaseResponse<>(categories);
	}

	@Operation(summary = "카테고리 수정", description = "카테고리 수정 API")
	@PatchMapping("/{categoryId}")
	@ApiErrorCodes(value = {
		BaseResponseStatus.EMPTY_ACCESS_KEY,
		BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
		BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
		BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<CategoryResponse.CategoryIdDto> updateCategory(
			@Parameter(description = "카테고리 ID") @PathVariable("categoryId") Long categoryId,
			@Valid @RequestBody CategoryRequest.PostCategoryDto postcategoryDto,
			HttpServletRequest request
	) {
		Long userId = (Long)request.getAttribute("userId");
		CategoryResponse.CategoryIdDto categoryIdDto = categoryFacade.modifyCategory(categoryId, postcategoryDto,
				userId);
		return new BaseResponse<>(categoryIdDto);
	}

	@Operation(summary = "카테고리 삭제", description = "카테고리 삭제 API")
	@DeleteMapping("/{categoryId}")
	@ApiErrorCodes(value = {
		BaseResponseStatus.EMPTY_ACCESS_KEY,
		BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
		BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
		BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public BaseResponse<String> deleteCategory(
			@Parameter(description = "카테고리 ID") @PathVariable("categoryId") Long categoryId,
			HttpServletRequest request
	) {
		Long userId = (Long)request.getAttribute("userId");
		categoryFacade.deleteCategory(categoryId, userId);
		return new BaseResponse<>("삭제에 성공하셨습니다.");
	}

}
