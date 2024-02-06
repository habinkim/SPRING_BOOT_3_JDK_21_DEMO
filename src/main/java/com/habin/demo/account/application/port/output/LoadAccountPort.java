package com.habin.demo.account.application.port.output;

import com.habin.demo.account.domain.state.AccountInfo;
import com.habin.demo.account.domain.state.RegisteredAccountInfo;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

public interface LoadAccountPort {

    Optional<User> loadCustomUserByUsername(String username);

    Optional<AccountInfo> accountInfo(String username);

    Optional<RegisteredAccountInfo> registeredAccountInfo(String username);

}
