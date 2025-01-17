package com.namo.spring.application.external.api.group.api;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.namo.spring.application.external.api.group.dto.GroupDiaryResponse;
import com.namo.spring.application.external.api.group.dto.MeetingDiaryRequest;
import com.namo.spring.application.external.api.group.dto.MeetingDiaryResponse;
import com.namo.spring.application.external.global.annotation.swagger.ApiErrorCodes;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.response.ResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "8. Diary (모임)", description = "모임 기록 관련 API")
public interface MeetingDiaryApi {
	@Operation(summary = "모임 활동 생성", description = "모임 활동을 생성합니다")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "모임 활동 생성 성공", value = """
			{
				"isSuccess": true,
				"code": 0,
				"message": "string",
				"result": null
			}
			""")
	}))
	@ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "요청 실패 - 모임 일정 ID가 잘못되었습니다. ", value = """
			{
				"isSuccess": false,
				"code": 404,
				"message": "스케줄을 찾을 수 없습니다."
			}
			""")
	}))
	public ResponseDto<Void> createMeetingActivity(
		@Parameter(description = "모임 일정 ID") @PathVariable Long moimScheduleId,
		@Parameter(description = "추가할 모임 활동 이미지") @RequestPart(required = false) List<MultipartFile> createImages,
		@Parameter(description = "활동명") @RequestParam String activityName,
		@Parameter(description = "활동 회비") @RequestParam String activityMoney,
		@Parameter(description = "활동 참여자", example = "1, 2") @RequestParam List<Long> participantUserIds
	);

	@Operation(summary = "모임 활동 수정", description = "모임 활동 수정 API")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "모임 활동 수정 성공", value = """
			{
				"isSuccess": true,
				"code": 0,
				"message": "string",
				"result": null
			}
			""")
	}))
	@ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "요청 실패 - 모임 활동 id가 잘못되었습니다", value = """
			{
				"isSuccess": false,
				"code": 404,
				"message": "모임 활동을 찾을 수 없습니다."
			}
			"""),
		@ExampleObject(name = "요청 실패 - 모임 활동 이미지 id가 잘못되었습니다", value = """
			{
				"isSuccess": false,
				"code": 404,
				"message": "모임 활동 이미지를 찾을 수 없습니다."
			}
			""")
	}))
	public ResponseDto<Object> updateMeetingActivity(
		@Parameter(description = "수정하고자 하는 활동 ID") @PathVariable Long activityId,
		@Parameter(description = "추가할 모임 활동 이미지") @RequestPart(required = false) List<MultipartFile> createImages,
		@Parameter(description = "삭제할 모임 활동 이미지 ID") @RequestParam(required = false) List<Long> deleteImageIds,
		@Parameter(description = "활동명") @RequestParam String activityName,
		@Parameter(description = "활동 회비") @RequestParam String activityMoney,
		@Parameter(description = "활동 참여자", example = "1, 2") @RequestParam List<Long> participantUserIds
	);

	@Operation(summary = "모임 기록 조회", description = "모임 기록 조회 API")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "모임 기록 조회 성공", value = """
			{
				"isSuccess": true,
				"code": 200,
				"message": "성공",
				"result": {
					"name": "모임 기록 제목",
					"startDate": 1676052480,
					"locationName": "모임 기록 장소 이름",
					"users": [
						{
							"userId": 3,
							"userName": "몽몽"
						},
						{
							"userId": 19,
							"userName": "몽몽2"
						}
					],
					"moimActivityDtos": [
						{
							"moimActivityId": 1,
							"name": "활동",
							"money": 0,
							"participants": [
								3,
								19
							],
							"images": [
								{
									"id" : 1,
									"url" : "이미지 url"
								}
							]
						}
					]
				}
			}
			""")
	}))
	@ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "요청 실패 - 모임 기록(일정) id가 잘못되었습니다", value = """
			{
				"isSuccess": false,
				"code": 404,
				"message": "스케줄을 찾을 수 없습니다."
			}
			""")
	}))
	public ResponseDto<GroupDiaryResponse.GroupDiaryDto> getGroupDiary(
		@Parameter(description = "모임 기록 ID") @PathVariable("moimScheduleId") Long moimScheduleId
	);

	@Operation(summary = "[개인 페이지] 월간 모임 기록 조회", description = "월간 모임 기록 조회 API")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "개인 페이지 월간 모임 기록 조회 성공", value = """
			{
				"isSuccess": true,
				"code": 200,
				"message": "성공",
				"result": {
					"content": [
						{
							"scheduleId": 29,
							"name": "모임 일정 제목",
							"startDate": 1676052480,
							"contents": "모임 일정에 해당하는 메모",
							"images": [
								{
									"id" : 1,
									"url" : "이미지 url"
								}
							],
							"categoryId": 31,
							"color": 4,
							"placeName": "모임 일정 장소"
						}
					],
					"currentPage": 0,
					"size": 4,
					"first": true,
					"last": true
				}
			}
			""")
	}))
	@ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "요청 실패 - 유효한 날짜를 조회 일자에 적어주세요", value = """
			{
				"isSuccess": false,
				"code": 404,
				"message": "유효한 날짜 값을 입력해주세요"
			}
			""")
	}))
	public ResponseDto<MeetingDiaryResponse.SliceDiaryDto<MeetingDiaryResponse.DiaryDto>> findMonthMeetingDiary(
		@Parameter(description = "조회 일자", example = "{년},{월}") @PathVariable("month") String month,
		Pageable pageable,
		@AuthenticationPrincipal SecurityUserDetails user
	);

	@Operation(summary = "[개인 페이지] 모임 기록 상세 조회", description = "모임 기록 상세 조회 API")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "개인 페이지 모임 기록 상세 조회 성공", value = """
			{
				"isSuccess": true,
				"code": 200,
				"message": "성공",
				"result": {
					"scheduleId": 29,
					"name": "모임 일정 제목",
					"startDate": 1676052480,
					"contents": "모임 일정에 해당하는 메모",
					"images": [
						{
							"id" : 1,
							"url" : "이미지 url"
						}
					],
					"categoryId": 31,
					"color": 4,
					"placeName": "모임 일정 장소"
				}
			}
			""")
	}))
	public ResponseDto<MeetingDiaryResponse.DiaryDto> getMeetingDiaryDetail(
		@Parameter(description = "모임 일정 ID") @PathVariable Long moimScheduleId,
		@AuthenticationPrincipal SecurityUserDetails user
	);

	@Operation(summary = "[개인 페이지] 모임 기록 삭제", description = "일정에 대한 모임 활동 기록 삭제 API")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "개인 페이지 모임 기록 삭제 성공", value = """
			{
				"isSuccess": true,
				"code": 200,
				"message": "성공",
				"result": null
			}
			""")
	}))
	@ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "요청 실패 - 모임 일정 id가 잘못되었습니다. ", value = """
			{
				"isSuccess": false,
				"code": 404,
				"message": "스케줄을 찾을 수 없습니다."
			}
			"""),
		@ExampleObject(name = "요청 실패 - 모임 일정 참여자가 아닙니다. ", value = """
			{
				"isSuccess": false,
				"code": 404,
				"message": "그룹 스케줄 구성원이 아닙니다."
			}
			""")
	}))
	public ResponseDto<Object> removePersonMeetingDiary(
		@Parameter(description = "모임 일정 ID") @PathVariable Long scheduleId,
		@AuthenticationPrincipal SecurityUserDetails user
	);

	@Operation(summary = "모임 기록 삭제", description = "일정에 대한 모임 기록 전체 삭제 API")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "모임 기록 삭제 성공", value = """
			{
				"isSuccess": true,
				"code": 200,
				"message": "성공",
				"result": null
			}
			""")
	}))
	public ResponseDto<Object> removeMeetingDiary(
		@Parameter(description = "모임 일정 ID") @PathVariable Long moimScheduleId
	);

	@Operation(summary = "모임 활동 삭제", description = "모임 활동 삭제 API")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "모임 활동 삭제 성공", value = """
			{
				"isSuccess": true,
				"code": 200,
				"message": "성공",
				"result": null
			}
			""")
	}))
	@ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "요청 실패 - 모임 활동 id가 잘못되었습니다.", value = """
			{
				"isSuccess": false,
				"code": 404,
				"message": "모임 활동을 찾을 수 없습니다."
			}
			""")
	}))
	public ResponseDto<Object> removeMeetingActivity(
		@Parameter(description = "모임 활동 ID") @PathVariable Long activityId
	);

	@Operation(summary = "[개인 페이지] 모임 메모 추가)", description = "모임 메모 추가 API")
	@ApiErrorCodes(value = {
		ErrorStatus.EMPTY_ACCESS_KEY,
		ErrorStatus.EXPIRATION_ACCESS_TOKEN,
		ErrorStatus.EXPIRATION_REFRESH_TOKEN,
		ErrorStatus.INTERNET_SERVER_ERROR
	})
	@ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "개인 페이지 모임 메모 추가 성공", value = """
			{
				"isSuccess": true,
				"code": 200,
				"message": "성공",
				"result": null
			}
			""")
	}))
	@ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {
		@ExampleObject(name = "요청 실패 - 모임 일정 id가 잘못되었습니다.", value = """
			{
				"isSuccess": false,
				"code": 404,
				"message": "스케줄을 찾을 수 없습니다."
			}
			"""),
		@ExampleObject(name = "요청 실패 - 모임 일정 참여자가 아닙니다.", value = """
			{
				"isSuccess": false,
				"code": 404,
				"message": "그룹 스케줄 구성원이 아닙니다."
			}
			""")
	}))
	public ResponseDto<Object> createMeetingMemo(
		@Parameter(description = "모임 일정 ID") @PathVariable Long moimScheduleId,
		@RequestBody MeetingDiaryRequest.PostMeetingScheduleTextDto meetingScheduleText,
		@AuthenticationPrincipal SecurityUserDetails user
	);

}
