package com.habin.demo.account.adapter.input.web;

import com.habin.demo.account.application.port.input.usecase.RefreshUseCase;
import com.habin.demo.common.hexagon.WebAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class RefreshController {

    private final RefreshUseCase refreshUseCase;

}
