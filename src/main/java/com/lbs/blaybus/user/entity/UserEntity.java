package com.lbs.blaybus.user.entity;

import com.lbs.blaybus.common.jpa.BaseEntity;
import com.lbs.blaybus.user.domain.User;
import jakarta.persistence.*;

/**
 * 작성자  : lbs
 * 날짜    : 2026-01-10
 * 풀이방법
 **/


@Entity
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(length = 255)
    private String name;

    @Column(length= 20)
    private String phone;


    public User mapToDomain(){
        return User.builder()
                .id(this.id)
                .email(this.email)
                .name(this.name)
                .phone(this.phone)
                .createDateTime(this.getCreateDateTime())
                .changeDateTime(this.getChangeDateTime())
                // .changeBy()
                // .createBy()
                .build();

    }


}
