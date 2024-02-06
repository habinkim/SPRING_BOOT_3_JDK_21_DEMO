package com.habin.demo.account.domain.state;

import java.util.List;

public record RegisteredAccountInfo(
        String username,
        String password,
        List<String> roles
) {
}
