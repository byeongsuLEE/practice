package com.lbs.blaybus.user.serviceimpl;

import com.lbs.blaybus.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JPARepository extends JpaRepository<UserEntity,Long> {

}
