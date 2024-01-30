package com.habin.demo.account.application.service;

import com.habin.demo.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtUtil jwtUtil;

//    private final JwtProperty jwtProperty;
//
//    @Transactional
//    public TokenInfoVo create(UserDetails userDetails) {
//        String accessToken = jwtUtil.generateAccessToken(userDetails);
//        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
//        return new TokenInfoVo(accessToken, refreshToken);
//    }
//
//    public void delete(HttpServletRequest request) {
//        jwtUtil.destroyAccessToken(request);
//        jwtUtil.destroyRefreshToken(request);
//    }
//
//    public String reissue(AuthenticationPayloads.RefreshRequest request) {
//        return jwtUtil.reissueAccessToken(request.refreshToken());
//    }
}
