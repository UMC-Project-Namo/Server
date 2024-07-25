package com.namo.spring.application.external.api.user.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import com.namo.spring.application.external.api.user.dto.UserRequest;
import com.namo.spring.application.external.api.user.dto.UserResponse;
import com.namo.spring.core.common.response.ResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "1. Auth", description = "로그인, 회원가입 관련 API")
public interface AuthApi {

	@Operation(summary = "카카오 회원가입", description = "카카오 소셜 로그인을 통한 회원가입을 진행합니다.")
	@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "카카오 회원가입 성공 - 신규 유저", value = """
			{
				"isSuccess": true,
				"code": 200,
				"message": "성공",
				"result": {
					"accessToken": "exmapleAccessToken",
					"refreshToken": "exampleRefreshToken",
					"newUser": true,
					"terms": [
						{
							"content": "exampleContent",
							"isCheck": false
						}
					]
				}
			}
			"""),
		@ExampleObject(name = "카카오 회원가입 성공 - 기존 유저", value = """
			{
				"isSuccess": true,
				"code": 200,
				"message": "성공",
				"result": {
					"accessToken": "exmapleAccessToken",
					"refreshToken": "exampleRefreshToken",
					"newUser": false,
					"terms": [
						{
							"content": "exampleContent",
							"isCheck": true
						}
					]
				}
			}""")
	}))
	@ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "요청 실패 - 유저 생성 실패", value = """
			{
				"isSuccess": false,
				"code": 400,
				"message": "email나 name이 비어있어 유저를 생성할 수 없습니다.",
				"result": "error discription"
			}
			""")
	}))
	@ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "카카오 실패 - accessToken 오류", value = """
			{
				"isSuccess": false,
				"code": 401,
				"message": "카카오 accessToken이 잘못되었습니다.",
				"result": "error discription"
			}
			"""),
		@ExampleObject(name = "요청 실패 - 소셜 로그인 요청 실패", value = """
			{
				"isSuccess": false,
				"code": 401,
				"message": "소셜 로그인에 실패했습니다.",
				"result": "error discription"
			}
			""")
	}))
	@ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "카카오 실패 - 카카오 권한 오류", value = """
			{
				"isSuccess": false,
				"code": 403,
				"message": "카카오 권한 오류",
				"result": "error discription"
			}
			"""),
		@ExampleObject(name = "요청 실패 - AccessToken 없음", value = """
			{
				"isSuccess": false,
				"code": 403,
				"message": "AccessToken 이 비어있습니다.",
				"result": "error discription"
			}
			"""),
		@ExampleObject(name = "요청 실패 - AccessToken 만료", value = """
			{
				"isSuccess": false,
				"code": 403,
				"message": "Access token 이 만료되었습니다.",
				"result": "error discription"
			}
			"""),
		@ExampleObject(name = "요청 실패 - RefreshToken 만료", value = """
			{
				"isSuccess": false,
				"code": 403,
				"message": "RefreshToken 이 만료되었습니다.",
				"result": "error discription"
			}
			"""),
	}))
	@ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "카카오 실패 - 카카오 시스템 오류", value = """
			{
				"isSuccess": false,
				"code": 404,
				"message": "카카오 시스템 오류",
				"result": "error discription"
			}
			"""),
		@ExampleObject(name = "카카오 실패 - 카카오 서비스 점검중", value = """
			{
				"isSuccess": false,
				"code": 404,
				"message": "카카오 서비스 점검중",
				"result": "error discription"
			}
			"""),
		@ExampleObject(name = "카카오 실패 - 카카오 서버 오류", value = """
			{
				"isSuccess": false,
				"code": 404,
				"message": "카카오 서버 오류",
				"result": "error discription"
			}
			""")
	}))
	ResponseDto<UserResponse.SignUpDto> kakaoSignup(
		@Valid @RequestBody UserRequest.SocialSignUpDto signUpDto
	);

	@Operation(summary = "네이버 회원가입", description = "네이버 소셜 로그인을 통한 회원가입을 진행합니다.")
	@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "네이버 회원가입 성공 - 신규 유저", value = """
			{
				"isSuccess": true,
				"code": 200,
				"message": "성공",
				"result": {
					"accessToken": "exmapleAccessToken",
					"refreshToken": "exampleRefreshToken",
					"newUser": true,
					"terms": [
						{
							"content": "exampleContent",
							"isCheck": false
						}
					]
				}
			}
			"""),
		@ExampleObject(name = "네이버 회원가입 성공 - 기존 유저", value = """
			{
				"isSuccess": true,
				"code": 200,
				"message": "성공",
				"result": {
					"accessToken": "exmapleAccessToken",
					"refreshToken": "exampleRefreshToken",
					"newUser": false,
					"terms": [
						{
							"content": "exampleContent",
							"isCheck": true
						}
					]
				}
			}""")
	}))
	@ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "요청 실패 - 유저 생성 실패", value = """
			{
				"isSuccess": false,
				"code": 400,
				"message": "email나 name이 비어있어 유저를 생성할 수 없습니다.",
				"result": "error discription"
			}
			""")
	}))
	@ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "네이버 실패 - accessToken 오류", value = """
			{
				"isSuccess": false,
				"code": 401,
				"message": "네이버 accessToken이 잘못되었습니다.",
				"result": "error discription"
			}
			"""),
		@ExampleObject(name = "요청 실패 - 소셜 로그인 요청 실패", value = """
			{
				"isSuccess": false,
				"code": 401,
				"message": "소셜 로그인에 실패했습니다.",
				"result": "error discription"
			}
			""")
	}))
	@ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "네이버 실패 - 네이버 권한 오류", value = """
			{
				"isSuccess": false,
				"code": 403,
				"message": "네이버 권한 오류",
				"result": "error discription"
			}
			"""),
		@ExampleObject(name = "요청 실패 - AccessToken 없음", value = """
			{
				"isSuccess": false,
				"code": 403,
				"message": "AccessToken 이 비어있습니다.",
				"result": "error discription"
			}
			"""),
		@ExampleObject(name = "요청 실패 - AccessToken 만료", value = """
			{
				"isSuccess": false,
				"code": 403,
				"message": "Access token 이 만료되었습니다.",
				"result": "error discription"
			}
			"""),
		@ExampleObject(name = "요청 실패 - RefreshToken 만료", value = """
			{
				"isSuccess": false,
				"code": 403,
				"message": "RefreshToken 이 만료되었습니다.",
				"result": "error discription"
			}
			"""),
	}))
	ResponseDto<UserResponse.SignUpDto> naverSignup(
		@Valid @RequestBody UserRequest.SocialSignUpDto signUpDto
	);

	@Operation(summary = "애플 회원가입", description = "애플 소셜 로그인을 통한 회원가입을 진행합니다.")
	@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "애플 회원가입 성공 - 신규 유저", value = """
			{
				"isSuccess": true,
				"code": 200,
				"message": "성공",
				"result": {
					"accessToken": "exmapleAccessToken",
					"refreshToken": "exampleRefreshToken",
					"newUser": true,
					"terms": [
						{
							"content": "exampleContent",
							"isCheck": false
						}
					]
				}
			}
			"""),
		@ExampleObject(name = "애플 회원가입 성공 - 기존 유저", value = """
			{
				"isSuccess": true,
				"code": 200,
				"message": "성공",
				"result": {
					"accessToken": "exmapleAccessToken",
					"refreshToken": "exampleRefreshToken",
					"newUser": false,
					"terms": [
						{
							"content": "exampleContent",
							"isCheck": true
						}
					]
				}
			}""")
	}))
	@ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "요청 실패 - 유저 생성 실패", value = """
			{
				"isSuccess": false,
				"code": 400,
				"message": "email나 name이 비어있어 유저를 생성할 수 없습니다.",
				"result": "error discription"
			}
			"""),
		@ExampleObject(name = "요청 실패 - identityToken 오류", value = """
			{
				"isSuccess": false,
				"code": 400,
				"message": "애플 identityToken이 잘못되었습니다.",
				"result": "error discription"
			}
			""")
	}))
	@ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "애플 실패 - authorizationCode 오류", value = """
			{
				"isSuccess": false,
				"code": 401,
				"message": "애플 authorization code가 잘못되었습니다.",
				"result": "error discription"
			}
			"""),
		@ExampleObject(name = "요청 실패 - 소셜 로그인 요청 실패", value = """
			{
				"isSuccess": false,
				"code": 401,
				"message": "소셜 로그인에 실패했습니다.",
				"result": "error discription"
			}
			""")
	}))
	@ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "요청 실패 - feign 서버 에러", value = """
			{
				"isSuccess": false,
				"code": 404,
				"message": "feign 서버 에러",
				"result": "error discription"
			}
			""")
	}))
	ResponseDto<UserResponse.SignUpDto> appleSignup(
		@Valid @RequestBody UserRequest.AppleSignUpDto dto
	);

	@Operation(summary = "토큰 재발급", description = "토큰 재발급")
	@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "토큰 재발급 성공", value = """
			{
				"isSuccess": true,
				"code": 200,
				"message": "성공",
				"result": {
					"accessToken": "exampleAccessToken",
					"refreshToken": "exampleRefreshToken"
				}
			}
			""")
	}))
	@ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "요청 실패 - 로그 아웃된 사용자", value = """
			{
				"isSuccess": false,
				"code": 403,
				"message": "로그 아웃된 사용자입니다.
			}
			""")
	}))
	@ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "요청 실패 - 유저 없음", value = """
			{
				"isSuccess": false,
				"code": 404,
				"message": "유저를 찾을 수 없습니다.",
				"result": "error discription"
			}
			""")
	}))
	ResponseDto<UserResponse.ReissueDto> reissueAccessToken(
		@Valid @RequestBody UserRequest.ReissueDto reissueDto
	);

	@Operation(summary = "로그아웃", description = "로그아웃 API, 로그아웃 처리된 유저의 토큰을 만료시킵니다.")
	@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "로그아웃 성공", value = """
			{
				"isSuccess": true,
				"code": 200,
				"message": "성공",
				"result": null
			}
			""")
	}))
	ResponseDto<Void> logout(
		@Valid @RequestBody UserRequest.LogoutDto logoutDto
	);

	@Operation(summary = "카카오 회원 탈퇴", description = """
		카카오 회원 탈퇴 API, 카카오 회원 탈퇴 처리를 진행합니다.
		카카오 회원 탈퇴 시, 카카오 회원 정보를 삭제하고, 회원의 애플리케이션 연결을 해제합니다.
		
		이때, 삭제 처리는 바로 진행되는 것이 아니며 탈퇴 신청 후 3일간 유예기간이 있습니다.
		""")
	@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "카카오 회원 탈퇴 성공", value = """
			{
				"isSuccess": true,
				"code": 200,
				"message": "성공",
				"result": null
			}
			""")
	}))
	@ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "카카오 실패 - AccessToken 오류", value = """
			{
				"isSuccess": false,
				"code": 401,
				"message": "카카오 AccessToken이 잘못되었습니다."
			}
			"""),
		@ExampleObject(name = "요청 실패 - 소셜 로그인 회원탈퇴 실패", value = """
			{
				"isSuccess": false,
				"code": 401,
				"message": "소셜 로그인 회원탈퇴에 실패했습니다."
			}
			""")
	}))
	@ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "요청 실패 - 로그 아웃된 사용자", value = """
			{
				"isSuccess": false,
				"code": 403,
				"message": "로그 아웃된 사용자입니다.
			}
			"""),
		@ExampleObject(name = "카카오 실패 - 카카오 권한 오류", value = """
			{
				"isSuccess": false,
				"code": 403,
				"message": "카카오 권한 오류"
			}
			""")
	}))
	@ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "요청 실패 - 유저 없음", value = """
			{
				"isSuccess": false,
				"code": 404,
				"message": "유저를 찾을 수 없습니다."
			}
			"""),
		@ExampleObject(name = "카카오 실패 - 카카오 시스템 오류", value = """
			{
				"isSuccess": false,
				"code": 404,
				"message": "카카오 시스템 오류"
			}
			"""),
		@ExampleObject(name = "카카오 실패 - 카카오 서비스 점검중", value = """
			{
				"isSuccess": false,
				"code": 404,
				"message": "카카오 서비스 점검중"
			}
			"""),
		@ExampleObject(name = "카카오 실패 - 카카오 서버 오류", value = """
			{
				"isSuccess": false,
				"code": 404,
				"message": "카카오 서버 오류"
			}
			"""),
		@ExampleObject(name = "요청 실패 - feign 서버 에러", value = """
			{
				"isSuccess": false,
				"code": 404,
				"message": "feign 서버 에러"
			}
			""")
	}))
	ResponseDto<Void> removeKakaoUser(HttpServletRequest request);

	@Operation(summary = "네이버 회원 탈퇴", description = """
		네이버 회원 탈퇴 API, 네이버 회원 탈퇴 처리를 진행합니다.
		네이버 회원 탈퇴 시, 네이버 회원 정보를 삭제하고, 회원의 애플리케이션 연결을 해제합니다.
		
		이때, 삭제 처리는 바로 진행되는 것이 아니며 탈퇴 신청 후 3일간 유예기간이 있습니다.
		""")
	@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "네이버 회원 탈퇴 성공", value = """
			{
				"isSuccess": true,
				"code": 200,
				"message": "성공",
				"result": null
			}
			""")
	}))
	@ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "네이버 실패 - AccessToken 오류", value = """
			{
				"isSuccess": false,
				"code": 401,
				"message": "네이버 AccessToken이 잘못되었습니다."
			}
			"""),
		@ExampleObject(name = "요청 실패 - 소셜 로그인 회원탈퇴 실패", value = """
			{
				"isSuccess": false,
				"code": 401,
				"message": "소셜 로그인 회원탈퇴에 실패했습니다."
			}
			""")
	}))
	@ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "요청 실패 - 로그 아웃된 사용자", value = """
			{
				"isSuccess": false,
				"code": 403,
				"message": "로그 아웃된 사용자입니다.
			}
			"""),
		@ExampleObject(name = "네이버 실패 - 네이버 권한 오류", value = """
			{
				"isSuccess": false,
				"code": 403,
				"message": "네이버 권한 오류"
			}
			""")
	}))
	@ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "요청 실패 - 유저 없음", value = """
			{
				"isSuccess": false,
				"code": 404,
				"message": "유저를 찾을 수 없습니다."
			}
			"""),
		@ExampleObject(name = "네이버 실패 - 유저 없음", value = """
			{
				"isSuccess": false,
				"code": 404,
				"message": "[네이버] 검색 결과가 없습니다."
			}
			"""),
		@ExampleObject(name = "요청 실패 - feign 서버 에러", value = """
			{
				"isSuccess": false,
				"code": 404,
				"message": "feign 서버 에러"
			}
			""")
	}))
	ResponseDto<Void> removeNaverUser(HttpServletRequest request);

	@Operation(summary = "애플 회원 탈퇴", description = """
		애플 회원 탈퇴 API, 애플 회원 탈퇴 처리를 진행합니다.
		애플 회원 탈퇴 시, 애플 회원 정보를 삭제하고, 회원의 애플리케이션 연결을 해제합니다.
		
		이때, 삭제 처리는 바로 진행되는 것이 아니며 탈퇴 신청 후 3일간 유예기간이 있습니다.
		""")
	@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "애플 회원 탈퇴 성공", value = """
			{
				"isSuccess": true,
				"code": 200,
				"message": "성공",
				"result": null
			}
			""")
	}))
	@ApiResponse(responseCode = "401", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "애플 실패 - authorizationCode 오류", value = """
			{
				"isSuccess": false,
				"code": 401,
				"message": "애플 authorization code가 잘못되었습니다."
			}
			"""),
		@ExampleObject(name = "요청 실패 - 소셜 로그인 회원탈퇴 실패", value = """
			{
				"isSuccess": false,
				"code": 401,
				"message": "소셜 로그인 회원탈퇴에 실패했습니다."
			}
			""")
	}))
	@ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "요청 실패 - 로그 아웃된 사용자", value = """
			{
				"isSuccess": false,
				"code": 403,
				"message": "로그 아웃된 사용자입니다."
			}
			""")
	}))
	@ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "요청 실패 - 유저 없음", value = """
			{
				"isSuccess": false,
				"code": 404,
				"message": "유저를 찾을 수 없습니다."
			}
			"""),
		@ExampleObject(name = "요청 실패 - feign 서버 에러", value = """
			{
				"isSuccess": false,
				"code": 404,
				"message": "feign 서버 에러"
			}
			""")
	}))
	ResponseDto<Void> removeAppleUser(HttpServletRequest request);
}
