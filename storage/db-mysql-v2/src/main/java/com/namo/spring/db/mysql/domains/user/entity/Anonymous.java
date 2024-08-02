package com.namo.spring.db.mysql.domains.user.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Anonymous extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Column(name = "name", nullable = false, length = 50)
	private String name;

	@Column(name = "name_visible", nullable = false)
	private boolean nameVisible;

	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Column(name = "birthday", length = 10)
	private String birthday;  // "MM-DD"

	@Column(name = "birthday_visible", nullable = false)
	private boolean birthdayVisible;

	@Column(name = "bio")
	@JdbcTypeCode(SqlTypes.VARCHAR)
	private String bio;

	@Builder
	public Anonymous(String name, boolean nameVisible, String birthday, boolean birthdayVisible, String bio) {
		if (!StringUtils.hasText(name))
			throw new IllegalArgumentException("name은 null이거나 빈 문자열일 수 없습니다.");
		this.name = name;
		this.nameVisible = Objects.requireNonNull(nameVisible, "nameVisible은 null일 수 없습니다.");
		this.birthday = birthday;
		this.birthdayVisible = Objects.requireNonNull(birthdayVisible, "birthdayVisible은 null일 수 없습니다.");
		this.bio = bio;
	}
	
}
