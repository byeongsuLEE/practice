package com.lbs.blaybus.user.service;

import com.lbs.blaybus.common.exception.UserException;
import com.lbs.blaybus.common.response.ErrorCode;
import com.lbs.blaybus.user.dto.request.UserJoinRequestDto;
import com.lbs.blaybus.user.dto.response.UserResponseDto;
import com.lbs.blaybus.user.entity.User;
import com.lbs.blaybus.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Override
    public UserResponseDto getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()-> new UserException(ErrorCode.USER_NOT_FOUND,"해당 유저가 없습니다"))
                .toResponseDto();
    }

    @Override
    public UserResponseDto joinUser(UserJoinRequestDto request) {
        User user = User.from(request);
        User savedUser = userRepository.save(user);
        return savedUser.toResponseDto();
    }
    // TODO: Implement service methods
}
