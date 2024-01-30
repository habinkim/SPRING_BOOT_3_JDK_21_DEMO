package com.habin.demo.account.application.service;

import com.habin.demo.account.application.port.input.usecase.LogoutUseCase;
import com.habin.demo.common.hexagon.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@UseCase
@RequiredArgsConstructor
public class LogoutService implements LogoutUseCase {
}
