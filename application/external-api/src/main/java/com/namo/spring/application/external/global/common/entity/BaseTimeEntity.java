package com.example.namo2.global.common.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseTimeEntity {
	@CreatedDate
	@Column(updatable = false, name = "created_date")
	private LocalDateTime createdDate;

	@LastModifiedDate
	@Column(name = "last_modified_date")
	protected LocalDateTime lastModifiedDate;
}
