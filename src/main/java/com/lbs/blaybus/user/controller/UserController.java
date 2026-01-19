package com.lbs.blaybus.user.controller;

import com.lbs.blaybus.common.response.ApiResponse;
import com.lbs.blaybus.user.dto.request.UserJoinRequestDto;
import com.lbs.blaybus.user.dto.response.UserResponseDto;
import com.lbs.blaybus.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController implements UserSwaggerApi{

    private final UserService userservice;

    @Override
    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUser(@PathVariable(value = "id") Long id) {
        UserResponseDto userResponseDto = userservice.getUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(HttpStatus.OK, userResponseDto));
    }

    @Override
    @PostMapping("/join")
    public ResponseEntity<ApiResponse<UserResponseDto>> joinUser(@RequestBody UserJoinRequestDto request) {
        UserResponseDto userResponseDto = userservice.joinUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED, userResponseDto));
    }

    // TODO: Add controller methods
}
