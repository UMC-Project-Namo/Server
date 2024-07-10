package com.namo.spring.application.external.api.user.service;

import com.namo.spring.db.mysql.domains.user.domain.User;
import com.namo.spring.db.mysql.domains.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> readUser(Long userId) {
        return userRepository.findById(userId);
    }

}
