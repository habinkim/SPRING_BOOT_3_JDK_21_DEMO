package com.habin.demo.account.application.port.output.jwt;

import com.habin.demo.account.domain.value.SaveJwtToken;
import org.springframework.security.core.userdetails.UserDetails;

public interface CreateRefreshTokenPort {
    String createRefreshToken(final SaveJwtToken saveJwtToken);
}
