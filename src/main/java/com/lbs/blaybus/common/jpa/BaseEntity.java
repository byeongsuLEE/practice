package com.lbs.blaybus.common.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 작성자  : lbs
 * 날짜    : 2026-01-13
 * 풀이방법
 **/


@MappedSuperclass // 이 클래스는 db table을 만들지 말아라 맞음?
@EntityListeners(AuditingEntityListener.class)
@Getter // 컬럼값 가져오기 위해서?
public abstract class BaseEntity {
    @LastModifiedDate
    @Column(name = "change_date")
    LocalDateTime changeDateTime;

    @CreatedDate
    @Column(name = "create_date")
    LocalDateTime createDateTime;

    // 이 두개는 나중에 audit config 에서 security 안에 있는 user 정보 가져와서 넣기
//    @CreatedBy
//    @Column(name = "create_by")
//    String createBy ;
//
//    @LastModifiedBy
//            @Column(name = "update_by")
//    String changeBy;
//

}
