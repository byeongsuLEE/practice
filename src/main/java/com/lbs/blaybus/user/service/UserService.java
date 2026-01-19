package com.lbs.blaybus.user.service;

import com.lbs.blaybus.user.dto.request.UserJoinRequestDto;
import com.lbs.blaybus.user.dto.response.UserResponseDto;

public interface UserService {

    UserResponseDto getUser(Long userId);

    UserResponseDto joinUser(UserJoinRequestDto request);
}
