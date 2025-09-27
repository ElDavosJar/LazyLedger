package com.lazyledger.user.domain.services;

import com.lazyledger.commons.UserId;
import com.lazyledger.user.domain.entities.User;
import com.lazyledger.user.domain.entities.vo.UserName;
import com.lazyledger.user.domain.entities.vo.Email;
import com.lazyledger.user.domain.repositories.UserRepository;

import java.util.UUID;

public class UserRegistrationService {

    private final UserRepository userRepository;

    public UserRegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(UserName name, Email email) {
        // Check if email already exists
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        // Generate new UserId
        UserId userId = UserId.of(UUID.randomUUID());

        // Create user
        User user = User.create(userId, name, email);

        // Save user
        return userRepository.save(user);
    }
}