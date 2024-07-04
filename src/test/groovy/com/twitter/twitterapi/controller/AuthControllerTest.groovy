package com.twitter.twitterapi.controller

import com.twitter.twitterapi.config.MockitoBased
import com.twitter.twitterapi.config.UserAuthentication
import com.twitter.twitterapi.config.jwt.JwtConfig
import com.twitter.twitterapi.config.jwt.JwtService
import com.twitter.twitterapi.dto.UserDto
import com.twitter.twitterapi.dto.UserLoginDto
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication

import static org.mockito.ArgumentMatchers.any
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

class AuthControllerTest extends MockitoBased {

    @Mock
    AuthenticationManager authenticationManager

    @Mock
    JwtService jwtService

    @Mock
    JwtConfig jwtConfig

    @InjectMocks
    AuthController authController

    def "Login generates JWT and returns UserDto"() {
        given: "A UserLoginDto with sample data and a mock HttpServletResponse"
        def userLoginDto = new UserLoginDto(username: 'testUser', password: 'password')
        def userAuthentication = mock(UserAuthentication)
        def userDto = new UserDto(username: 'testUser', email: 'test@example.com')
        def expectedToken = "Bearer test_token"
        def mockHttpServletResponse = new MockHttpServletResponse()

        and: "Mocked behavior for authenticationManager, jwtService, and jwtConfig"
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(userAuthentication)
        when(jwtService.generateAuthHeader(any(UserAuthentication.class))).thenReturn(expectedToken)
        when(userAuthentication.getPrincipal()).thenReturn("testUser")
        when(userAuthentication.getDetails()).thenReturn(userDto) // Ensure this matches your method's expectations

        when: "login method is called"

        def result = authController.login(userLoginDto, mockHttpServletResponse)

        then: "The result is the expected UserDto"
        assert result.username == 'testUser'
        assert result.email == 'test@example.com'

        then: "The response has the expected header"
        assert mockHttpServletResponse.getHeader("Authorization") == expectedToken
    }
}