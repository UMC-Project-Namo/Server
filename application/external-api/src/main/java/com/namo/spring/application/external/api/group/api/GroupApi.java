package com.namo.spring.application.external.api.group.api;

import com.namo.spring.application.external.api.group.dto.GroupRequest;
import com.namo.spring.application.external.api.group.dto.GroupResponse;
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
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "6. Group", description = "그룹 관련 API")
public interface GroupApi {
    @Operation(summary = "그룹 생성", description = "그룹 생성 API")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
            @ExampleObject(name = "그룹 생성 성공", value = """
                    {
                    	"isSuccess": true,
                    	"code": 200,
                    	"message": "성공",
                    	"result": 1
                    }
                    """)
    }))
    ResponseDto<GroupResponse.GroupIdDto> createGroup(
            @Parameter(name = "img", description = "그룹 프로필 이미지") @RequestPart(name = "img", required = false) MultipartFile img,
            @Parameter(name = "groupName", description = "그룹명") @RequestPart(name = "groupName", required = true) String groupName,
            @AuthenticationPrincipal SecurityUserDetails user
    );

    @Operation(summary = "그룹 목록 조회", description = "유저가 참여 중인 그룹 목록을 조회합니다.")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
            @ExampleObject(name = "그룹 목록 조회 성공", value = """
                    {
                    	"isSuccess": true,
                    	"code": 200,
                    	"message": "성공",
                    	"result": [
                                    {
                                      "groupId": 1,
                                      "groupName": "그룹1",
                                      "groupImgUrl": "string",
                                      "groupCode": "dcbe3295-1afd-4c8a-8d26-02ff64e25619",
                                      "groupUsers": [
                                        {
                                          "userId": 1,
                                          "userName": "유저1",
                                          "color": 5
                                        },
                                        {
                                          "userId": 2,
                                          "userName": "유저2",
                                          "color": 6
                                        }
                                      ]
                                    }
                                  ]
                    }
                    """)
    }))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {
            @ExampleObject(name = "요청 실패 - 로그인한 사용자를 찾을 수 없습니다.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "유저를 찾을 수 없습니다."
                    }
                    """)
    }))
    ResponseDto<List<GroupResponse.GetGroupDto>> getGroups(
            @AuthenticationPrincipal SecurityUserDetails user
    );

    @Operation(summary = "그룹 이름 수정", description = "그룹 이름을 수정하여 커스텀합니다. 기존 그룹명은 변경되지 않습니다.")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
            @ExampleObject(name = "그룹 이름 수정 성공", value = """
                    {
                    	"isSuccess": true,
                    	"code": 200,
                    	"message": "성공",
                    	"result": 1
                    }
                    """)
    }))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {
            @ExampleObject(name = "요청 실패 - 로그인한 사용자를 찾을 수 없습니다.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "유저를 찾을 수 없습니다."
                    }
                    """),
            @ExampleObject(name = "요청 실패 - 잘못된 그룹 ID 입니다. ", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "그룹을 찾을 수 없습니다."
                    }
                    """),
            @ExampleObject(name = "요청 실패 - 로그인 한 유저가 그룹의 구성원이 아닙니다. ", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "유저가 그룹에 포함되어 있지 않습니다."
                    }
                    """)
    }))
    ResponseDto<Long> modifyGroupName(
            @Valid @RequestBody GroupRequest.PatchGroupNameDto patchGroupNameDto,
            @AuthenticationPrincipal SecurityUserDetails user
    );

    @Operation(summary = "그룹 참여", description = "그룹 참여 코드를 입력하여 그룹에 참여합니다.")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
            @ExampleObject(name = "그룹 참여 성공", value = """
                    {
                    	"isSuccess": true,
                    	"code": 200,
                    	"message": "성공",
                    	"result": {
                    	    
                    	}
                    }
                    """)
    }))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {
            @ExampleObject(name = "요청 실패 - 로그인한 사용자를 찾을 수 없습니다.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "유저를 찾을 수 없습니다."
                    }
                    """),
            @ExampleObject(name = "요청 실패 - 입력한 참여코드로 그룹을 찾을 수 없습니다. ", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "그룹을 찾을 수 없습니다."
                    }
                    """),
            @ExampleObject(name = "요청 실패 - 로그인 한 유저가 이미 그룹의 구성원입니다.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "이미 가입한 그룹입니다."
                    }
                    """),
            @ExampleObject(name = "요청 실패 - 그룹의 인원이 다 차 참여할 수 없습니다.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": " 그룹의 인원이 다 찼습니다."
                    }
                    """)
    }))
    ResponseDto<GroupResponse.GroupParticipantDto> createGroupAndUser(
            @Parameter(name = "code", description = "그룹 참여 코드") @PathVariable(name = "code") String code,
            @AuthenticationPrincipal SecurityUserDetails user
    );

    @Operation(summary = "그룹 탈퇴", description = "그룹을 탈퇴합니다.")
    @ApiErrorCodes(value = {
            ErrorStatus.EMPTY_ACCESS_KEY,
            ErrorStatus.EXPIRATION_ACCESS_TOKEN,
            ErrorStatus.EXPIRATION_REFRESH_TOKEN,
            ErrorStatus.INTERNET_SERVER_ERROR
    })
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json", examples = {
            @ExampleObject(name = "그룹 탈퇴 성공", value = """
                    {
                    	"isSuccess": true,
                    	"code": 200,
                    	"message": "성공",
                    	"result": null
                    }
                    """)
    }))
    @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json", examples = {
            @ExampleObject(name = "요청 실패 - 로그인한 사용자를 찾을 수 없습니다.", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "유저를 찾을 수 없습니다."
                    }
                    """),
            @ExampleObject(name = "요청 실패 - 잘못된 그룹 ID 입니다. ", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "그룹을 찾을 수 없습니다."
                    }
                    """),
            @ExampleObject(name = "요청 실패 - 로그인 한 유저가 그룹의 구성원이 아닙니다. ", value = """
                    {
                    	"isSuccess": false,
                    	"code": 404,
                    	"message": "유저가 그룹에 포함되어 있지 않습니다."
                    }
                    """)
    }))
    ResponseDto<Void> removeGroupAndUser(
            @Parameter(name = "groupId", description = "그룹 ID") @PathVariable(name = "groupId") Long groupId,
            @AuthenticationPrincipal SecurityUserDetails user
    );
}
