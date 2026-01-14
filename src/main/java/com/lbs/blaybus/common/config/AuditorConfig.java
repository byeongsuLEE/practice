package com.lbs.blaybus.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * 작성자  : lbs
 * 날짜    : 2026-01-13
 * 풀이방법
 **/


@Configuration
public class AuditorConfig implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // 나중에 	SecurityContextHolder.getContext()로 user 값 가져오기
        return Optional.of("INITAL");
    }
}
