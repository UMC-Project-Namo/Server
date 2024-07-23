package com.namo.spring.db.mysql.domains.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import com.namo.spring.db.mysql.common.model.BaseTimeEntity;
import com.namo.spring.db.mysql.domains.user.type.SocialType;
import com.namo.spring.db.mysql.domains.user.type.UserRole;
import com.namo.spring.db.mysql.domains.user.type.UserStatus;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(
	uniqueConstraints = {
		@UniqueConstraint(
			name = "emailAndSocialType",
			columnNames = {"email", "socialType"}
		)
	}
)
public class User extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(nullable = false, length = 20)
	private String name;

	@Column(nullable = false, length = 50)
	private String email;

	@Column
	private String birthday;

	@Column(name = "refresh_token")
	private String refreshToken;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "VARCHAR(15) DEFAULT 'ACTIVE'")
	private UserStatus status;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SocialType socialType;

	@Column(nullable = false)
	private String socialRefreshToken;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "USER")
	private UserRole userRole;

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
		this.userRole = UserRole.USER;
	}

	public void updateRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public void updateSocialRefreshToken(String socialRefreshToken) {
		this.socialRefreshToken = socialRefreshToken;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}
}
