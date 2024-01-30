package com.habin.demo.account.application.port.input.usecase.jwt;

public interface LoadUsernameFromTokenUseCase {
    String loadUsernameFromToken(final String token);
}
