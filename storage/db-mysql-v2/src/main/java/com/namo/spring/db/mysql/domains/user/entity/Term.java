package com.namo.spring.db.mysql.domains.user.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import org.hibernate.annotations.DynamicInsert;

import com.namo.spring.db.mysql.common.model.BaseTimeEntity;
import com.namo.spring.db.mysql.domains.user.type.Content;

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

	@Enumerated(EnumType.STRING)
	private Content content;

	@Column(nullable = false)
	private boolean agree;

	private LocalDateTime agreeAt;

	@Builder
	public Term(Member user, Content content, boolean agree, LocalDateTime agreeAt) {
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

	public void update() {
		this.updatedAt = LocalDateTime.now();
	}
}
