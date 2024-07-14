package com.namo.spring.db.mysql.domains.oauth.service;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.oauth.entity.Oauth;
import com.namo.spring.db.mysql.domains.oauth.repository.OauthRepository;

import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class OauthService {
	private final OauthRepository oauthRepository;

	@Transactional
	public Oauth createOauth(Oauth oauth) {
		return oauthRepository.save(oauth);
	}

	@Transactional(readOnly = true)
	public Optional<Oauth> readOauth(Long oauthId) {
		return oauthRepository.findById(oauthId);
	}

	@Transactional(readOnly = true)
	public Optional<Oauth> readOauthByUserId(Long userId) {
		return oauthRepository.findByUserId(userId);
	}

	@Transactional
	public void deleteOauth(Long oauthId) {
		oauthRepository.deleteById(oauthId);
	}
}
