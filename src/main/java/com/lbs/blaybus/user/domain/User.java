package com.lbs.blaybus.user.domain;

import com.lbs.blaybus.user.dto.response.UserResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 작성자  : lbs
 * 날짜    : 2026-01-13
 * 풀이방법
 **/


@Builder
@Getter
@ToString
public class User {
    private Long id;
    private String email;
    private String name;
    private String phone;
    LocalDateTime createDateTime;
    LocalDateTime changeDateTime;
    String createBy ;
    String changeBy;

    public UserResponseDto mapToDto() {
        return UserResponseDto.builder()
                .id(this.id)
                .email(this.email)
                .name(this.name)
                .phone(this.phone)
                .createDateTime(this.createDateTime)
                .changeDateTime(this.changeDateTime)
                .createBy(this.createBy)
                .changeBy(this.changeBy)
                .build();
    }
}
