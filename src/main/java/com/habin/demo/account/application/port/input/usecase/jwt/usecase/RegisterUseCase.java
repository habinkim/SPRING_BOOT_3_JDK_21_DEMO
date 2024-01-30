package com.habin.demo.account.application.port.input.usecase.jwt.usecase;

import com.habin.demo.account.application.port.input.usecase.jwt.command.AccountCommands;
import com.habin.demo.account.domain.state.RegisterResult;

public interface RegisterUseCase {
    RegisterResult register(final AccountCommands.Register command);
}
