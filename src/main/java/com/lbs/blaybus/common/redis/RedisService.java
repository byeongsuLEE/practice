//package com.lbs.blaybus.common.redis;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//import java.time.Duration;
//import java.util.Set;
//import java.util.concurrent.TimeUnit;
//
///**
// * Redis 기본 CRUD 작업을 위한 유틸리티 서비스
// */
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class RedisService {
//
//    private final RedisTemplate<String, Object> redisTemplate;
//
//    /**
//     * 데이터 저장
//     */
//    public void save(String key, Object value) {
//        redisTemplate.opsForValue().set(key, value);
//        log.debug("Redis 저장 완료 - Key: {}", key);
//    }
//
//    /**
//     * 데이터 저장 (만료 시간 설정)
//     */
//    public void save(String key, Object value, Duration duration) {
//        redisTemplate.opsForValue().set(key, value, duration);
//        log.debug("Redis 저장 완료 - Key: {}, TTL: {}초", key, duration.getSeconds());
//    }
//
//    /**
//     * 데이터 조회
//     */
//    public Object get(String key) {
//        Object value = redisTemplate.opsForValue().get(key);
//        log.debug("Redis 조회 - Key: {}, Value: {}", key, value);
//        return value;
//    }
//
//    /**
//     * 데이터 조회 (타입 변환)
//     */
//    @SuppressWarnings("unchecked")
//    public <T> T get(String key, Class<T> clazz) {
//        return (T) get(key);
//    }
//
//    /**
//     * 데이터 삭제
//     */
//    public void delete(String key) {
//        redisTemplate.delete(key);
//        log.debug("Redis 삭제 완료 - Key: {}", key);
//    }
//
//    /**
//     * 데이터 존재 여부 확인
//     */
//    public boolean exists(String key) {
//        Boolean exists = redisTemplate.hasKey(key);
//        return exists != null && exists;
//    }
//
//    /**
//     * 만료 시간 설정
//     */
//    public void expire(String key, Duration duration) {
//        redisTemplate.expire(key, duration);
//        log.debug("Redis TTL 설정 - Key: {}, TTL: {}초", key, duration.getSeconds());
//    }
//
//    /**
//     * 남은 만료 시간 조회 (초)
//     */
//    public Long getTTL(String key) {
//        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
//    }
//
//    /**
//     * 패턴으로 키 검색
//     */
//    public Set<String> getKeys(String pattern) {
//        return redisTemplate.keys(pattern);
//    }
//
//    /**
//     * 증가 (Increment)
//     */
//    public Long increment(String key) {
//        return redisTemplate.opsForValue().increment(key);
//    }
//
//    /**
//     * 증가 (특정 값만큼)
//     */
//    public Long increment(String key, long delta) {
//        return redisTemplate.opsForValue().increment(key, delta);
//    }
//
//    /**
//     * 감소 (Decrement)
//     */
//    public Long decrement(String key) {
//        return redisTemplate.opsForValue().decrement(key);
//    }
//}