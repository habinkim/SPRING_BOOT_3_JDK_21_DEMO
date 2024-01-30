package com.habin.demo.account.application.port.input.command;

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

    public record Login(
            @NotBlank
            String username,
            @NotBlank
            String password
    ) {

    }

}
