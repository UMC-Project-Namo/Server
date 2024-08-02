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
@Table(name = "activity_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityImage extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "activity_id", nullable = false)
	private Activity activity;

	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Column(name = "image_url", nullable = false)
	private String imageUrl;

	@Column(name = "image_order", nullable = false)
	private Integer imageOrder;

	@Builder
	public ActivityImage(Activity activity, String imageUrl, Integer imageOrder) {
		if (!StringUtils.hasText(imageUrl))
			throw new IllegalArgumentException("imgUrl은 null이거나 빈 문자열일 수 없습니다.");
		this.activity = Objects.requireNonNull(activity, "activity은 null일 수 없습니다.");
		this.imageUrl = imageUrl;
		this.imageOrder = Objects.requireNonNull(imageOrder, "imgOrder은 null일 수 없습니다.");
	}

	public ActivityImage of(Diary diary, Activity activity, String imageUrl, Integer imageOrder) {
		return ActivityImage.builder()
			.activity(activity)
			.imageUrl(imageUrl)
			.imageOrder(imageOrder)
			.build();
	}

	public void updateImageUrl(String imageUrl) {
		if (!StringUtils.hasText(imageUrl))
			throw new IllegalArgumentException("imgUrl은 null이거나 빈 문자열일 수 없습니다.");
		this.imageUrl = imageUrl;
	}

	public void updateImgOrder(Integer imageOrder) {
		this.imageOrder = Objects.requireNonNull(imageOrder, "imgOrder은 null일 수 없습니다.");
	}

}
