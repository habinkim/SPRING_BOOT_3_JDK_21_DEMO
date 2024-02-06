package com.habin.demo.account.adapter.output.persistence;

import com.habin.demo.account.domain.behavior.SaveAccount;
import com.habin.demo.account.domain.state.AccountInfo;
import com.habin.demo.account.domain.state.RegisteredAccountInfo;
import com.habin.demo.common.config.BaseMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;

@Mapper(
        config = BaseMapperConfig.class,
        imports = {Collections.class, Role.class}
)
public abstract class AccountMapper {

    @Mapping(target = "roles", source = "roles", qualifiedByName = "roleValues")
    public abstract AccountInfo accountInfo(final AccountJpaEntity accountJpaEntity);

    @Mapping(target = "roles", expression = "java(Collections.singletonList(Role.CLIENT_0))")
    public abstract AccountJpaEntity fromSaveAccountValue(final SaveAccount saveAccount);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "roleValues")
    public abstract RegisteredAccountInfo registeredAccountInfo(AccountJpaEntity accountJpaEntity);

    @Named("roleValues")
    public List<String> roleValues(List<Role> roles) {
        return Role.roleValues(roles);
    }

    @Named("roleValue")
    public String roleValue(Role role) {
        return role.name();
    }
}
