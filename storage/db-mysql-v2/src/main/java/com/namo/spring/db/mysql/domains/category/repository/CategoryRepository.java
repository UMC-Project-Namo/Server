package com.namo.spring.db.mysql.domains.category.repository;

import com.namo.spring.db.mysql.domains.category.entity.Category;
import com.namo.spring.db.mysql.domains.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c from Category c join fetch c.member where c.id = :id and c.member= :member")
    Optional<Category> findCategoryByMemberAndId(@Param("id") Long id, @Param("member") Member member);

    @Query("select c from Category c join fetch c.member where c.member= :member and c.type = '2'")
    Optional<Category> findMeetingCategoryByMember(@Param("member") Member member);
}
