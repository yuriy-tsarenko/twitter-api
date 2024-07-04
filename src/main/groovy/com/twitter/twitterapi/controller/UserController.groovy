package com.twitter.twitterapi.controller

import com.twitter.twitterapi.dto.UserDto
import com.twitter.twitterapi.dto.UserRegisterDto
import com.twitter.twitterapi.service.UserMaintenanceService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/users")
class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController)

    private final UserMaintenanceService userService

    UserController(UserMaintenanceService userService) {
        this.userService = userService
    }

    @PostMapping("/register")
    UserDto register(@RequestBody UserRegisterDto user) {
        log.info("Registering user: {}", user)
        def registered = userService.register(user)
        registered
    }
}
