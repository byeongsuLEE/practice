package com.lbs.blaybus.product.controller;

import com.lbs.blaybus.common.response.ApiResponse;
import com.lbs.blaybus.product.dto.request.ProductCreateRequestDto;
import com.lbs.blaybus.product.dto.response.ProductCreateResponseDto;
import com.lbs.blaybus.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 작성자  : lbs
 * 날짜    : 2026-01-19
 * 풀이방법
 **/


@RestController
@RequestMapping ("/products")
@RequiredArgsConstructor
public class ProductController implements ProductSwaggerApi{


    private final ProductService productService;
    @PostMapping
    public ResponseEntity<ApiResponse<ProductCreateResponseDto>> createProduct (@RequestBody ProductCreateRequestDto request) {

        ProductCreateResponseDto createdProduct = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(HttpStatus.CREATED, createdProduct));
    }


}
