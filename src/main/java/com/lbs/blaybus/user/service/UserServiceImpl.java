package com.lbs.blaybus.user.service;

import com.lbs.blaybus.common.exception.UserException;
import com.lbs.blaybus.common.response.ErrorCode;
import com.lbs.blaybus.user.domain.User;
import com.lbs.blaybus.user.entity.UserEntity;
import com.lbs.blaybus.user.repository.JPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final JPARepository jpaRepository;
    @Override
    public User getUser(Long userId) {
        return jpaRepository.findById(userId)
                .orElseThrow(()-> new UserException(ErrorCode.USER_NOT_FOUND,"해당 유저가 없습니다"))
                .mapToDomain();
    }

    @Override
    public User joinUser(User user) {
        UserEntity userEntity = UserEntity.createUserEntity(user);
        UserEntity savedUserEntity = jpaRepository.save(userEntity);
        return savedUserEntity.mapToDomain();
    }
    // TODO: Implement service methods
}
