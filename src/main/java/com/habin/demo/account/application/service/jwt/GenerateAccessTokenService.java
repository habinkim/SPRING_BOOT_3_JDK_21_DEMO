package com.habin.demo.account.application.service.jwt;

import com.habin.demo.account.application.port.input.usecase.jwt.GenerateAccessTokenUseCase;
import com.habin.demo.account.application.port.output.jwt.CreateAccessTokenPort;
import com.habin.demo.account.domain.value.SaveJwtToken;
import com.habin.demo.common.hexagon.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.stream.Collectors;

@UseCase
@RequiredArgsConstructor
public class GenerateAccessTokenService implements GenerateAccessTokenUseCase {

    private final CreateAccessTokenPort createAccessTokenPort;

    @Override
    public String generateAccessToken(final UserDetails userDetails) {
        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        SaveJwtToken saveJwtToken = new SaveJwtToken(userDetails.getUsername(), authorities);
        return createAccessTokenPort.createAccessToken(saveJwtToken);
    }
}
