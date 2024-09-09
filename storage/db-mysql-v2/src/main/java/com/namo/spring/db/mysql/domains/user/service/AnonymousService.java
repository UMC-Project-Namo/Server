package com.namo.spring.db.mysql.domains.user.service;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.user.entity.Anonymous;
import com.namo.spring.db.mysql.domains.user.repository.AnonymousRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@DomainService
@RequiredArgsConstructor
public class AnonymousService {
    private final AnonymousRepository anonymousRepository;

    public Anonymous createAnonymous(Anonymous anonymous) {
        anonymousRepository.save(anonymous);
    }

    public List<Anonymous> findAnonymousNickname(String nickname) {
        return anonymousRepository.findAnonymousByNickname(nickname);
    }
}
