package com.namo.spring.db.mysql.domains.diary.entity;

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
@Table(name = "diary_img")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityImg extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "diary_id", nullable = true)
	private Diary diary;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "activity_id", nullable = false)
	private Activity activity;

	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Column(name = "img_url", nullable = false)
	private String imgUrl;

	@Column(name = "img_order", nullable = false)
	private Integer imgOrder;

	@Builder
	public ActivityImg(Diary diary, Activity activity, String imgUrl, Integer imgOrder) {
		if (!StringUtils.hasText(imgUrl))
			throw new IllegalArgumentException("imgUrl은 null이거나 빈 문자열일 수 없습니다.");

		this.diary = diary;
		this.activity = Objects.requireNonNull(activity, "activity은 null일 수 없습니다.");
		this.imgUrl = imgUrl;
		this.imgOrder = Objects.requireNonNull(imgOrder, "imgOrder은 null일 수 없습니다.");
	}

	public ActivityImg of(Diary diary, Activity activity, String imgUrl, Integer imgOrder) {
		return ActivityImg.builder()
			.diary(diary)
			.activity(activity)
			.imgUrl(imgUrl)
			.imgOrder(imgOrder)
			.build();
	}

	public void updateImgUrl(String imgUrl) {
		if (!StringUtils.hasText(imgUrl))
			throw new IllegalArgumentException("imgUrl은 null이거나 빈 문자열일 수 없습니다.");

		this.imgUrl = imgUrl;
	}

	public void updateImgOrder(Integer imgOrder) {
		this.imgOrder = Objects.requireNonNull(imgOrder, "imgOrder은 null일 수 없습니다.");
	}

	public void updateDiary(Diary diary) {
		this.diary = Objects.requireNonNull(diary, "diary은 null일 수 없습니다.");
	}
}
