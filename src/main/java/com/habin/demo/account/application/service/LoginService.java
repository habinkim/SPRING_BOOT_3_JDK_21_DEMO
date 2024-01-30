package com.habin.demo.account.application.service;

import com.habin.demo.account.application.port.input.command.AccountCommands;
import com.habin.demo.account.application.port.input.usecase.LoginUseCase;
import com.habin.demo.account.domain.state.LoginResult;
import com.habin.demo.common.hexagon.UseCase;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {

    @Override
    public LoginResult login(AccountCommands.Login command) {
        return null;
    }
}
