package com.lazyledger.user.domain.entities;

import com.lazyledger.commons.UserId;
import com.lazyledger.user.domain.entities.vo.UserName;
import com.lazyledger.user.domain.entities.vo.Email;

import java.time.Instant;
import java.util.Objects;

public class User {
    private final UserId id;
    private final UserName name;
    private final Email email;
    private final Instant createdAt;

    private User(UserId id, UserName name, Email email, Instant createdAt) {
        this.id = Objects.requireNonNull(id, "UserId must be non-null");
        this.name = Objects.requireNonNull(name, "UserName must be non-null");
        this.email = Objects.requireNonNull(email, "Email must be non-null");
        this.createdAt = Objects.requireNonNull(createdAt, "CreatedAt must be non-null");
    }

    // Factory method for creating a new User
    public static User create(UserId id, UserName name, Email email) {
        return new User(id, name, email, Instant.now());
    }

    // Rehydrate from persistence
    public static User rehydrate(UserId id, UserName name, Email email, Instant createdAt) {
        return new User(id, name, email, createdAt);
    }

    public UserId getId() {
        return id;
    }

    public UserName getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    // Update name
    public User changeName(UserName newName) {
        return new User(this.id, newName, this.email, this.createdAt);
    }

    // Update email
    public User changeEmail(Email newEmail) {
        return new User(this.id, this.name, newEmail, this.createdAt);
    }

    @Override
    public String toString() {
        return "{" +
                "\n  \"id\": \"" + id + "\"," +
                "\n  \"name\": \"" + name + "\"," +
                "\n  \"email\": \"" + email + "\"," +
                "\n  \"createdAt\": \"" + createdAt + "\"" +
                "\n}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}