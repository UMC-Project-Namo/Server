package com.namo.spring.db.mysql.domains.user.service;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository userRepository;

	@Transactional
	public Member createMember(Member member) {
		return userRepository.save(member);
	}

	@Transactional(readOnly = true)
	public Optional<Member> readMember(Long memberId) {
		return userRepository.findById(memberId);
	}

	@Transactional
	public void deleteMember(Long memberId) {
		userRepository.deleteById(memberId);
	}

	@Transactional
	public void deleteMember(Member member) {
		userRepository.delete(member);
	}
}
