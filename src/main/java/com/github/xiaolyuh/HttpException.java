package com.github.xiaolyuh;

public class HttpException extends RuntimeException {
    private final int code;

    public HttpException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
