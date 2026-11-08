package com.gameverse.api;

/**
 * Formato uniforme para los errores de la API REST.
 */
public class ApiError {

    private final int status;
    private final String error;
    private final String message;
    private final String path;
    private final String timestamp;

    public ApiError(int status, String error, String message, String path, String timestamp) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
