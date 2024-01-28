package com.habin.demo.common.response;

public record Response<T extends BasePayload>(String message, String code, T data) implements AbstractResponse {

    @Override
    public String getMessage() {
        return message();
    }

    @Override
    public String getCode() {
        return code();
    }
}
