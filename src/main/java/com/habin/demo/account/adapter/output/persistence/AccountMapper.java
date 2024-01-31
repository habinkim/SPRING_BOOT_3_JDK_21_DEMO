package com.habin.demo.account.adapter.output.persistence;

import com.habin.demo.account.domain.behavior.SaveAccount;
import com.habin.demo.account.domain.state.AccountInfo;
import com.habin.demo.account.domain.state.RegisteredAccountInfo;
import com.habin.demo.common.config.BaseMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;

@Mapper(
        config = BaseMapperConfig.class,
        imports = {Collections.class, Role.class}
)
public abstract class AccountMapper {

    public abstract AccountInfo accountInfo(final AccountJpaEntity accountJpaEntity);

    @Mapping(target = "roles", expression = "java(Collections.singletonList(Role.CLIENT_0))")
    public abstract AccountJpaEntity fromSaveAccountValue(final SaveAccount saveAccount);

    public abstract RegisteredAccountInfo registeredAccountInfo(AccountJpaEntity accountJpaEntity);
}
