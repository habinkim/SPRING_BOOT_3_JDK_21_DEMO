package com.habin.demo.account.adapter.input.web;

import com.habin.demo.account.application.port.input.command.AccountCommands;
import com.habin.demo.account.application.port.input.usecase.RegisterUseCase;
import com.habin.demo.common.config.Uris;
import com.habin.demo.common.hexagon.WebAdapter;
import com.habin.demo.common.response.Response;
import com.habin.demo.common.response.ResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@WebAdapter
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterUseCase registerUseCase;
    private final ResponseMapper responseMapper;

    /**
     * @return
     * @param<p> {@link com.habin.demo.account.application.port.input.command.AccountCommands.Register} command
     */
    @PostMapping(value = Uris.REGISTER)
    public ResponseEntity<Response<?>> register(@Valid @RequestBody final AccountCommands.Register command) {
        registerUseCase.register(command);
        return responseMapper.ok();
    }

}
