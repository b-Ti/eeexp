package com.example.cscmp.framework;

/**
 * 错误代号和信息
 *
 */
public enum ErrorStatus {
    /**
     * OK
     */
    OK(200, "OK"),
    /**
     * FOUND
     */
    FOUND(302, "Found"),
    /**
     * BAD_REQUEST
     */
    BAD_REQUEST(400, "Bad Request"),
    /**
     * UNAUTHORIZED
     */
    UNAUTHORIZED(401, "Unauthorized"),
    /**
     * FORBIDDEN
     */
    FORBIDDEN(403, "Forbidden"),
    /**
     * NOT_FOUND
     */
    NOT_FOUND(404, "Not Found"),
    /**
     * INTERNAL_SERVER_ERROR
     */
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    /**
     * SERVICE_UNAVAILABLE
     */
    SERVICE_UNAVAILABLE(503, "Service Unavailable");

    private final int code;

    private final String message;

    ErrorStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
