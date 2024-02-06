package com.habin.demo.account.application.service;

import com.habin.demo.account.application.port.input.command.AccountCommands;
import com.habin.demo.account.application.port.input.usecase.LoginUseCase;
import com.habin.demo.account.application.port.output.LoadAccountPort;
import com.habin.demo.account.application.port.output.RecordLastLoginAtPort;
import com.habin.demo.account.application.port.output.jwt.CreateAccessTokenPort;
import com.habin.demo.account.application.port.output.jwt.CreateRefreshTokenPort;
import com.habin.demo.account.domain.behavior.SaveJwtToken;
import com.habin.demo.account.domain.state.AccountInfo;
import com.habin.demo.account.domain.state.LoginResult;
import com.habin.demo.account.domain.state.RegisteredAccountInfo;
import com.habin.demo.common.exception.CommonApplicationException;
import com.habin.demo.common.hexagon.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static com.habin.demo.common.response.MessageCode.ACCOUNT_NOT_FOUND;

@Service
@UseCase
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {

    private final LoadAccountPort loadAccountPort;
    private final RecordLastLoginAtPort recordLastLoginAtPort;
    private final CreateAccessTokenPort createAccessTokenPort;
    private final CreateRefreshTokenPort createRefreshTokenPort;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public LoginResult login(final AccountCommands.Login command) {
        RegisteredAccountInfo registeredAccountInfo = loadAccountPort.registeredAccountInfo(command.username())
                .orElseThrow(() -> new CommonApplicationException(ACCOUNT_NOT_FOUND));

        if (!passwordEncoder.matches(command.password(), registeredAccountInfo.password()))
            throw new CommonApplicationException(ACCOUNT_NOT_FOUND);

        String authorities = String.join(",", registeredAccountInfo.roles());

        SaveJwtToken saveJwtToken = new SaveJwtToken(registeredAccountInfo.username(), authorities);

        String accessToken = createAccessTokenPort.createAccessToken(saveJwtToken);
        String refreshToken = createRefreshTokenPort.createRefreshToken(saveJwtToken);

        recordLastLoginAtPort.recordLastLoginAt(registeredAccountInfo.username());

        return new LoginResult(registeredAccountInfo.username(), registeredAccountInfo.roles(), accessToken, refreshToken);
    }
}
