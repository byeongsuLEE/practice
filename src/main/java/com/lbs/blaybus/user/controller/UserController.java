package com.lbs.blaybus.user.controller;

import com.lbs.blaybus.common.response.ApiResponse;
import com.lbs.blaybus.user.dto.request.JoinUserRequestDto;
import com.lbs.blaybus.user.dto.response.UserResponseDto;
import com.lbs.blaybus.user.domain.User;
import com.lbs.blaybus.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController implements UserSwaggerApi{

    UserService userservice;

    @Override
    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUser(@PathVariable(value = "id") Long id) {
        User user = userservice.getUser(id);
        UserResponseDto userResponseDto = user.mapToDto();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(HttpStatus.OK, userResponseDto));
    }

    @Override
    @PostMapping("/join")
    public ResponseEntity<ApiResponse<UserResponseDto>> joinUser(@RequestBody JoinUserRequestDto request) {
        User user = request.mapToDomain(request);
        user = userservice.joinUser(user);

        UserResponseDto userResponseDto = user.mapToDto();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED, userResponseDto));
    }
    // TODO: Add controller methods
}
