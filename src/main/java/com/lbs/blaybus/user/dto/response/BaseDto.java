package com.lbs.blaybus.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 작성자  : lbs
 * 날짜    : 2026-01-14
 * 풀이방법
 **/
@SuperBuilder
@Getter
public class BaseDto {
    @Schema(description = "생성 일시", example = "2026-01-13T10:00:00")
    private LocalDateTime createDateTime;
    @Schema(description = "수정 일시", example = "2026-01-13T10:00:00")
    private LocalDateTime changeDateTime;
    @Schema(description = "생성자", example = "admin")
    private String createBy;
    @Schema(description = "수정자", example = "admin")
    private String changeBy;
}
