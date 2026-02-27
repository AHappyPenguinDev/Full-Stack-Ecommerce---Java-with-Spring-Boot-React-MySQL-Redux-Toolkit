package com.penguinshop.controller;

import com.penguinshop.response.ApiResponse;

import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAsync
public class HomeController{

    @GetMapping("/")
    public ApiResponse HomeControllerHandler() {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Welcome to the Ecommerce Multivendor System!");
        return apiResponse;
    }

}
