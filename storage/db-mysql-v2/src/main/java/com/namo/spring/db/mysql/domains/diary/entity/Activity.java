package com.namo.spring.db.mysql.domains.diary.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
@Table(name = "activity")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Activity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Column(name = "title", nullable = false, length = 50)
	private String title;

	@JdbcTypeCode(SqlTypes.INTEGER)
	@Column(name = "amount", nullable = false)
	private Integer amount;

	@Builder
	public Activity(String title, Integer amount) {
		if(!StringUtils.hasText(title))
			throw new IllegalArgumentException("title은 null이거나 빈 문자열일 수 없습니다.");

		this.title = title;
		this.amount = Objects.requireNonNull(amount, "amount는 null일 수 없습니다.");
	}
}
