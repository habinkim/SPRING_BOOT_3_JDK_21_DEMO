package com.habin.demo.account.application.port.input.usecase;

import com.habin.demo.account.application.port.input.command.AccountCommands;
import com.habin.demo.account.domain.state.RegisterResult;

public interface RegisterUseCase {
    RegisterResult register(final AccountCommands.Register command);
}
