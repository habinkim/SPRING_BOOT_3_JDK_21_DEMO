package com.habin.demo.account.adapter.input.rest;

import com.habin.demo.account.application.port.input.usecase.jwt.usecase.RefreshUseCase;
import com.habin.demo.common.hexagon.WebAdapter;
import lombok.RequiredArgsConstructor;

@WebAdapter
@RequiredArgsConstructor
public class RefreshController {

    private final RefreshUseCase refreshUseCase;

}
