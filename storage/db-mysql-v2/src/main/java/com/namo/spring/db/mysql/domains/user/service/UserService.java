package com.namo.spring.db.mysql.domains.user.service;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import com.namo.spring.core.common.annotation.DomainService;
import com.namo.spring.db.mysql.domains.user.entity.User;
import com.namo.spring.db.mysql.domains.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	@Transactional
	public User createUser(User user) {
		return userRepository.save(user);
	}

	@Transactional(readOnly = true)
	public Optional<User> readUser(Long userId) {
		return userRepository.findById(userId);
	}

	@Transactional
	public void deleteUser(Long userId) {
		userRepository.deleteById(userId);
	}

	@Transactional
	public void deleteUser(User user) {
		userRepository.delete(user);
	}
}
