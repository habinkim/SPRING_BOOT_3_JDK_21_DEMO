package com.habin.demo.common.util;

import com.habin.demo.account.adapter.output.persistence.jwt.AccessToken;
import com.habin.demo.account.adapter.output.persistence.jwt.JwtToken;
import com.habin.demo.account.adapter.output.persistence.jwt.RefreshToken;
import com.habin.demo.common.exception.CommonApplicationException;
import com.habin.demo.common.property.JwtProperty;
import com.habin.demo.common.response.MessageCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil implements InitializingBean {

    public static final String ACCESS_TOKEN_KEY_PREFIX = "access_";
    public static final String REFRESH_TOKEN_KEY_PREFIX = "refresh_";

    private SecretKey secretKey;
    private final JwtProperty jwtProperty;
    private final UserDetailsService userDetailsService;

    private final RedisTemplate<String, JwtToken> redisTemplateJwt;
    private final RedisTemplate<String, AccessToken> redisTemplateAccess;
    private final RedisTemplate<String, RefreshToken> redisTemplateRefresh;
    private final RedisTemplate<String, Object> redisTemplateObject;

    @Override
    public void afterPropertiesSet() {
        this.secretKey = hmacShaKeyFor(jwtProperty.getSecret().getBytes(UTF_8));
    }

    public String getUsernameFromToken(final String token) {
        Claims body = getPayload(token);
        return body.getSubject();
    }

    public String getIdFromTokenSkipException(final String token) {
        Claims body = getPayloadsSkipException(token);
        return body.getSubject();
    }

    public Claims getPayload(final String token) {
        try {
            return Jwts.parser().verifyWith(secretKey)
                    .sig().add(Jwts.SIG.HS512)
                    .and()
                    .build()
                    .parseSignedClaims(token).getPayload();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new CommonApplicationException(MessageCode.EXCEPTION_AUTHENTICATION_INVALID_TOKEN);
        }
    }

    public Claims getPayloadsSkipException(final String token) {
        Claims claims = null;

        try {
            claims = Jwts.parser().verifyWith(secretKey)
                    .sig().add(Jwts.SIG.HS512)
                    .and()
                    .build()
                    .parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {
            claims = e.getClaims();
        }

        return claims;
    }

    public Boolean validate(final String token) {
        try {
            Jwts.parser().verifyWith(secretKey)
                    .sig().add(Jwts.SIG.HS512)
                    .and()
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
            throw new CommonApplicationException(MessageCode.EXCEPTION_AUTHENTICATION_INVALID_TOKEN);
        }
    }

    public String generateAccessToken(final UserDetails userDetails) {
        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + jwtProperty.getAccessTokenValidity());
        String tokenValue = Jwts.builder()
                .claim("role", authorities)
                .claim("tokenType", "Bearer")
                .id(UUID.randomUUID().toString())
                .subject(userDetails.getUsername())
                .issuedAt(now)
                .notBefore(now)
                .expiration(expireDate)
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();

        ValueOperations<String, AccessToken> operations = redisTemplateAccess.opsForValue();
        AccessToken buildAccessToken = AccessToken.builder().username(userDetails.getUsername()).token(tokenValue).build();

        String accessTokenKey = ACCESS_TOKEN_KEY_PREFIX + userDetails.getUsername();
        operations.set(accessTokenKey, buildAccessToken);
        redisTemplateAccess.expireAt(accessTokenKey, expireDate);

        return tokenValue;
    }

    public String generateRefreshToken(final UserDetails userDetails) {
        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + jwtProperty.getRefreshTokenValidity());
        String tokenValue = Jwts.builder()
                .claim("role", authorities)
                .claim("tokenType", "Bearer")
                .id(UUID.randomUUID().toString())
                .subject(userDetails.getUsername())
                .issuedAt(now)
                .notBefore(now)
                .expiration(expireDate)
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();

        ValueOperations<String, RefreshToken> operations = redisTemplateRefresh.opsForValue();
        RefreshToken buildRefreshToken = RefreshToken.builder().username(userDetails.getUsername()).token(tokenValue).build();

        String refreshTokenKey = REFRESH_TOKEN_KEY_PREFIX + userDetails.getUsername();
        operations.set(refreshTokenKey, buildRefreshToken);
        redisTemplateRefresh.expireAt(refreshTokenKey, expireDate);

        return tokenValue;
    }

    public Boolean isAccessTokenExpired(final String accessToken) {
        try {
            final Date expiration = getPayload(accessToken).getExpiration();
            Long expireTime = redisTemplateObject.getExpire(accessToken); // 로그아웃하면서 만료되었는지 체크
            return expiration.before(new Date()) || (expireTime != null && expireTime >= 0);
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public Boolean isRefreshTokenExpired(final String refreshToken) {
        try {
            final Date expiration = getPayload(refreshToken).getExpiration();
            Long expireTime = redisTemplateRefresh.getExpire(refreshToken);
            return expiration.before(new Date()) || (expireTime != null && expireTime >= 0);
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public String reissueAccessToken(String refreshTokenValue) {
        UserDetails userDetails = getUserDetails(refreshTokenValue);
        RefreshToken refreshToken = redisTemplateRefresh.opsForValue().get(REFRESH_TOKEN_KEY_PREFIX + userDetails.getUsername());

        if (refreshToken == null && refreshToken.getToken().equals(refreshTokenValue))
            throw new CommonApplicationException(MessageCode.EXCEPTION_AUTHENTICATION_INVALID_TOKEN);

        if (isRefreshTokenExpired(refreshToken.getToken()))
            throw new CommonApplicationException(MessageCode.EXCEPTION_EXPIRED_REFRESH_TOKEN);

        return generateAccessToken(userDetails);
    }

    public void destroyAccessToken(HttpServletRequest request) {
        log.info("destroy access token");
        String username = null;

        try {
            username = getUsernameFromToken(getAccessTokenFromRequest(request));
        } catch (IllegalArgumentException ignored) {
        } catch (ExpiredJwtException e) { // 만료
            username = e.getClaims().getSubject(); // 만료된 access token으로부터 user_id를 가져옴
            log.info("user_id from expired access token : " + username);
        }

        redisTemplateAccess.delete(ACCESS_TOKEN_KEY_PREFIX + username);

        ValueOperations<String, Object> vopObject = redisTemplateObject.opsForValue();
        String accessToken = getAccessTokenFromRequest(request);
        vopObject.set(accessToken, true);
        redisTemplateObject.expire(accessToken, jwtProperty.getAccessTokenValidityDuration());

    }

    public void destroyRefreshToken(HttpServletRequest request) {
        log.info("destroy refresh token");
        String username = null;

        try {
            username = getUsernameFromToken(getAccessTokenFromRequest(request));
        } catch (IllegalArgumentException ignored) {
        } catch (ExpiredJwtException e) { // 만료
            username = e.getClaims().getSubject(); // 만료된 access token으로부터 userna를 가져옴
            log.info("user_id from expired access token : " + username);
        }

        redisTemplateRefresh.delete(REFRESH_TOKEN_KEY_PREFIX + username);
    }

    public String getAccessTokenFromRequest(final HttpServletRequest request) {
        String accessToken = Optional.ofNullable(request.getHeader(AUTHORIZATION))
                .orElseThrow(() -> new CommonApplicationException(MessageCode.EXCEPTION_AUTHENTICATION_TOKEN_NOT_FOUND));

        return removeBearerPrefix(accessToken);
    }

    private UserDetails getUserDetails(String token) {
        return userDetailsService.loadUserByUsername(getUsernameFromToken(token));
    }

    public PreAuthenticatedAuthenticationToken getAuthentication(final String token) {
        UserDetails userDetails = getUserDetails(token);
        return new PreAuthenticatedAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public String removeBearerPrefix(String accessTokenFromRequest) {
        return Pattern.matches("^Bearer .*", accessTokenFromRequest) ? accessTokenFromRequest.substring(7) : null;
    }
}
