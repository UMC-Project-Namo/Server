package com.namo.spring.application.external.global.common.security.authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.core.common.exception.AuthException;
import com.namo.spring.db.mysql.domains.user.service.MemberService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {
	private final MemberService userService;

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		return userService.readMember(Long.parseLong(userId))
			.map(SecurityUserDetails::from)
			.orElseThrow(() -> new AuthException(ErrorStatus.NOT_FOUND_USER_FAILURE));
	}
}
