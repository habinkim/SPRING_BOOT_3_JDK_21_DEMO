package com.habin.demo.account.adapter.output.persistence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@AllArgsConstructor
public enum Role implements GrantedAuthority {

    CLIENT_0("ROLE_CLIENT_0z"),
    ARTIST("ROLE_ARTIST"),
    ADMIN("ROLE_ADMIN");

    private final String authority;

}
