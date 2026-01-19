package com.lbs.blaybus.product.service;

import com.lbs.blaybus.product.dto.request.ProductCreateRequestDto;
import com.lbs.blaybus.product.dto.response.ProductCreateResponseDto;
import com.lbs.blaybus.product.entity.Product;

public interface ProductService   {
    ProductCreateResponseDto createProduct(ProductCreateRequestDto request);
}
