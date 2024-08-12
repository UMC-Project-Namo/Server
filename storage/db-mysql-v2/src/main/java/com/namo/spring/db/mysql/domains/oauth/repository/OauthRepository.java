package com.namo.spring.db.mysql.domains.oauth.repository;

import com.namo.spring.db.mysql.domains.oauth.entity.Oauth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OauthRepository extends JpaRepository<Oauth, Long> {
    Optional<Oauth> findByMemberId(Long userId);
}
