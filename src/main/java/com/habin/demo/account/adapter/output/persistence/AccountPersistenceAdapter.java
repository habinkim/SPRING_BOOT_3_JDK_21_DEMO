package com.habin.demo.account.adapter.output.persistence;

import com.habin.demo.account.application.port.output.LoadAccountPort;
import com.habin.demo.account.application.port.output.RecordLastLoginAtPort;
import com.habin.demo.account.application.port.output.SaveAccountPort;
import com.habin.demo.account.domain.behavior.SaveAccount;
import com.habin.demo.account.domain.state.CurrentAccount;
import com.habin.demo.account.domain.state.RegisteredAccount;
import com.habin.demo.common.hexagon.PersistenceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@PersistenceAdapter
@RequiredArgsConstructor
public class AccountPersistenceAdapter implements
        LoadAccountPort, SaveAccountPort,
        RecordLastLoginAtPort {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<User> loadCustomUserByUsername(String username) {
        return loadAccountByUsername(username)
                .map(account -> new User(account.getUsername(), account.getPassword(), account.getAuthorities()));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CurrentAccount> accountInfo(String username) {
        return loadAccountByUsername(username)
                .map(accountMapper::accountInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RegisteredAccount> registeredAccountInfo(String username) {
        return loadAccountByUsername(username)
                .map(accountMapper::registeredAccountInfo);
    }

    @Override
    @Transactional
    public void recordLastLoginAt(String username) {
        loadAccountByUsername(username)
                .ifPresent(account -> {
                    AccountJpaEntity updatedAccount = account.toBuilder()
                            .lastLoginAt(LocalDateTime.now()).build();
                    accountRepository.save(updatedAccount);
                });
    }

    @Transactional(readOnly = true)
    public Optional<AccountJpaEntity> loadAccountByUsername(String username) {
        return accountRepository.findByUsernameActive(username);
    }

    @Override
    @Transactional
    public CurrentAccount saveAccount(SaveAccount saveAccount) {
        AccountJpaEntity saved = accountRepository.save(accountMapper.fromSaveAccountValue(saveAccount));
        return accountMapper.accountInfo(saved);
    }
}
