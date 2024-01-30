package com.habin.demo.account.adapter.input.web;

import com.habin.demo.account.application.port.input.usecase.LoginUseCase;
import com.habin.demo.common.hexagon.WebAdapter;
import lombok.RequiredArgsConstructor;

@WebAdapter
@RequiredArgsConstructor
public class LoginController {

    private final LoginUseCase loginUseCase;

}
