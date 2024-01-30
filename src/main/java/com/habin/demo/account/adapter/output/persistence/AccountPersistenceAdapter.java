package com.habin.demo.account.adapter.output.persistence;

import com.habin.demo.account.application.port.output.LoadAccountPort;
import com.habin.demo.account.application.port.output.SaveAccountPort;
import com.habin.demo.account.domain.state.CustomUser;
import com.habin.demo.account.domain.state.AccountInfo;
import com.habin.demo.account.domain.behavior.SaveAccount;
import com.habin.demo.common.hexagon.PersistenceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@PersistenceAdapter
@RequiredArgsConstructor
public class AccountPersistenceAdapter implements
        LoadAccountPort, SaveAccountPort {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomUser> loadCustomUserByUsername(String username) {
        return loadAccountByUsername(username)
                .map(CustomUser::new);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccountInfo> accountInfo(String username) {
        return loadAccountByUsername(username)
                .map(accountMapper::accountInfo);
    }

    @Transactional(readOnly = true)
    public Optional<AccountJpaEntity> loadAccountByUsername(String username) {
        return accountRepository.findByUsernameActive(username);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public AccountInfo saveAccount(SaveAccount saveAccount) {
        AccountJpaEntity saved = accountRepository.save(accountMapper.fromSaveAccountValue(saveAccount));
        return accountMapper.accountInfo(saved);
    }
}
