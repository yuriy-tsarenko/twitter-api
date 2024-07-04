package com.twitter.twitterapi.controller

import com.twitter.twitterapi.config.MockitoBased
import com.twitter.twitterapi.dto.UserDto
import com.twitter.twitterapi.dto.UserRegisterDto
import com.twitter.twitterapi.service.UserMaintenanceService
import org.mockito.InjectMocks
import org.mockito.Mock

import static org.mockito.BDDMockito.given

class UserControllerTest extends MockitoBased {

    @Mock
    UserMaintenanceService userService

    @InjectMocks
    UserController userController

    def "Register"() {
        given: "A UserRegisterDto and the expected UserDto"
        def userRegisterDto = new UserRegisterDto(username: 'testUser', password: 'password', email: 'test@example.com')
        def expectedUserDto = new UserDto(username: 'testUser', email: 'test@example.com')
        given(userService.register(userRegisterDto)).willReturn(expectedUserDto)

        when: "register method is called"
        UserDto response = userController.register(userRegisterDto)

        then: "The response status is OK and body matches the expected UserDto"
        response == expectedUserDto
    }
}
