package com.habin.demo.account.domain.state;

import com.habin.demo.account.adapter.output.persistence.Role;
import com.habin.demo.common.response.BasePayload;

import java.util.List;

public record RegisterResult(
        String username,
        List<Role> roles,
        String accessToken,
        String refreshToken
) implements BasePayload {
}
