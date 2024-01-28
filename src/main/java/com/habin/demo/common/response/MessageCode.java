package com.habin.demo.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum MessageCode {

    SUCCESS(HttpStatus.OK, "0000"),
    CREATED(HttpStatus.CREATED, "0001"),
    ACCEPTED(HttpStatus.ACCEPTED, "0002"),
    NOT_OWNER(HttpStatus.UNAUTHORIZED, "9997"),
    NOT_FOUND_DATA(HttpStatus.BAD_REQUEST, "9998"),
    ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "9999"),
    EXCEPTION_ILLEGAL_ARGUMENT(HttpStatus.BAD_REQUEST, "9100"),


    // 사용자 관련
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "2000"),
    USER_POLICY_ACCOUNT_REGISTERED(HttpStatus.BAD_REQUEST, "2001"),
    USER_POLICY_NICKNAME_REGISTERED(HttpStatus.BAD_REQUEST, "2002"),
    USER_UNREGISTERED(HttpStatus.UNAUTHORIZED, "2003"),
    USER_POLICY_AUTH(HttpStatus.UNAUTHORIZED, "2004"),

    // 로그인 관련
    EXCEPTION_AUTHENTICATION_LOGIN_FAIL(HttpStatus.UNAUTHORIZED, "3001"),
    EXCEPTION_AUTHENTICATION_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "3002"),
    EXCEPTION_AUTHENTICATION_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "3003"),
    EXCEPTION_EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "3004"),
    EXCEPTION_REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "3005"),
    EXCEPTION_EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "3006");

    private HttpStatus httpStatus;
    private String code;

    public static Optional<MessageCode> get(String name) {
        return Arrays.stream(MessageCode.values())
                .filter(env -> env.name().equals(name))
                .findFirst();
    }

}