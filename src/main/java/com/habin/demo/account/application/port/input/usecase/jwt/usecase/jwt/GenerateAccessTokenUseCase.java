package com.habin.demo.account.application.port.input.usecase.jwt.usecase.jwt;

import org.springframework.security.core.userdetails.UserDetails;

public interface GenerateAccessTokenUseCase {

    String generateAccessToken(final UserDetails userDetails);

}
