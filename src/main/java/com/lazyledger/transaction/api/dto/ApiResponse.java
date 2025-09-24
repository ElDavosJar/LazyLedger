package com.lazyledger.transaction.api.dto;

public record ApiResponse<T>(
    String msg,
    T data
) {
    public static <T> ApiResponse<T> success(String msg, T data) {
        return new ApiResponse<>(msg, data);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("Success", data);
    }

    public static <T> ApiResponse<T> error(String msg) {
        return new ApiResponse<>(msg, null);
    }
}