package com.habin.demo.account.adapter.output.security.domain;

import com.habin.demo.account.adapter.output.persistence.AccountJpaEntity;
import lombok.Getter;
import org.springframework.security.core.userdetails.User;

@Getter
public class CustomUser extends User {

    private final AccountJpaEntity account;

    public CustomUser(AccountJpaEntity accountJpaEntity) {
        super(accountJpaEntity.getUsername(), accountJpaEntity.getPassword(), accountJpaEntity.getAuthorities());
        this.account = accountJpaEntity;
    }

}
