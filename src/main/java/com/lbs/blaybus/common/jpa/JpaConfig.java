package com.lbs.blaybus.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * JPA Auditing 활성화 설정
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {

    /**
     * 생성자/수정자 정보를 자동으로 주입하는 Bean
     * SecurityContext에서 현재 로그인한 사용자 정보를 가져옴
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }
}

class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.of("SYSTEM");
        }

        // 사용자 이메일 또는 ID 반환
        return Optional.of(authentication.getName());
    }
}