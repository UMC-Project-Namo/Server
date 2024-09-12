package com.namo.spring.db.mysql.domains.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.user.dto.AnonymousInviteCodeQuery;
import com.namo.spring.db.mysql.domains.user.entity.Anonymous;
import com.namo.spring.db.mysql.domains.user.repository.AnonymousRepository;

import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class AnonymousService {
    private final AnonymousRepository anonymousRepository;

    @Transactional
    public Anonymous createAnonymous(Anonymous anonymous) {
        return anonymousRepository.save(anonymous);
    }

    @Transactional(readOnly = true)
    public List<Anonymous> readAnonymousNickname(String nickname) {
        return anonymousRepository.findAnonymousByNickname(nickname);
    }

    @Transactional(readOnly = true)
    public Optional<Anonymous> readAnonymousByInviteCode(String code) {
        return anonymousRepository.findAnonymousByInviteCode(code);
    }

    @Transactional(readOnly = true)
    public Optional<Anonymous> readAnonymousByTagAndNickname(String tag, String nickname) {
        return anonymousRepository.findAnonymousByTagAndNickname(tag, nickname);
    }

    @Transactional(readOnly = true)
    public List<AnonymousInviteCodeQuery> readAllInviteCodes() {
        return anonymousRepository.findAllInviteCodes();
    }
}
