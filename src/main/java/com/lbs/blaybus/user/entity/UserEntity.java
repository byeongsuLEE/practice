package com.lbs.blaybus.user.entity;

import com.lbs.blaybus.common.jpa.BaseEntity;
import com.lbs.blaybus.user.dto.request.JoinUserRequestDto;
import com.lbs.blaybus.user.dto.response.UserResponseDto;
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

    public static UserEntity from(JoinUserRequestDto dto) {
        return UserEntity.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .phone(dto.getPhone())
                .build();
    }

    public UserResponseDto toResponseDto(){
        return UserResponseDto.builder()
                .id(this.id)
                .email(this.email)
                .name(this.name)
                .phone(this.phone)
                .createDateTime(this.getCreateDateTime())
                .changeDateTime(this.getChangeDateTime())
                // 나중에 추가
//                .createBy(this.getgetCreateBy())
//                .changeBy(this.getChangeBy())
                .build();
    }


}
