package com.habin.demo.account.adapter.input.rest;

import com.habin.demo.account.adapter.output.persistence.AccountJpaEntity;
import com.habin.demo.account.application.port.input.usecase.LogoutUseCase;
import com.habin.demo.account.domain.state.CurrentUser;
import com.habin.demo.common.config.Uris;
import com.habin.demo.common.hexagon.WebAdapter;
import com.habin.demo.common.response.ResponseMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;

@WebAdapter
@RequiredArgsConstructor
public class LogoutController {

    private final LogoutUseCase logoutUseCase;
    private final ResponseMapper responseMapper;

    @PutMapping(value = Uris.LOGOUT)
    public void logout(HttpServletRequest request, @CurrentUser final AccountJpaEntity account) {
        logoutUseCase.logout();
    }

}
