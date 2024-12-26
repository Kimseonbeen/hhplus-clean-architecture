package io.hhplus.lecture.interfaces.api.dto.error;

public record ErrorResponse(
        String code,
        String message
) {
}