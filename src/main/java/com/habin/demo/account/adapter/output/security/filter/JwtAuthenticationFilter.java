package com.habin.demo.account.adapter.output.security.filter;

import com.habin.demo.common.config.Uris;
import com.habin.demo.common.util.JwtUtil;
import com.habin.demo.common.response.ExceptionResponse;
import com.habin.demo.common.response.MessageCode;
import com.habin.demo.common.util.MessageSourceUtil;
import com.habin.demo.common.util.StringUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Profile("!dev")
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final StringUtil stringUtil;
    private final MessageSourceUtil messageSourceUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String accessToken = jwtUtil.getAccessTokenFromRequest(request);

            if (accessToken != null && jwtUtil.validate(accessToken) && !jwtUtil.isAccessTokenExpired(accessToken)) {
                PreAuthenticatedAuthenticationToken authentication = jwtUtil.getAuthentication(accessToken);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                if (!HttpMethod.OPTIONS.name().equals(request.getMethod())) {
                    log.info("===================================================");
                    log.info("AuthenticationJwtFilter.doFilterInternal >> ");
                    log.info("authentication, is login = {}", authentication.isAuthenticated());
                    log.info("authentication, name ={}", authentication.getName());
                    log.info("authentication, principal ={}", authentication.getPrincipal());
                    log.info("authentication, credentials ={}", authentication.getCredentials());
                    log.info("authentication, details ={}", authentication.getDetails());
                    log.info("===================================================");
                }
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

            String message = stringUtil.objectToJson(
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
}
