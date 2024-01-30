package com.habin.demo.account.application.port.output;

import com.habin.demo.account.domain.state.AccountInfo;
import com.habin.demo.account.domain.behavior.SaveAccount;

public interface SaveAccountPort {
    AccountInfo saveAccount(final SaveAccount saveAccount);
}
