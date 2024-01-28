package com.habin.demo.account.adapter.output.security.service;

import com.habin.demo.common.property.JwtProperty;
import com.habin.demo.common.util.JwtUtil;
import com.surgepay.api.v3.security.vo.TokenInfoVo;
import com.surgepay.api.v3.user.dto.AuthenticationPayloads;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtUtil jwtUtil;

    private final JwtProperty jwtProperty;

    @Transactional
    public TokenInfoVo create(UserDetails userDetails) {
        String accessToken = jwtUtil.generateAccessToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        return new TokenInfoVo(accessToken, refreshToken);
    }

    public void delete(HttpServletRequest request) {
        jwtUtil.destroyAccessToken(request);
        jwtUtil.destroyRefreshToken(request);
    }

    public String reissue(AuthenticationPayloads.RefreshRequest request) {
        return jwtUtil.reissueAccessToken(request.refreshToken());
    }
}
