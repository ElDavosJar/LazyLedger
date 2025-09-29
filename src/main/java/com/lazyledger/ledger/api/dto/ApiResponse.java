package com.lazyledger.ledger.api.dto;

public record ApiResponse<T>(
    boolean success,
    String message,
    T data,
    String error
) {
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data, null);
    }

    public static <T> ApiResponse<T> error(String error) {
        return new ApiResponse<>(false, null, null, error);
    }
}