package com.namo.spring.db.mysql.domains.user.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

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
public class Term extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Column(nullable = false, length = 250)
	private String content;

	@Column(nullable = false)
	private boolean agree;

	private LocalDateTime agreeAt;

	@Builder
	public Term(Member user, String content, boolean agree, LocalDateTime agreeAt) {
		if (!StringUtils.hasText(content))
			throw new IllegalArgumentException("content는 null이거나 빈 문자열일 수 없습니다.");

		this.member = Objects.requireNonNull(user, "member은 null일 수 없습니다.");
		this.content = content;
		this.agree = Objects.requireNonNull(agree, "agree은 null일 수 없습니다.");
		this.agreeAt = agreeAt;
	}

	public void checkAgree() {
		this.agree = true;
		this.agreeAt = LocalDateTime.now();
	}

	public void checkDisagree() {
		this.agree = false;
		this.agreeAt = null;
	}

}
