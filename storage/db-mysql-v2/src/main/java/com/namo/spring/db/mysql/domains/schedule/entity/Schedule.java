package com.namo.spring.db.mysql.domains.schedule.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.util.StringUtils;

import com.namo.spring.db.mysql.common.model.BaseTimeEntity;
import com.namo.spring.db.mysql.domains.schedule.type.Location;
import com.namo.spring.db.mysql.domains.schedule.type.Period;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Schedule extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Column(nullable = false, length = 50)
	private String title;

	@Embedded
	private Period period;

	@Embedded
	private Location location;

	// 0: 개인, 1: 그룹
	@Column(nullable = false, columnDefinition = "TINYINT")
	private int scheduleType;

	@JdbcTypeCode(SqlTypes.VARCHAR)
	private String imageUrl;

	private Integer participantCount;

	private String participantNicknames;

	private String invitationUrl;

	@OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY)
	private List<Participant> participantList = new ArrayList<>();

	@Builder
	public Schedule(String title, Period period, Location location, int scheduleType, String imageUrl,
		Integer participantCount, String participantNicknames) {
		if (!StringUtils.hasText(title))
			throw new IllegalArgumentException("title은 null이거나 빈 문자열일 수 없습니다.");
		this.title = title;
		this.period = period;
		this.location = location;
		this.scheduleType = scheduleType;
		this.imageUrl = imageUrl;
		this.participantCount = participantCount;
		this.participantNicknames = participantNicknames;
	}

	public static Schedule of(String title, Period period, Location location, int scheduleType, String imageUrl,
		Integer participantCount, String participantNicknames) {
		return Schedule.builder()
			.title(title)
			.period(period)
			.location(location)
			.scheduleType(scheduleType)
			.imageUrl(imageUrl)
			.participantCount(participantCount)
			.participantNicknames(participantNicknames)
			.build();
	}

	public void updateContent(String title, Period period, Location location) {
		this.title = title;
		this.period = period;
		if (location != null) {
			this.location = location;
		}
	}

	public void addActiveParticipant(String nickname) {
		if (!StringUtils.hasText(nickname))
			throw new IllegalArgumentException("nickname은 null이거나 빈 문자열일 수 없습니다.");
		if (this.participantNicknames == null || this.participantNicknames == "") {
			this.participantNicknames = nickname;
		} else {
			this.participantNicknames += ", " + nickname;
		}
		if (this.participantCount == null) {
			this.participantCount = 1;
		} else {
			this.participantCount++;
		}
	}

	public void updateParticipant(String oldNickname, String newNickname) {
		if (!StringUtils.hasText(oldNickname) || !StringUtils.hasText(newNickname))
			throw new IllegalArgumentException("nickname은 null이거나 빈 문자열일 수 없습니다.");
		if (this.participantNicknames != null) {
			List<String> nicknames = new ArrayList<>(Arrays.asList(this.participantNicknames.split(", ")));
			int index = nicknames.indexOf(oldNickname);
			if (index != -1) {
				nicknames.set(index, newNickname);
				this.participantNicknames = String.join(", ", nicknames);
			}
		}
	}

	public void removeParticipants(List<String> nicknamesToRemove) {
		if (nicknamesToRemove == null || nicknamesToRemove.isEmpty()) {
			throw new IllegalArgumentException("삭제할 닉네임 목록이 비어있거나 null일 수 없습니다.");
		}

		List<String> currentNicknames = new ArrayList<>(Arrays.asList(this.participantNicknames.split(", ")));

		for (String nicknameToRemove : nicknamesToRemove) {
			currentNicknames.remove(nicknameToRemove); // 첫 번째 일치하는 닉네임만 제거
		}

		this.participantNicknames = String.join(", ", currentNicknames);
		this.participantCount = currentNicknames.size();
	}

	public void updateInvitationUrl(String invitationUrl) {
		this.invitationUrl = invitationUrl;
	}

}
