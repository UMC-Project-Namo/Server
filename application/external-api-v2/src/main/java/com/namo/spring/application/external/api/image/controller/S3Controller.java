package com.namo.spring.application.external.api.image.controller;

import static com.namo.spring.core.common.code.status.ErrorStatus.*;

import java.net.URL;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCodes;
import com.namo.spring.core.common.response.ResponseDto;
import com.namo.spring.core.infra.common.aws.dto.MultipartCompleteRequest;
import com.namo.spring.core.infra.common.aws.dto.MultipartStartResponse;
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
            @Parameter(description = "업로드할 이미지의 종류를 선택해 주세요. 예: 'activity' (활동 이미지), 'diary' (일기 이미지), 'cover' (커버 이미지), 'profile' (프로필 이미지)",
                    example = "activity")
            @RequestParam String prefix,
            @Parameter(description = "업로드할 파일의 이름을 입력해 주세요.", example = "example.jpg")
            @RequestParam String fileName) {
        String preSignedUrl = s3Service.getPreSignedUrl(prefix, fileName);
        return ResponseDto.onSuccess(preSignedUrl);
    }

    @PostMapping("/pre-signed/start")
    @Operation(
            summary = "S3 Presigned URLs 생성 요청 (Part 업로드용)",
            description = "이 API는 클라이언트가 큰 파일을 여러 파트로 나누어 S3에 업로드할 수 있도록 각 파트에 대한 Presigned URL을 생성합니다. \n"
                    + "파일을 원하는 만큼의 파트로 나누기 위해 partCount에 파트 수를 입력해 주세요. 반환된 각 URL을 사용해 각 파트를 업로드한 후, "
                    + "업로드가 완료되면 반드시 '/pre-signed/complete' API를 호출하여 최종 업로드를 완료해야 합니다. 그렇지 않으면 S3에 임시로 저장된 파일이 일정 기간(7일) 후 삭제될 수 있습니다."
    )
    public ResponseDto<MultipartStartResponse> start(
            @Parameter(description = "업로드할 파일의 이름을 입력해 주세요.", example = "example.jpg")
            @RequestParam String fileName,
            @Parameter(description = "업로드할 이미지의 종류를 선택해 주세요. 예: 'activity' (활동 이미지), 'diary' (일기 이미지), 'cover' (커버 이미지), 'profile' (프로필 이미지)",
                    example = "activity")
            @RequestParam String prefix,
            @Parameter(description = "파일을 몇 개의 파트로 나누어 업로드할지 입력해 주세요.", example = "3")
            @RequestParam int partCount
    ) {
        return ResponseDto.onSuccess(s3Service.getPreSignedUrls(prefix, fileName, partCount));
    }

    @PostMapping("/pre-signed/complete")
    @Operation(
            summary = "S3 멀티파트 업로드 완료 요청",
            description = "이 API는 S3에 멀티파트 업로드를 완료하는 요청을 보냅니다. 클라이언트는 모든 파트를 Presigned URLs을 통해 업로드한 후 이 API를 호출하여 업로드를 최종 완료해야 합니다. \n"
                    + "업로드가 완료되지 않으면 임시 저장된 파일이 일정 기간(7일) 후 삭제될 수 있습니다."
    )
    public ResponseDto<String> complete(
            @Parameter(description = "업로드 완료를 위한 요청 정보를 JSON 형식으로 전달합니다. 이 정보에는 파일 이름, 업로드 ID, 각 파트의 ETag 정보가 포함되어야 합니다.")
            @RequestBody MultipartCompleteRequest request
    ) {
        return ResponseDto.onSuccess(s3Service.completeMultipartUpload(request));
    }
}
