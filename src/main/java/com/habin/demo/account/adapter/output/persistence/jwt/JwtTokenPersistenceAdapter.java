package com.habin.demo.account.adapter.output.persistence.jwt;

import com.habin.demo.account.application.port.output.jwt.LoadUsernamePort;
import com.habin.demo.account.application.port.output.jwt.*;
import com.habin.demo.common.exception.CommonApplicationException;
import com.habin.demo.common.hexagon.PersistenceAdapter;
import com.habin.demo.common.property.JwtProperty;
import com.habin.demo.common.response.MessageCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.crypto.SecretKey;

import java.util.Date;

import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Account - Output Adapter - Persistence - JwtToken
 */
@PersistenceAdapter
@RequiredArgsConstructor
public class JwtTokenPersistenceAdapter implements
        LoadUsernamePort,
        LoadAccessTokenPort, LoadRefreshTokenPort,
        SaveAccessTokenPort, SaveRefreshTokenPort,
        CheckAccessTokenExpirePort, CheckRefreshTokenExpirePort,
        UpdateAccessTokenExpirePort, UpdateRefreshTokenExpirePort,
        DeleteAccessTokenPort, DeleteRefreshTokenPort,
        InitializingBean {

    private static final String ACCESS_TOKEN_KEY_PREFIX = "access_";
    private static final String REFRESH_TOKEN_KEY_PREFIX = "refresh_";

    private SecretKey secretKey;
    private final JwtProperty jwtProperty;
    private final UserDetailsService userDetailsService;

    private final RedisTemplate<String, AccessToken> redisTemplateAccess;
    private final RedisTemplate<String, RefreshToken> redisTemplateRefresh;
    private final RedisTemplate<String, Object> redisTemplateObject;

    @Override
    public void afterPropertiesSet() {
        this.secretKey = hmacShaKeyFor(jwtProperty.getSecret().getBytes(UTF_8));
    }

    @Override // LoadUsernamePort
    public String loadUsernameByToken(final String token) {
        Claims body = getPayload(token);
        return body.getSubject();
    }

    @Override // CheckAccessTokenExpirePort
    public Boolean isAccessTokenExpired(final String accessToken) {
        return validateJwtExpiration(accessToken, redisTemplateObject);
    }

    @Override // CheckRefreshTokenExpirePort
    public Boolean isRefreshTokenExpired(final String refreshToken) {
        return validateJwtExpiration(refreshToken, redisTemplateRefresh);
    }

    @Override // UpdateAccessTokenExpirePort
    public void updateAccessTokenExpire(final String username, final Date expireDate) {
        redisTemplateAccess.expireAt(getAccessTokenKey(username), expireDate);
    }

    @Override // UpdateRefreshTokenExpirePort
    public void updateRefreshTokenExpire(final String username, final Date expireDate) {
        redisTemplateRefresh.expireAt(getRefreshTokenKeyPrefix(username), expireDate);
    }

    private Boolean validateJwtExpiration(final String token, final RedisTemplate<String, ?> redisTemplate) {
        try {
            final Date expiration = getPayload(token).getExpiration();
            Long expireTime = redisTemplate.getExpire(token);
            return expiration.before(new Date()) || (expireTime != null && expireTime >= 0);
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    private Claims getPayload(final String token) {
        try {
            return Jwts.parser().verifyWith(secretKey)
                    .sig().add(Jwts.SIG.HS512)
                    .and()
                    .build()
                    .parseSignedClaims(token).getPayload();
        } catch (IllegalArgumentException e) {
            throw new CommonApplicationException(MessageCode.EXCEPTION_AUTHENTICATION_INVALID_TOKEN);
        }
    }

    private static String getAccessTokenKey(final String username) {
        return ACCESS_TOKEN_KEY_PREFIX + username;
    }

    private static String getRefreshTokenKeyPrefix(final String username) {
        return REFRESH_TOKEN_KEY_PREFIX + username;
    }
}
