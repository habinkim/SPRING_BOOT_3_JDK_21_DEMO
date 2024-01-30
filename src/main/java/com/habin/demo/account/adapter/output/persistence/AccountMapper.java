package com.habin.demo.account.adapter.output.persistence;

import com.habin.demo.account.domain.state.AccountInfo;
import com.habin.demo.account.domain.behavior.SaveAccount;
import com.habin.demo.common.config.BaseMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapperConfig.class)
public abstract class AccountMapper {

    public abstract AccountInfo accountInfo(final AccountJpaEntity accountJpaEntity);

    public abstract AccountJpaEntity fromSaveAccountValue(final SaveAccount saveAccount);
}
