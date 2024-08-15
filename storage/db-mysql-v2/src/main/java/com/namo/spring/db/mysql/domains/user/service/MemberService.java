package com.namo.spring.db.mysql.domains.user.service;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.exception.MemberException;
import com.namo.spring.db.mysql.domains.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DomainService
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public Member createMember(Member member) {
        return memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public Member readMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new MemberException(ErrorStatus.NOT_FOUND_USER_FAILURE)
        );
    }

    @Transactional(readOnly = true)
    public List<Member> readMembersById(List<Long> memberIds) {
        return memberRepository.findMembersById(memberIds);
    }

    @Transactional
    public void deleteMember(Long memberId) {
        memberRepository.deleteById(memberId);
    }

    @Transactional
    public void deleteMember(Member member) {
        memberRepository.delete(member);
    }
}
