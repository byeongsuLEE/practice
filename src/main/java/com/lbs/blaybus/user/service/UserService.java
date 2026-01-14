package com.lbs.blaybus.user.service;

import com.lbs.blaybus.user.dto.request.JoinUserRequestDto;
import com.lbs.blaybus.user.dto.response.UserResponseDto;

public interface UserService {

    UserResponseDto getUser(Long userId);

    UserResponseDto joinUser(JoinUserRequestDto request);
}
