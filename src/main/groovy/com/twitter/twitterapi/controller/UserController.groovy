package com.twitter.twitterapi.controller

import com.twitter.twitterapi.dto.UserDto
import com.twitter.twitterapi.dto.UserRegisterDto
import com.twitter.twitterapi.service.UserMaintenanceService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

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

    @PutMapping("/update")
    @Secured("USER")
    UserDto update(@RequestBody UserDto user) {
        log.info("Updating user: {}", user)
        def updated = userService.update(user)
        updated
    }

    @PostMapping("/delete")
    @Secured("USER")
    void delete(@RequestBody String id, @AuthenticationPrincipal String username) {
        log.info("Deleting user with id: {}", id)
        userService.delete(id, username)
    }
}
