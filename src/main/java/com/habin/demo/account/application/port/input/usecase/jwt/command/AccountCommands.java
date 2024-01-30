package com.habin.demo.account.application.port.input.usecase.jwt.command;

import jakarta.validation.constraints.NotBlank;

public class AccountCommands {

    public record Register(
            @NotBlank
            String username,
            @NotBlank
            String password,
            @NotBlank
            String email,
            @NotBlank
            String nickname
    ) {

    }

}
