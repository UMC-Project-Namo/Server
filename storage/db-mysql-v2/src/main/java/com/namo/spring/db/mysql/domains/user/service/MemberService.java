package com.namo.spring.db.mysql.domains.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.event.MemberCreatedEvent;
import com.namo.spring.db.mysql.domains.user.repository.MemberRepository;
import com.namo.spring.db.mysql.domains.user.type.MemberStatus;

import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Member createMember(Member member) {
        eventPublisher.publishEvent(new MemberCreatedEvent(member));
        return memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public Optional<Member> readMember(Long memberId) {
        return memberRepository.findById(memberId);
    }

    @Transactional(readOnly = true)
    public List<Member> readMemberByNickname(String nickname) {
        return memberRepository.findMembersByNickname(nickname);
    }

    public Optional<Member> readMemberByStatus(Long memberId, MemberStatus status){
        return memberRepository.findByIdAndStatus(memberId, status);
    }

    public Optional<Member> readMemberByStatus(String nickname, String tag, MemberStatus status){
        return memberRepository.findByNicknameAndTagAndStatus(nickname, tag, status);
    }
}
