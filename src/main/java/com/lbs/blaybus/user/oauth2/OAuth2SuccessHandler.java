package com.lbs.blaybus.user.oauth2;

import com.lbs.blaybus.auth.dto.response.TokenResponseDto;
import com.lbs.blaybus.auth.service.AuthService;
import com.lbs.blaybus.user.entity.User;
import com.lbs.blaybus.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

/**
 * 작성자  : lbs
 * 날짜    : 2026-01-14
 * 풀이방법
 **/

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService authService;
    private final UserRepository userRepository;
    @Value("${frontend.success.url}")
    private String frontendUrl;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        log.info("=== OAuth2 로그인 성공 ===");
        log.debug("OAuth2 User Attributes: {}", oAuth2User.getAttributes());

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        Optional<User> user = userRepository.findByEmail(email);

        if(user.isPresent()) {
            TokenResponseDto tokenResponse = authService.generateToken(email, name);

            log.info("토큰 발급 완료 - Email: {}", email);

            String redirectUrl = String.format("%s/main?accessToken=%s&refreshToken=%s",
                    frontendUrl,
                    tokenResponse.getAccessToken(),
                    tokenResponse.getRefreshToken());


            redisTemplate.opsForValue().set("user:"+email, tokenResponse.getRefreshToken());
            response.sendRedirect(redirectUrl);
        }else{
            response.sendRedirect(frontendUrl + "/signup?email=" + email + "&name=" + name);
        }
    }
}
