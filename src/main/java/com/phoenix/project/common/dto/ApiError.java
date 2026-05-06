package com.phoenix.project.common.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ApiError {
    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;
    private List<String> details;

    public ApiError() {}

    public ApiError(int status, String error, String message, LocalDateTime timestamp, List<String> details) {
        this.status = status; this.error = error; this.message = message;
        this.timestamp = timestamp; this.details = details;
    }

    public static ApiError of(int status, String error, String message) {
        return new ApiError(status, error, message, LocalDateTime.now(), null);
    }

    public static ApiError of(int status, String error, String message, List<String> details) {
        return new ApiError(status, error, message, LocalDateTime.now(), details);
    }

    public int getStatus() { return status; }
    public String getError() { return error; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public List<String> getDetails() { return details; }
}
