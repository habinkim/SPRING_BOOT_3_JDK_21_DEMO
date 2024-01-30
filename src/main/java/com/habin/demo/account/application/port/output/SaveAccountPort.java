package com.habin.demo.account.application.port.output;

import com.habin.demo.account.domain.value.AccountInfo;
import com.habin.demo.account.domain.value.SaveAccount;

public interface SaveAccountPort {
    AccountInfo saveAccount(final SaveAccount saveAccount);
}
