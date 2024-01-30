package com.habin.demo.account.adapter.output.persistence;

import com.habin.demo.account.domain.value.AccountInfo;
import com.habin.demo.account.domain.value.SaveAccount;
import com.habin.demo.common.config.BaseMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapperConfig.class)
public abstract class AccountMapper {

    public abstract AccountInfo accountInfo(final AccountJpaEntity accountJpaEntity);

    public abstract AccountJpaEntity fromSaveAccountValue(final SaveAccount saveAccount);
}
