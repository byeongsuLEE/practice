package com.lbs.blaybus.user.controller;

import com.lbs.blaybus.common.response.ApiResponse;
import com.lbs.blaybus.user.dto.response.UserResponseDto;
import com.lbs.blaybus.user.domain.User;
import com.lbs.blaybus.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    UserService userservice;

    @GetMapping("/test")
    public String test() {

        return "test";
    }

    @GetMapping
    public ApiResponse<UserResponseDto> get(@PathVariable(value = "id")Long id) {
        User user = userservice.getUser(id);
        UserResponseDto userResponseDto = user.mapToDto();
        return ApiResponse.success(HttpStatus.OK,userResponseDto);
    }
    // TODO: Add controller methods
}
