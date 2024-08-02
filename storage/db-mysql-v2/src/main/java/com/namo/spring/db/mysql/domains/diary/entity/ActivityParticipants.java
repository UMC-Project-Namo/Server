package com.namo.spring.db.mysql.domains.diary.entity;

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

import com.namo.spring.db.mysql.domains.schedule.entity.Participants;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "activity_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class ActivityParticipants {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@Column(name = "participants_id", nullable = false)
	private Participants participants;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "activity_id", nullable = false)
	private Activity activity;

	@Column(name = "total_amount", nullable = false)
	private BigDecimal amount;

	@Builder
	public ActivityParticipants(Participants participants, Activity activity) {
		this.participants = participants;
		this.activity = activity;
		// this.amount
	}
}
