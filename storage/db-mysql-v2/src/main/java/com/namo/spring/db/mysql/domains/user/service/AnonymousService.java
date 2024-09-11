package com.namo.spring.db.mysql.domains.user.service;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.user.dto.AnonymousInviteCodeQuery;
import com.namo.spring.db.mysql.domains.user.entity.Anonymous;
import com.namo.spring.db.mysql.domains.user.repository.AnonymousRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@DomainService
@RequiredArgsConstructor
public class AnonymousService {
    private final AnonymousRepository anonymousRepository;

    public Anonymous createAnonymous(Anonymous anonymous) {
        return anonymousRepository.save(anonymous);
    }

    public List<Anonymous> findAnonymousNickname(String nickname) {
        return anonymousRepository.findAnonymousByNickname(nickname);
    }

    public Optional<Anonymous> findAnonymousByInviteCode(String code) {
        return anonymousRepository.findAnonymousByInviteCode(code);
    }

    public List<AnonymousInviteCodeQuery> findAllInviteCodes() {
        return anonymousRepository.findAllInviteCodes();
    }
}
