package com.namo.spring.application.external.api.user.service;

import com.namo.spring.application.external.api.category.service.CategoryMaker;
import com.namo.spring.application.external.api.user.converter.MemberConverter;
import com.namo.spring.application.external.api.user.dto.MemberDto;
import com.namo.spring.application.external.api.user.dto.MemberRequest;
import com.namo.spring.core.common.code.status.ErrorStatus;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import com.namo.spring.db.mysql.domains.user.entity.Term;
import com.namo.spring.db.mysql.domains.user.exception.MemberException;
import com.namo.spring.db.mysql.domains.user.repository.MemberRepository;
import com.namo.spring.db.mysql.domains.user.repository.TermRepository;
import com.namo.spring.db.mysql.domains.user.service.MemberService;
import com.namo.spring.db.mysql.domains.user.type.MemberStatus;
import com.namo.spring.db.mysql.domains.user.type.SocialType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberManageService {

    private final MemberRepository memberRepository;
    private final TermRepository termRepository;
    private final MemberService memberService;
    private final CategoryMaker categoryMaker;

    public List<Term> getTerms(Member member) {
        return termRepository.findTermsByMember(member);
    }

    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(ErrorStatus.NOT_FOUND_USER_FAILURE));
    }

    public Optional<Member> getMemberByEmailAndSocialType(String email, SocialType socialType) {
        return memberRepository.findMemberByEmailAndSocialType(email, socialType);
    }

    public Optional<Member> getMemberAuthId(String authId) {
        return memberRepository.findMemberByAuthId(authId);
    }

    public List<Member> getInactiveMember() {
        return memberRepository.findMembersByStatusAndDate(MemberStatus.INACTIVE, LocalDateTime.now().minusDays(3));
    }

    public void saveMember(Member member) {
        memberRepository.save(member);
    }

    public void removeMember(Member member) {
        memberRepository.delete(member);
    }

    public void checkEmailAndName(String email, String name) {
        if (email.isBlank() || name.isBlank()) {
            throw new MemberException(ErrorStatus.USER_POST_ERROR);
        }
    }

    public MemberDto.MemberCreationRecord updateExistingMember(Member existingMember, String socialRefreshToken) {
        existingMember.changeToActive();
        existingMember.updateSocialRefreshToken(socialRefreshToken);
        return new MemberDto.MemberCreationRecord(existingMember, false);
    }

    public MemberDto.MemberCreationRecord createNewMember(Member member) {
        checkEmailAndName(member.getEmail(), member.getName());
        log.debug("Creating new social member");
        Member savedMember = memberService.createMember(member);
        makeBaseCategory(savedMember);
        return new MemberDto.MemberCreationRecord(savedMember, true);
    }

    public Member createNewAppleMember(MemberRequest.AppleSignUpDto req, String authId, String appleRefreshToken) {
        log.debug("Creating new apple member");
        Member newMember = memberService.createMember(MemberConverter.toMember(
                authId,
                appleRefreshToken,
                SocialType.APPLE));
        makeBaseCategory(newMember);
        return newMember;
    }

    public Member updateExistingAppleMember(Member existingMember, String appleRefreshToken) {
        existingMember.changeToActive();
        existingMember.updateSocialRefreshToken(appleRefreshToken);
        return existingMember;
    }

    private void makeBaseCategory(Member member) {
        categoryMaker.makePersonalCategory(member);
        categoryMaker.makeMeetingCategory(member);
    }

    public List<String> getMemberTagsByNickname(String nickname) {
        List<Member> members = memberService.readMemberByNickname(nickname);
        return members.stream()
                .map(Member::getTag)
                .toList();
    }

    public void validateEmail(SocialType socialType, String email) {
        if (memberRepository.existsByEmailAndSocialType(email, socialType)) {
            throw new MemberException(ErrorStatus.DUPLICATE_EMAIL_FAILURE);
        }
    }
}
