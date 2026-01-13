package com.lbs.blaybus.user.dto.response;


import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
public class UserResponseDto {
    // TODO: Add response fields
    private Long id;
    private String email;
    private String name;
    private String phone;
    LocalDateTime createDateTime;
    LocalDateTime changeDateTime;
    String createBy ;
    String changeBy;
}
