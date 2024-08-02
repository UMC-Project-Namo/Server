package com.namo.spring.db.mysql.domains.record.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
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

import com.namo.spring.db.mysql.domains.schedule.entity.Schedule;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "activity")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Activity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "schedule_id", nullable = false)
	private Schedule schedule;

	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Column(name = "title", nullable = false, length = 50)
	private String title;

	@Column(name = "total_amount", nullable = false)
	private BigDecimal totalAmount;

	@Builder
	public Activity(Schedule schedule, String title, BigDecimal totalAmount) {
		this.schedule = schedule;
		this.title = title;
		this.totalAmount = totalAmount;
	}
}
