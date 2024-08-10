package com.namo.spring.db.mysql.domains.record.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

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
public class DiaryImage extends BaseTimeEntity implements Image {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "diary_id", nullable = false)
	private Diary diary;

	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Column(nullable = false)
	private String imageUrl;

	@Column(nullable = false)
	private Integer imageOrder;

	@Builder
	public DiaryImage(Diary diary, String imageUrl, Integer imageOrder) {
		if (!StringUtils.hasText(imageUrl))
			throw new IllegalArgumentException("imageUrl은 null이거나 빈 문자열일 수 없습니다.");

		this.diary = Objects.requireNonNull(diary, "diary은 null일 수 없습니다.");
		this.imageUrl = imageUrl;
		this.imageOrder = Objects.requireNonNull(imageOrder, "imageOrder은 null일 수 없습니다.");
	}

	public DiaryImage of(Diary diary, String imageUrl, Integer imageOrder) {
		return DiaryImage.builder()
			.diary(diary)
			.imageUrl(imageUrl)
			.imageOrder(imageOrder)
			.build();
	}

	@Override
	public void updateImageOrder(Integer imageOrder) {
		this.imageOrder = Objects.requireNonNull(imageOrder, "imageOrder은 null일 수 없습니다.");
	}

	@Override
	public void updateImageUrl(String imageUrl) {
		if (!StringUtils.hasText(imageUrl))
			throw new IllegalArgumentException("imageUrl은 null이거나 빈 문자열일 수 없습니다.");
		this.imageUrl = imageUrl;
	}
}
