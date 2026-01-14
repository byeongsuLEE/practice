package com.lbs.blaybus.user.repository;

import com.lbs.blaybus.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JPARepository extends JpaRepository<UserEntity,Long> {

}
