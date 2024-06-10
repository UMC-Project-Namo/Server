package com.example.namo2.domain.test.ui;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.namo2.domain.test.ui.dto.TestRequest;
import com.example.namo2.domain.test.ui.dto.TestResponse;
import com.example.namo2.global.annotation.swagger.ApiErrorCode;
import com.example.namo2.global.annotation.swagger.ApiErrorCodes;
import com.example.namo2.global.common.response.BaseResponse;
import com.example.namo2.global.common.response.BaseResponseStatus;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "A. Test", description = "테스트 API")
@RestController
@RequestMapping("/test")
public class TestController {

	@GetMapping("/log")
	@ApiErrorCode(BaseResponseStatus.INTERNET_SERVER_ERROR)
	public BaseResponse<TestResponse.TestDto> testLog() {
		return new BaseResponse<>(
				TestResponse.TestDto.builder()
						.test("test")
						.build()
		);
	}

	@GetMapping("/authenticate")
	@ApiErrorCodes({
			BaseResponseStatus.EMPTY_ACCESS_KEY,
			BaseResponseStatus.EXPIRATION_ACCESS_TOKEN,
			BaseResponseStatus.EXPIRATION_REFRESH_TOKEN,
			BaseResponseStatus.INTERNET_SERVER_ERROR
	})
	public String test() {
		return "인증 완료";
	}

	@PostMapping("/log")
	@ApiErrorCode(BaseResponseStatus.INTERNET_SERVER_ERROR)
	public BaseResponse<TestResponse.LogTestDto> loggingTest(
			@RequestBody TestRequest.LogTestDto logTestDto
	) {
		TestResponse.LogTestDto dto = TestResponse.LogTestDto.builder()
				.text(logTestDto.getText())
				.number(logTestDto.getNumber())
				.build();

		return new BaseResponse<>(dto);
	}
}
