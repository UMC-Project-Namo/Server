package com.namo.spring.db.mysql.domains.user.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.type.SqlTypes;
import org.springframework.util.StringUtils;

import com.namo.spring.db.mysql.common.converter.UserRoleConverter;
import com.namo.spring.db.mysql.common.converter.UserStatusConverter;
import com.namo.spring.db.mysql.common.model.BaseTimeEntity;
import com.namo.spring.db.mysql.domains.user.type.UserRole;
import com.namo.spring.db.mysql.domains.user.type.UserStatus;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW(), status = 'INACVTIVE' WHERE id = ?")
public class User extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Column(name = "username", nullable = false, length = 50)
	private String username;

	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Column(name = "email", nullable = false, length = 50)
	private String email;

	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Column(name = "birthday", length = 10)
	private String birthday;  // "MM-DD"

	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Convert(converter = UserRoleConverter.class)
	@Column(name = "user_role", nullable = false, length = 50)
	private UserRole userRole;

	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Convert(converter = UserStatusConverter.class)
	@Column(name = "status", nullable = false, length = 50)
	private UserStatus status;

	@ColumnDefault("NULL")
	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@Builder
	public User(String username, String email, String birthday, UserRole userRole, UserStatus status) {
		if (!StringUtils.hasText(username))
			throw new IllegalArgumentException("username은 null이거나 빈 문자열일 수 없습니다.");
		else if (!StringUtils.hasText(email))
			throw new IllegalArgumentException("email은 null이거나 빈 문자열일 수 없습니다.");

		this.username = username;
		this.email = email;
		this.birthday = birthday;
		this.userRole = Objects.requireNonNull(userRole, "userRole은 null일 수 없습니다.");
		this.status = Objects.requireNonNull(status, "status는 null일 수 없습니다.");
	}

	@Override
	public String toString() {
		return "User ["
			+ "id='" + id + '\'' +
			", username='" + username + '\'' +
			", email='" + email + '\'' +
			", birthday='" + birthday + '\'' +
			", userRole='" + userRole + '\'' +
			", status='" + status + '\'' +
			", deletedAt='" + deletedAt + '\'' +
			']';
	}
}
