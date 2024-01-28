package com.habin.demo.common.response;

import com.habin.demo.common.util.MessageSourceUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ResponseMapper {

    private final MessageSourceUtil messageSourceUtil;

    public ResponseEntity<Response<?>> ok() {
        return ResponseEntity.ok().body(new Response<>(MessageCode.SUCCESS.name(), MessageCode.SUCCESS.getCode(), null));
    }

    private <T extends BasePayload> ResponseEntity<Response<T>> ok(final MessageCode messageCode, final T source) {
        return ResponseEntity.ok().body(new Response<>(messageCode.name(), messageCode.getCode(), source));
    }

    private <T extends BasePayload> ResponseEntity<Response<T>> created(final MessageCode messageCode, final T source) {
        return ResponseEntity.created(null).body(new Response<>(messageCode.name(), messageCode.getCode(), source));
    }

    public <T extends BasePayload> ResponseEntity<Response<T>> ok(final T source) {
        return ok(MessageCode.SUCCESS, source);
    }

    public ResponseEntity<ExceptionResponse> error(final MessageCode messageCode) {
        return ResponseEntity
                .status(messageCode.getHttpStatus())
                .body(new ExceptionResponse(messageSourceUtil.getMessage(messageCode.getCode()), messageCode.getCode()));
    }

    public ResponseEntity<ExceptionResponse> error(final MessageCode messageCode, final String message) {
        return ResponseEntity
                .status(messageCode.getHttpStatus())
                .body(new ExceptionResponse(message, messageCode.getCode()));
    }

}
