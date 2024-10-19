package com.namo.spring.application.external.api.image.controller;

import static com.namo.spring.core.common.code.status.ErrorStatus.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCodes;
import com.namo.spring.core.common.response.ResponseDto;
import com.namo.spring.core.infra.common.aws.s3.S3Uploader;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "*. Image", description = "S3 이미지 업로드 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/s3")
public class S3Controller {

    private final S3Uploader s3Service;

    @Operation(summary = "s3 presigned Url 생성 요청", description = "이미지 업로드를 위한 S3 Presigned Url을 생성합니다. 요청 성공 시 생성된 URL을 전송합니다.")
    @ApiErrorCodes(value = {
            EMPTY_ACCESS_KEY,
            EXPIRATION_ACCESS_TOKEN,
            EXPIRATION_REFRESH_TOKEN,
            INTERNET_SERVER_ERROR})
    @GetMapping("/generate-presigned-url")
    public ResponseDto<String> generatePresignedUrl(
            @Parameter(description = "이미지 종류입니다 {activity: 활동 이미지, diary: 일기 이미지, cover: 커버 이미지, profile: 프로필 이미지} 입력 가능합니다.", example = "activity")
            @RequestParam String prefix,
            @RequestParam String fileName) {
        String preSignedUrl = s3Service.getPreSignedUrl(prefix, fileName);
        return ResponseDto.onSuccess(preSignedUrl);
    }
}
