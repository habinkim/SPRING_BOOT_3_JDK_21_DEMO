package com.habin.demo.account.adapter.input.web;

import com.habin.demo.account.application.port.input.usecase.LogoutUseCase;
import com.habin.demo.common.hexagon.WebAdapter;
import lombok.RequiredArgsConstructor;

@WebAdapter
@RequiredArgsConstructor
public class LogoutController {

    private final LogoutUseCase logoutUseCase;

}
