package com.namo.spring.application.external.api.image.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.namo.spring.application.external.api.image.api.S3Api;
import com.namo.spring.core.common.response.ResponseDto;
import com.namo.spring.core.infra.common.aws.s3.S3Uploader;

import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/s3")
@Tag(name = "3. Image", description = "S3 이미지 업로드 관련 API")
public class S3Controller implements S3Api {

	private final S3Uploader s3Service;

	@GetMapping("/generate-presigned-url")
	public ResponseDto<String> generatePresignedUrl(@RequestParam String prefix,
		@RequestParam String fileName) {
		String preSignedUrl = s3Service.getPreSignedUrl(prefix, fileName);
		return ResponseDto.onSuccess(preSignedUrl);
	}
}
