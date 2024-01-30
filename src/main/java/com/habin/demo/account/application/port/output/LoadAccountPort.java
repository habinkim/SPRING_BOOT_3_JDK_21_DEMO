package com.habin.demo.account.application.port.output;

import com.habin.demo.account.domain.value.AccountInfo;
import com.habin.demo.account.domain.CustomUser;

import java.util.Optional;

public interface LoadAccountPort {

    Optional<CustomUser> loadCustomUserByUsername(String username);

    Optional<AccountInfo> accountInfo(String username);

}
