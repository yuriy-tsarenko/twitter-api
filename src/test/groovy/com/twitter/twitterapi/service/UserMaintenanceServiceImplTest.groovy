package com.twitter.twitterapi.service

import com.twitter.twitterapi.config.MockitoBased
import com.twitter.twitterapi.dto.UserDto
import com.twitter.twitterapi.dto.UserRegisterDto
import com.twitter.twitterapi.entity.UserEntity
import com.twitter.twitterapi.exception.ActionForbiddenException
import com.twitter.twitterapi.mapper.UserMapper
import com.twitter.twitterapi.repository.UserRepository
import org.junit.jupiter.api.Assertions
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

class UserMaintenanceServiceImplTest extends MockitoBased {

    @Mock
    UserRepository userRepository

    @Mock
    UserMapper userMapper

    @Mock
    PasswordEncoder passwordEncoder

    @InjectMocks
    UserMaintenanceServiceImpl userMaintenanceService

    def "Register"() {
        given: "A user registration DTO and expected user entity"
        def userRegisterDto = new UserRegisterDto(username: "testUser", password: "password", email: "test@example.com")
        def userEntity = new UserEntity(username: "testUser", password: "encodedPassword", email: "test@example.com", authorities: [new SimpleGrantedAuthority("ROLE_USER")])
        def expectedUserDto = new UserDto(username: "testUser", email: "test@example.com")

        when(userMapper.toUserEntity(userRegisterDto)).thenReturn(userEntity)
        when(passwordEncoder.encode(userEntity.password)).thenReturn("encodedPassword")
        when(userRepository.save(userEntity)).thenReturn(userEntity)
        when(userMapper.toUserDto(userEntity)).thenReturn(expectedUserDto)

        when: "Register method is called"
        def result = userMaintenanceService.register(userRegisterDto)

        then: "The result should be the expected user DTO"
        assertNotNull(result)
        assertEquals(expectedUserDto.username, result.username)
        assertEquals(expectedUserDto.email, result.email)
    }

    def "Update"() {
        given: "A user DTO for updating and the expected updated user entity"
        def userDto = new UserDto(username: "updatedUser", email: "update@example.com")
        def userEntity = new UserEntity(username: "updatedUser", password: "updatedPassword", email: "update@example.com")
        def expectedUpdatedUserDto = new UserDto(username: "updatedUser", email: "update@example.com")

        when(userMapper.toUserEntity(userDto)).thenReturn(userEntity)
        when(userRepository.save(userEntity)).thenReturn(userEntity)
        when(userMapper.toUserDto(userEntity)).thenReturn(expectedUpdatedUserDto)

        when: "Update method is called"
        def result = userMaintenanceService.update(userDto)

        then: "The result should be the expected updated user DTO"
        assertNotNull(result)
        assertEquals(expectedUpdatedUserDto.username, result.username)
        assertEquals(expectedUpdatedUserDto.email, result.email)
    }

    def "Delete"() {
        given: "A user ID to delete and the actor's username with matching IDs"
        def userIdToDelete = "123"
        def actorName = "actorUser"
        def actorId = "123"
        def userEntity = new UserEntity(id: actorId, username: actorName)

        when(userRepository.findByUsername(actorName)).thenReturn(Optional.of(userEntity))
        when(userRepository.findById(userIdToDelete)).thenReturn(Optional.of(userEntity))

        when: "Delete method is called"
        userMaintenanceService.delete(userIdToDelete, actorName)

        then: "The user with the given ID should be deleted"
        verify(userRepository).deleteById(userIdToDelete)
    }

    def "Delete with non-matching ID throws ActionForbiddenException"() {
        given: "A user ID to delete and the actor's username with non-matching IDs"
        def userIdToDelete = "123"
        def actorName = "actorUser"
        def actorId = "456"
        def actorUserEntity = new UserEntity(id: actorId, username: actorName)
        def nonMatchingUserEntity = new UserEntity(id: userIdToDelete)

        when(userRepository.findByUsername(actorName)).thenReturn(Optional.of(actorUserEntity))
        when(userRepository.findById(userIdToDelete)).thenReturn(Optional.of(nonMatchingUserEntity))

        when: "Delete method is called with non-matching ID"
        Assertions.assertThrows(ActionForbiddenException.class, () -> {
            userMaintenanceService.delete(userIdToDelete, actorName)
        })

        then: "An ActionForbiddenException is expected to be thrown"
    }
}