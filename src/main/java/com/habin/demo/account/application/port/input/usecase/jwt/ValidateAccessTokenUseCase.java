package com.habin.demo.account.application.port.input.usecase.jwt;

public interface ValidateAccessTokenUseCase {

    Boolean validateAccessToken(String accessToken);

}
