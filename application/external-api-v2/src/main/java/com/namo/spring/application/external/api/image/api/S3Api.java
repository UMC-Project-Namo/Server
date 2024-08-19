package com.namo.spring.application.external.api.image.api;

import org.springframework.web.bind.annotation.RequestParam;

import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCodes;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.response.ResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "3. Image", description = "S3 이미지 업로드 관련 API")
public interface S3Api {

	@Operation(summary = "s3 presigned Url 생성 요청", description = "이미지 업로드를 위한 S3 Presigned Url을 생성합니다. 요청 성공 시 생성된 URL을 전송합니다.")
	@ApiErrorCodes(value = {ErrorStatus.EMPTY_ACCESS_KEY, ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN, ErrorStatus.INTERNET_SERVER_ERROR})
	@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "모임 일정 생성 성공", value = """
			{
				"isSuccess": true,
				"code": 200,
				"message": "성공",
				"result": "image URL~"
			}
			""")}))
	public ResponseDto<String> generatePresignedUrl(@RequestParam String prefix, @RequestParam String fileName);
}
