package com.namo.spring.db.mysql.domains.user.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.namo.spring.db.mysql.domains.user.domain.User;
import com.namo.spring.db.mysql.domains.user.type.SocialType;
import com.namo.spring.db.mysql.domains.user.type.UserStatus;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findUserByEmail(String email);

	Optional<User> findUserByEmailAndSocialType(String email, SocialType socialType);

	Optional<User> findUserByRefreshToken(String refreshToken);

	@Query("select u from User u where u.id in :ids")
	List<User> findUsersById(List<Long> ids);

	@Query("select u from User u where u.status in :status and u.lastModifiedDate < :localDateTime")
	List<User> findUsersByStatusAndDate(UserStatus status, LocalDateTime localDateTime);

}
