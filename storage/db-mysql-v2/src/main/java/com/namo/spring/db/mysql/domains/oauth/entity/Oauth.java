package com.namo.spring.db.mysql.domains.oauth.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.util.StringUtils;

import com.namo.spring.db.mysql.common.converter.ProviderConverter;
import com.namo.spring.db.mysql.common.model.BaseTimeEntity;
import com.namo.spring.db.mysql.domains.user.entity.User;
import com.namo.spring.db.mysql.domains.oauth.type.Provider;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "oauth")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Oauth extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Convert(converter = ProviderConverter.class)
	@Column(name = "provider", nullable = false, length = 50)
	private Provider provider;

	@Column(name = "provider_id", nullable = false)
	private String providerId;

	@Column(name = "login_id", nullable = false)
	private String loginId;

	@Column(name = "social_refresh_token")
	private String socialRefreshToken;

	@Builder
	public Oauth(Provider provider, String providerId, String loginId, String socialRefreshToken, User user) {
		if (!StringUtils.hasText(providerId))
			throw new IllegalArgumentException("providerId은 null이거나 빈 문자열일 수 없습니다.");
		else if (!StringUtils.hasText(loginId))
			throw new IllegalArgumentException("loginId은 null이거나 빈 문자열일 수 없습니다.");

		this.provider = Objects.requireNonNull(provider, "provider은 null일 수 없습니다.");
		this.providerId = providerId;
		this.loginId = loginId;
		this.socialRefreshToken = socialRefreshToken;
		this.user = Objects.requireNonNull(user, "user은 null일 수 없습니다.");
	}

	@Override
	public String toString() {
		return "Oauth ["
			+ "id='" + id + "', "
			+ "provider='" + provider + "', "
			+ "providerId='" + providerId + "', "
			+ "loginId='" + loginId + "', "
			+ "socialRefreshToken='" + socialRefreshToken + "', "
			+ "user='" + user + "'"
			+ "]";
	}
}
