package com.namo.spring.application.external.domain.test.ui;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.namo.spring.application.external.domain.test.ui.dto.TestRequest;
import com.namo.spring.application.external.domain.test.ui.dto.TestResponse;
import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCode;
import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCodes;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.response.ResponseDto;

import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "A. Test", description = "테스트 API")
@RestController
@RequestMapping("/test")
public class TestController {

	@GetMapping("/log")
	@ApiErrorCode(ErrorStatus.INTERNET_SERVER_ERROR)
	public ResponseDto<TestResponse.TestDto> testLog() {
		return ResponseDto.onSuccess(
				TestResponse.TestDto.builder()
						.test("test")
						.build()
		);
	}

	@GetMapping("/authenticate")
	@ApiErrorCodes({
			ErrorStatus.EMPTY_ACCESS_KEY,
			ErrorStatus.EXPIRATION_ACCESS_TOKEN,
			ErrorStatus.EXPIRATION_REFRESH_TOKEN,
			ErrorStatus.INTERNET_SERVER_ERROR
	})
	public ResponseDto<String> test() {
		return ResponseDto.onSuccess("인증 완료");
	}

	@PostMapping("/log")
	@ApiErrorCode(ErrorStatus.INTERNET_SERVER_ERROR)
	public ResponseDto<TestResponse.LogTestDto> loggingTest(
			@RequestBody TestRequest.LogTestDto logTestDto
	) {
		TestResponse.LogTestDto dto = TestResponse.LogTestDto.builder()
				.text(logTestDto.getText())
				.number(logTestDto.getNumber())
				.build();

		return ResponseDto.onSuccess(dto);
	}
}
