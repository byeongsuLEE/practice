package com.lbs.blaybus.product.service;

import com.lbs.blaybus.common.exception.UserException;
import com.lbs.blaybus.common.response.ErrorCode;
import com.lbs.blaybus.product.dto.request.ProductCreateRequestDto;
import com.lbs.blaybus.product.dto.response.ProductCreateResponseDto;
import com.lbs.blaybus.product.entity.Product;
import com.lbs.blaybus.product.repository.ProductRepository;
import com.lbs.blaybus.user.entity.User;
import com.lbs.blaybus.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 작성자  : lbs
 * 날짜    : 2026-01-19
 * 풀이방법
 **/


@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public ProductCreateResponseDto createProduct(ProductCreateRequestDto request) {

        User seller = userRepository.findById(request.sellerId())
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, "판매 유저를 찾을 수 없습니다."));


        Product product = Product.builder()
                .name(request.name())
                .price(request.price())
                .seller(seller)
                .build();

        product = productRepository.save(product);
        return product.mapToCreateResponseDto(request.sellerId());
    }
}
