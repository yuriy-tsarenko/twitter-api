package com.twitter.twitterapi.controller

import com.twitter.twitterapi.config.UserAuthentication
import com.twitter.twitterapi.dto.UserDto
import com.twitter.twitterapi.dto.UserLoginDto
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import java.text.ParseException

@RestController
@RequestMapping("/v1/auth")
class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController)

    private final AuthenticationManager authenticationManager

    AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager
    }

    @PostMapping("/login")
    UserDto login(@RequestBody UserLoginDto request, HttpServletResponse response) {
        log.info("Login request received")
        log.info("Login request received:{}", request)
        Authentication authentication = new UserAuthentication(
                request.username(),
                request.password()
        );

        UserAuthentication authenticate = authenticationManager.authenticate(authentication);
        try {
            //TODO: implement JWT authentication here (TWIT-004)
            response.addHeader("Authorization", "Bearer " + "test_token")
        } catch (ParseException e) {
            log.error("JWT token generation failed", e);
        }
        log.info("JWT token successfully generated!");
        log.info(String.format("customer: %s is successfully authorized", authenticate));

        authenticate.details as UserDto;
    }
}
