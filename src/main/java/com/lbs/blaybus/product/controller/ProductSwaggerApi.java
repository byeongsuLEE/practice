package com.lbs.blaybus.product.controller;

import com.lbs.blaybus.common.response.ApiResponse;
import com.lbs.blaybus.product.dto.request.ProductCreateRequestDto;
import com.lbs.blaybus.product.dto.response.ProductCreateResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Tag(name = "Product", description = "상품 API")
public interface ProductSwaggerApi {

    @Operation(
            summary = "상품 등록",
            description = "상품을 등록합니다"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "상품 등록 성공",
                    content = @Content(schema = @Schema(implementation = ProductCreateResponseDto.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 데이터"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "이미 존재하는 상품"
            )
    })
    ResponseEntity<ApiResponse<ProductCreateResponseDto>> createProduct (
            @Parameter(description = "상품 등록" ,required = true)
            @RequestBody ProductCreateRequestDto reqeust
            );
}
