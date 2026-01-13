package com.lbs.blaybus.user.entity;

import com.lbs.blaybus.common.jpa.BaseEntity;
import com.lbs.blaybus.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 작성자  : lbs
 * 날짜    : 2026-01-10
 * 풀이방법
 **/


@Getter
@Table(name = "users")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    private UserEntity(String email, String name, String phone) {
        this.email = email;
        this.name = name;
        this.phone = phone;
    }

    public static UserEntity createUserEntity(User user) {
        return UserEntity.builder()
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .build();
    }
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
