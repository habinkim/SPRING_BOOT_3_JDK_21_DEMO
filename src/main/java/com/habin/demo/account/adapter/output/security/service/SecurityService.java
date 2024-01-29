package com.habin.demo.account.adapter.output.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SecurityService {

//    private final UserService userService;
//    private final RoleService roleService;
//    private final AbilityService abilityService;
//
//    private final SecurityMapper securityMapper;
//
//    private final PasswordEncoder passwordEncoder;
//    private final JwtTokenProvider tokenProvider;
//    private final JwtProperty jwtProperty;
//
//    @Transactional
//    public AuthenticationPayloads.LoginResponse login(final AuthenticationPayloads.LoginRequest request) {
//        User user = userService.findByUsername(request.username())
//                .orElseThrow(() -> new CommonApplicationException(MessageCode.USER_NOT_FOUND));
//        TokenInfoVo tokens;
//
//        isAvailable(user);
//
//        if (passwordEncoder.matches(request.password(), user.getPassword())) {
//            tokens = tokenProvider.create(user);
//        } else {
//            throw new CommonApplicationException(MessageCode.EXCEPTION_AUTHENTICATION_LOGIN_FAIL);
//        }
//
//        List<RoleUser> roleUsers = roleService.findByUserId(user.getId());
//        List<AbilityUser> abilityUsers = abilityService.findByUserId(user.getId());
//
//        LoginVo loginVo = new LoginVo(user, jwtProperty.getAccessTokenValidity(), tokens, roleUsers, abilityUsers);
//        return securityMapper.toLoginResponse(loginVo);
//    }
//
//    @Transactional
//    public void logout(HttpServletRequest request, UserDetails userDetails) {
//        isAvailable(userDetails);
//        tokenProvider.delete(request);
//    }
//
//    @Transactional
//    public AuthenticationPayloads.RefreshResponse reissue(final AuthenticationPayloads.RefreshRequest request) {
//        String reissuedAccessToken = tokenProvider.reissue(request);
//
//        RefreshVo refreshVo = new RefreshVo(reissuedAccessToken, request.refreshToken(), jwtProperty.getAccessTokenValidity());
//        return securityMapper.toRefreshResponse(refreshVo);
//    }
//
//    private void isAvailable(UserDetails userDetails) {
//        if (!userDetails.isEnabled() || !userDetails.isAccountNonExpired() || !userDetails.isAccountNonLocked() || !userDetails.isCredentialsNonExpired()) {
//            throw new CommonApplicationException(EXCEPTION.UNAUTHENTICATED);
//        }
//    }
//
//    public boolean isUnregistered(User user) {
//        return user.getIsDisabled();
//    }
}
