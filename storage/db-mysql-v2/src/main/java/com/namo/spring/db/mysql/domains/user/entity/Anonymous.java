package com.namo.spring.db.mysql.domains.user.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.util.StringUtils;

import com.namo.spring.db.mysql.common.model.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Anonymous extends BaseTimeEntity implements User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Column(nullable = false, length = 50)
	private String name;

	@Column(nullable = false)
	private boolean nameVisible;

	@Column(nullable = false, length = 4)
	private String tag;

	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Column(nullable = false, length = 50)
	private String nickname;

	@Builder
	public Anonymous(String name, boolean nameVisible, String tag,
		String nickname) {
		if (!StringUtils.hasText(name))
			throw new IllegalArgumentException("name은 null이거나 빈 문자열일 수 없습니다.");
		if (!StringUtils.hasText(nickname))
			throw new IllegalArgumentException("nickname은 null이거나 빈 문자열일 수 없습니다.");
		if (!StringUtils.hasText(tag))
			throw new IllegalArgumentException("tag는 null이거나 빈 문자열일 수 없습니다.");
		this.name = name;
		this.nameVisible = Objects.requireNonNull(nameVisible, "nameVisible은 null일 수 없습니다.");
		this.tag = tag;
		this.nickname = nickname;
	}

}
