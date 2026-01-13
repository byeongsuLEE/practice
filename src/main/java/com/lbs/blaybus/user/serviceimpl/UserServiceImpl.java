package com.lbs.blaybus.user.serviceimpl;

import com.lbs.blaybus.common.exception.UserException;
import com.lbs.blaybus.common.response.ErrorCode;
import com.lbs.blaybus.user.domain.User;
import com.lbs.blaybus.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    JPARepository jpaRepository;
    @Override
    public User getUser(Long userId) {
        return jpaRepository.findById(userId)
                .orElseThrow(()-> new UserException(ErrorCode.USER_NOT_FOUND,"해당 유저가 없습니다"))
                .mapToDomain();
    }
    // TODO: Implement service methods
}
