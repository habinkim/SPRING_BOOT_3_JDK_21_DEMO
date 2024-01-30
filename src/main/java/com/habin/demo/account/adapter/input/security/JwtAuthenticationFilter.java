package com.habin.demo.account.adapter.input.security;

import com.habin.demo.account.application.port.input.usecase.jwt.LoadUsernameFromTokenUseCase;
import com.habin.demo.account.application.port.input.usecase.jwt.ValidateAccessTokenUseCase;
import com.habin.demo.common.config.Uris;
import com.habin.demo.common.exception.CommonApplicationException;
import com.habin.demo.common.response.ExceptionResponse;
import com.habin.demo.common.response.MessageCode;
import com.habin.demo.common.util.i18n.MessageSourceUtil;
import com.habin.demo.common.util.ObjectUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final ValidateAccessTokenUseCase validateAccessTokenUseCase;
    private final LoadUsernameFromTokenUseCase loadUsernameFromTokenUseCase;

    private final ObjectUtil objectUtil;
    private final MessageSourceUtil messageSourceUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String accessToken = getAccessTokenFromRequest(request);

            if (validateAccessTokenUseCase.validateAccessToken(accessToken)) {
                PreAuthenticatedAuthenticationToken authentication = getAuthentication(accessToken);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (ExpiredJwtException e) {
            log.error("ExpiredJwtException: {}", e.getMessage());
            response.setStatus(401);
            response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "GET,POST,DELETE,PUT,OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "*");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Max-Age", "180");

            String message = objectUtil.objectToJson(
                    new ExceptionResponse(messageSourceUtil.getMessage(MessageCode.EXCEPTION_EXPIRED_TOKEN.getCode()),
                            MessageCode.EXCEPTION_EXPIRED_TOKEN.getCode()));
            response.getWriter().write(message);
            return;
        } catch (Exception e) { // 이부분은 망가진 토큰일때
            log.error("cannot set user, exception \n", e);
            log.error("cannot set user, exception message \n{}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        Optional<String> matchedUri = Arrays.stream(Uris.PERMIT_ALL_URIS)
                .filter(uri -> uri.equals(request.getServletPath()))
                .findFirst();

        return matchedUri.isPresent();
    }

    private String getAccessTokenFromRequest(final HttpServletRequest request) {
        String accessToken = Optional.ofNullable(request.getHeader(AUTHORIZATION))
                .orElseThrow(() -> new CommonApplicationException(MessageCode.EXCEPTION_AUTHENTICATION_TOKEN_NOT_FOUND));

        return removeBearerPrefix(accessToken);
    }

    private String removeBearerPrefix(String accessTokenFromRequest) {
        return Pattern.matches("^Bearer .*", accessTokenFromRequest) ? accessTokenFromRequest.substring(7) : null;
    }

    private PreAuthenticatedAuthenticationToken getAuthentication(final String token) {
        UserDetails userDetails = getUserDetails(token);
        return new PreAuthenticatedAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private UserDetails getUserDetails(String token) {
        String username = loadUsernameFromTokenUseCase.loadUsernameFromToken(token);
        return userDetailsService.loadUserByUsername(username);
    }
}
