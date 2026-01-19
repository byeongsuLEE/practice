package com.lbs.blaybus.payment.kakao.domain.dto;

import java.time.LocalDateTime;

public record ReadyResponse(String tid,
                            String next_redirect_app_url,
                            String next_redirect_mobile_url,
                            String next_redirect_pc_url,
                            LocalDateTime created_at) {
}
