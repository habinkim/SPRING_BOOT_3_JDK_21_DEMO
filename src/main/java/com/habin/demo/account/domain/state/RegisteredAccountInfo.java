package com.habin.demo.account.domain.state;

import com.habin.demo.account.adapter.output.persistence.Role;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RegisteredAccountInfo(
        String username,
        String password,
        List<Role> roles
) {
}
