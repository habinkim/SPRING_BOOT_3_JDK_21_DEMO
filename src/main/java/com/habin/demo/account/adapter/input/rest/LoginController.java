package com.habin.demo.account.adapter.input.rest;

import com.habin.demo.account.application.port.input.usecase.jwt.usecase.LoginUseCase;
import com.habin.demo.common.hexagon.WebAdapter;
import lombok.RequiredArgsConstructor;

@WebAdapter
@RequiredArgsConstructor
public class LoginController {

    private final LoginUseCase loginUseCase;

}
