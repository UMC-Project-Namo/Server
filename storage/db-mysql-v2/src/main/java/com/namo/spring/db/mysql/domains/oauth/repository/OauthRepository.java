package com.namo.spring.db.mysql.domains.oauth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.namo.spring.db.mysql.domains.oauth.entity.Oauth;

public interface OauthRepository extends JpaRepository<Oauth, Long> {
    Optional<Oauth> findByMemberId(Long userId);
}
