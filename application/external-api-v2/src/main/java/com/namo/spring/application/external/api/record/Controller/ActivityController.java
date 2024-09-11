package com.namo.spring.application.external.api.record.Controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.namo.spring.application.external.api.record.dto.ActivityResponse;
import com.namo.spring.application.external.global.common.security.authentication.SecurityUserDetails;
import com.namo.spring.core.common.response.ResponseDto;

import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "활동", description = "활동 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/activities")
public class ActivityController {

	@GetMapping("/{scheduleId}")
	public ResponseDto<List<ActivityResponse.ActivityInfoDto>> getActivity(
		@AuthenticationPrincipal SecurityUserDetails memberInfo,
		@PathVariable String scheduleId) {
		return null;
	}

}
