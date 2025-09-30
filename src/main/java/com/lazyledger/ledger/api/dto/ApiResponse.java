package com.lazyledger.ledger.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard API response wrapper")
public record ApiResponse<T>(
    @Schema(description = "Indicates if the operation was successful", example = "true")
    boolean success,

    @Schema(description = "Response message", example = "Operation completed successfully")
    String message,

    @Schema(description = "Response data (present only on success)")
    T data,

    @Schema(description = "Error message (present only on failure)")
    String error

) {
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data, null);
    }

    public static <T> ApiResponse<T> error(String error) {
        return new ApiResponse<>(false, null, null, error);
    }
}