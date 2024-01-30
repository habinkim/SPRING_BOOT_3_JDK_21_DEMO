package com.habin.demo.account.application.service;

import com.habin.demo.account.application.port.input.command.AccountCommands;
import com.habin.demo.account.application.port.input.usecase.RegisterUseCase;
import com.habin.demo.account.application.port.output.LoadAccountPort;
import com.habin.demo.account.application.port.output.SaveAccountPort;
import com.habin.demo.account.application.port.output.jwt.CreateAccessTokenPort;
import com.habin.demo.account.application.port.output.jwt.CreateRefreshTokenPort;
import com.habin.demo.account.domain.value.AccountInfo;
import com.habin.demo.account.domain.value.SaveAccount;
import com.habin.demo.account.domain.value.SaveJwtToken;
import com.habin.demo.common.exception.CommonApplicationException;
import com.habin.demo.common.hexagon.UseCase;
import com.habin.demo.common.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static com.habin.demo.common.response.MessageCode.USER_POLICY_ACCOUNT_REGISTERED;

@UseCase
@RequiredArgsConstructor
public class RegisterService implements RegisterUseCase {

    private final LoadAccountPort loadAccountPort;
    private final SaveAccountPort saveAccountPort;
    private final CreateAccessTokenPort createAccessTokenPort;
    private final CreateRefreshTokenPort createRefreshTokenPort;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void register(final AccountCommands.Register command) {
        loadAccountPort.accountInfo(command.username()).ifPresent(accountInfo -> {
            throw new CommonApplicationException(USER_POLICY_ACCOUNT_REGISTERED);
        });

        SaveAccount saveAccount = new SaveAccount(
                StringUtil.uuid(), command.username(), passwordEncoder.encode(command.password()),
                command.email(), command.nickname());
        AccountInfo savedAccount = saveAccountPort.saveAccount(saveAccount);

        String authorities = savedAccount.roles().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        SaveJwtToken saveJwtToken = new SaveJwtToken(savedAccount.username(), authorities);

        String accessToken = createAccessTokenPort.createAccessToken(saveJwtToken);
        String refreshToken = createRefreshTokenPort.createRefreshToken(saveJwtToken);


    }
}
