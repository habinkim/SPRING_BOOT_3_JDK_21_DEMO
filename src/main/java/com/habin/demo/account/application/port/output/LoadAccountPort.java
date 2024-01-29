package com.habin.demo.account.application.port.output;

import com.habin.demo.account.domain.CustomUser;

public interface LoadAccountPort {

    CustomUser loadCustomUserByUsername(String username);

}
