package com.habin.demo.account.application.port.output;

import com.habin.demo.account.domain.state.AccountInfo;
import com.habin.demo.account.domain.state.CustomUser;

import java.util.Optional;

public interface LoadAccountPort {

    Optional<CustomUser> loadCustomUserByUsername(String username);

    Optional<AccountInfo> accountInfo(String username);

}
