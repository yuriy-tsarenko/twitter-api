package com.twitter.twitterapi.controller

import com.twitter.twitterapi.config.MockitoBased
import com.twitter.twitterapi.config.UserAuthentication
import com.twitter.twitterapi.dto.UserDto
import com.twitter.twitterapi.dto.UserLoginDto
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.authentication.AuthenticationManager

import static org.mockito.ArgumentMatchers.any
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

class AuthControllerTest extends MockitoBased {

    @Mock
    AuthenticationManager authenticationManager

    @InjectMocks
    AuthController authController

    def "Login"() {
        given: "A UserLoginDto with sample data and a mock HttpServletResponse"
        def userLoginDto = new UserLoginDto(username: 'testUser', password: 'password')
        def response = new MockHttpServletResponse()
        def userAuthentication = mock(UserAuthentication)
        def userDto = new UserDto(username: 'testUser', email: 'test@example.com')

        and: "Mocked authenticationManager behavior"
        when(authenticationManager.authenticate(any())).thenReturn(userAuthentication)
        when(userAuthentication.details).thenReturn(userDto)

        when: "login method is called"
        def result = authController.login(userLoginDto, response)

        then: "The response contains the expected 'Authorization' header"
        response.getHeader("Authorization") == "Bearer test_token"

        and: "The result is the expected UserDto"
        assert result.username == 'testUser'
        assert result.email == 'test@example.com'
    }
}
