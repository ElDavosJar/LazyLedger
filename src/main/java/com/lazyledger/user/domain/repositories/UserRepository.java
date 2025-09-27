package com.lazyledger.user.domain.repositories;

import com.lazyledger.commons.UserId;
import com.lazyledger.user.domain.entities.User;
import com.lazyledger.user.domain.entities.vo.Email;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(UserId id);

    Optional<User> findByEmail(Email email);

    List<User> findAll();

    void delete(UserId id);
}