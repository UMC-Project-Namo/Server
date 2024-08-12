package com.namo.spring.db.mysql.domains.oauth.service;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.oauth.entity.Oauth;
import com.namo.spring.db.mysql.domains.oauth.repository.OauthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    public Optional<Oauth> readOauthByMemberId(Long userId) {
        return oauthRepository.findByMemberId(userId);
    }

    @Transactional
    public void deleteOauth(Long oauthId) {
        oauthRepository.deleteById(oauthId);
    }
}
