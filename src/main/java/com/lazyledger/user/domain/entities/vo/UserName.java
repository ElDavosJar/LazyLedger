package com.lazyledger.user.domain.entities.vo;

public record UserName(String value) {
    public UserName {
        if (value == null || value.isBlank() || value.length() < 2 || value.length() > 50) {
            throw new IllegalArgumentException("UserName must be between 2 and 50 characters and not blank");
        }
    }

    public static UserName of(String value) {
        return new UserName(value);
    }

    public boolean isSame(UserName other) {
        return this.value.equals(other.value);
    }

    @Override
    public String toString() {
        return value;
    }
}