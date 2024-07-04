package com.twitter.twitterapi.controller

import com.twitter.twitterapi.config.MockitoBased
import com.twitter.twitterapi.dto.UserDto
import com.twitter.twitterapi.dto.UserRegisterDto
import com.twitter.twitterapi.service.UserMaintenanceService
import org.mockito.InjectMocks
import org.mockito.Mock

import static org.mockito.BDDMockito.given
import static org.mockito.Mockito.verify

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

    def "Update User"() {
        given: "A UserDto for updating and the expected updated UserDto"
        def userDto = new UserDto(username: 'updatedUser', email: 'update@example.com')
        def expectedUpdatedUserDto = new UserDto(username: 'updatedUser', email: 'update@example.com')
        given(userService.update(userDto)).willReturn(expectedUpdatedUserDto)

        when: "update method is called"
        UserDto response = userController.update(userDto)

        then: "The response matches the expected updated UserDto"
        response == expectedUpdatedUserDto
    }

    def "Delete User"() {
        given: "A user ID to delete and the username of the authenticated user"
        def userIdToDelete = "123"
        def username = "authenticatedUser"

        when: "delete method is called"
        userController.delete(userIdToDelete, username)

        then: "The userService delete method is called with the correct parameters"
        verify(userService).delete(userIdToDelete, username)
    }
}
