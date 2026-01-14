package com.lbs.blaybus.auth.repository;

import com.lbs.blaybus.auth.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 작성자  : lbs
 * 날짜    : 2026-01-14
 * 풀이방법 : RefreshToken Repository
 **/

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByEmail(String email);

    Optional<RefreshTokenEntity> findByToken(String token);

}