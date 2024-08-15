package com.namo.spring.db.mysql.domains.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;

	@Transactional
	public Member createMember(Member member) {
		return memberRepository.save(member);
	}

	@Transactional(readOnly = true)
	public Optional<Member> readMember(Long memberId) {
		return memberRepository.findById(memberId);
	}

	@Transactional
	public void deleteMember(Long memberId) {
		memberRepository.deleteById(memberId);
	}

	@Transactional
	public void deleteMember(Member member) {
		memberRepository.delete(member);
	}

	@Transactional(readOnly = true)
	public List<Member> readMemberByNickname(String nickname) {
		return memberRepository.findMembersByNickname(nickname);
	}
}
