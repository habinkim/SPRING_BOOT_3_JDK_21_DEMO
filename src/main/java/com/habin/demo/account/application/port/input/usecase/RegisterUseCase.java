package com.habin.demo.account.application.port.input.usecase;

import com.habin.demo.account.application.port.input.command.AccountCommands;

public interface RegisterUseCase {
    void register(final AccountCommands.Register command);
}
