package com.namo.spring.application.external.global.common.security.authentication;

import java.io.Serial;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.type.MemberStatus;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUserDetails implements UserDetails {
	@Serial
	private static final long serialVersionUID = 1L;

	private Long userId;
	private String username;
	private Collection<? extends GrantedAuthority> authorities;
	private boolean accountNonLocked;

	@JsonIgnore
	private boolean enabled;
	@JsonIgnore
	private String password;
	@JsonIgnore
	private boolean credentialsNonExpired;
	@JsonIgnore
	private boolean accountNonExpired;

	@Builder
	public SecurityUserDetails(
		Long userId,
		String username,
		Collection<? extends GrantedAuthority> authorities,
		boolean accountNonLocked
	) {
		this.userId = userId;
		this.username = username;
		this.authorities = authorities;
		this.accountNonLocked = accountNonLocked;
	}

	public static UserDetails from(Member user) {
		return SecurityUserDetails.builder()
			.userId(user.getId())
			.username(user.getName())
			.authorities(List.of(new CustomGrantedAuthority(user.getUserRole().getType())))
			.accountNonLocked(user.getStatus().equals(MemberStatus.INACTIVE))
			.build();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isEnabled() {
		throw new UnsupportedOperationException();
	}
}
