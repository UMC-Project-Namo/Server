package com.namo.spring.application.external.domain.user.domain;

import java.time.LocalDateTime;

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

import com.namo.spring.application.external.domain.user.domain.constant.Content;
import com.namo.spring.application.external.global.common.entity.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 네이밍 마음에 드는지 좀 봐주세요.
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Term extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "term_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Enumerated(EnumType.STRING)
	private Content content;

	@Column(name = "is_check", nullable = false, columnDefinition = "TINYINT(1)")
	private Boolean isCheck;

	@Builder
	public Term(Long id, User user, Content content, Boolean isCheck) {
		this.id = id;
		this.user = user;
		this.content = content;
		this.isCheck = isCheck;
	}

	public void update() {
		this.lastModifiedDate = LocalDateTime.now();
	}
}
