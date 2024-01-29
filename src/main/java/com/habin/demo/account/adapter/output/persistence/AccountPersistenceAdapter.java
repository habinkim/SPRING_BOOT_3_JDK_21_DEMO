package com.habin.demo.account.adapter.output.persistence;

import com.habin.demo.account.application.port.output.LoadAccountPort;
import com.habin.demo.account.domain.CustomUser;
import com.habin.demo.common.exception.CommonApplicationException;
import com.habin.demo.common.hexagon.PersistenceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.habin.demo.common.response.MessageCode.USER_NOT_FOUND;

@PersistenceAdapter
@RequiredArgsConstructor
public class AccountPersistenceAdapter implements LoadAccountPort {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    @Transactional(readOnly = true)
    public CustomUser loadCustomUserByUsername(String username) {
        return loadAccountByUsername(username)
                .map(CustomUser::new)
                .orElseThrow(() -> new CommonApplicationException(USER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Optional<AccountJpaEntity> loadAccountByUsername(String username) {
        return accountRepository.findByUsernameActive(username);
    }
}
