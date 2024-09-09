package com.namo.spring.db.mysql.domains.user.repository;

import com.namo.spring.db.mysql.domains.user.entity.Anonymous;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnonymousRepository extends JpaRepository<Anonymous, Long> {

    List<Anonymous> findAnonymousByNickname(String nickname);
}
