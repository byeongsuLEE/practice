package com.lbs.blaybus.product.entity;

import com.lbs.blaybus.common.jpa.BaseEntity;
import com.lbs.blaybus.product.dto.response.ProductCreateResponseDto;
import com.lbs.blaybus.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 작성자  : lbs
 * 날짜    : 2026-01-19
 * 풀이방법
 **/


@Entity
@Getter
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;

    @Builder
    private Product(Long id, String name, Long price, User seller) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.seller = seller;
    }

    public ProductCreateResponseDto mapToCreateResponseDto(Long sellerId) {
       return  new ProductCreateResponseDto(this.id , name, price, sellerId);
    }
}
