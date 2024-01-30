package com.habin.demo.account.adapter.output.persistence.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@RedisHash("RefreshToken")
public final class RefreshToken implements Serializable, JwtToken {

    private String username;
    private String token;

}
