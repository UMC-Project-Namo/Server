package com.namo.spring.application.external.global.common.security.authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.namo.spring.application.external.api.user.service.UserService;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.exception.AuthException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService {
	private final UserService userService;

	public UserDetails loadUserByUserId(Long userId) {
		return userService.readUser(userId)
			.map(SecurityUserDetails::from)
			.orElseThrow(() -> new AuthException(ErrorStatus.NOT_FOUND_USER_FAILURE));
	}
}
