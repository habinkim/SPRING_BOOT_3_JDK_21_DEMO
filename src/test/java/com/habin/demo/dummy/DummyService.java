package com.habin.demo.dummy;


import com.habin.demo.account.adapter.output.persistence.AccountJpaEntity;
import com.habin.demo.account.adapter.output.persistence.AccountPersistenceAdapter;
import com.habin.demo.account.application.port.input.command.AccountCommands;
import com.habin.demo.account.application.port.input.usecase.RegisterUseCase;
import com.habin.demo.account.application.port.input.usecase.jwt.GenerateAccessTokenUseCase;
import com.habin.demo.account.application.port.input.usecase.jwt.GenerateRefreshTokenUseCase;
import com.habin.demo.account.domain.state.CustomUser;
import com.habin.demo.base.AbstractIntegrationTest;
import com.habin.demo.common.util.StringUtil;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;

@Getter
@Service
public class DummyService extends AbstractIntegrationTest {

    @Autowired
    private RegisterUseCase registerUseCase;

    @Autowired
    private GenerateAccessTokenUseCase generateAccessTokenUseCase;

    @Autowired
    private GenerateRefreshTokenUseCase generateRefreshTokenUseCase;

    @Autowired
    private AccountPersistenceAdapter accountPersistenceAdapter;

    private AccountJpaEntity defaultAccount = null;

    private String defaultUsername = null;
    private String defaultPassword = null;
    private String defaultEmail = null;

    private String defaultAccessToken = null;
    private String defaultRefreshToken = null;

    @Transactional
    public void initUser() throws NoSuchAlgorithmException {
        defaultUsername = StringUtil.randomAlphanumeric(10, 15);
        defaultPassword = StringUtil.randomAlphaNumericSymbol(15, 20);
        defaultEmail = StringUtil.randomAlphabetic(7) + "@" + StringUtil.randomAlphabetic(5) + ".com";
        String nickname = StringUtil.randomAlphabetic(5);

        AccountCommands.Register command = new AccountCommands.Register(defaultUsername, defaultPassword, defaultEmail, nickname);

        registerUseCase.register(command);

        defaultAccount = accountPersistenceAdapter.loadAccountByUsername(defaultUsername)
                .orElseThrow(() -> new RuntimeException("initUser 실패"));
    }

    @Transactional
    public void login(final AccountJpaEntity account) {
        defaultAccessToken = generateAccessTokenUseCase.generateAccessToken(account);
        defaultRefreshToken = generateRefreshTokenUseCase.generateRefreshToken(account);

        CustomUser customUser = new CustomUser(account);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(customUser, null, customUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
