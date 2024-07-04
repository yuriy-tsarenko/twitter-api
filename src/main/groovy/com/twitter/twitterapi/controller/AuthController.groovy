package com.twitter.twitterapi.controller

import com.twitter.twitterapi.config.common.UserAuthentication
import com.twitter.twitterapi.config.jwt.JwtConfig
import com.twitter.twitterapi.config.jwt.JwtService
import com.twitter.twitterapi.dto.UserDto
import com.twitter.twitterapi.dto.UserLoginDto
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
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
    private final JwtService jwtService
    private final JwtConfig jwtConfig

    AuthController(AuthenticationManager authenticationManager, JwtService jwtService, JwtConfig jwtConfig) {
        this.authenticationManager = authenticationManager
        this.jwtService = jwtService
        this.jwtConfig = jwtConfig
    }

    @PostMapping("/login")
    UserDto login(@RequestBody UserLoginDto request, HttpServletResponse response) {
        log.info("Login request received")
        log.info("Login request received:{}", request)
        Authentication authentication = new UserAuthentication(
                request.username(),
                request.password()
        )
        UserAuthentication authenticatedUser = authenticationManager.authenticate(authentication)
        try {

            response.addHeader(HttpHeaders.AUTHORIZATION, jwtService.generateAuthHeader(authenticatedUser))
        } catch (ParseException e) {
            log.error("JWT token generation failed", e)
        }
        log.info("JWT token successfully generated!")
        log.info(String.format("customer: %s is successfully authorized", authenticatedUser))

        authenticatedUser.details as UserDto;
    }
}
