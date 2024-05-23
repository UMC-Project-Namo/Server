package com.example.namo2.domain.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import com.example.namo2.domain.user.domain.constant.SocialType;
import com.example.namo2.domain.user.domain.constant.UserStatus;

import com.example.namo2.global.common.entity.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(nullable = false, length = 20)
	private String name;

	@Column(nullable = false, length = 50, unique = true)
	private String email;

	@Column
	private String birthday;

	@Column(name = "refresh_token")
	private String refreshToken;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "VARCHAR(15) DEFAULT 'ACTIVE'")
	private UserStatus status;

	@Enumerated(EnumType.STRING)
	private SocialType socialType;

	private String socialRefreshToken;

	@Builder
	public User(Long id, String name, String email, String birthday, String refreshToken, UserStatus status,
		SocialType socialType, String socialRefreshToken) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.birthday = birthday;
		this.refreshToken = refreshToken;
		this.status = status;
		this.socialType = socialType;
		this.socialRefreshToken = socialRefreshToken;
	}

	public void updateRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public void updateSocialRefreshToken(String refreshToken) {
		this.socialRefreshToken = socialRefreshToken;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}
}
