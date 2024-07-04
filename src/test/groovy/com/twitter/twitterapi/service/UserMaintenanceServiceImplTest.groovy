package com.twitter.twitterapi.service

import com.twitter.twitterapi.config.MockitoBased
import com.twitter.twitterapi.dto.UserDto
import com.twitter.twitterapi.dto.UserRegisterDto
import com.twitter.twitterapi.entity.UserEntity
import com.twitter.twitterapi.mapper.UserMapper
import com.twitter.twitterapi.repository.UserRepository
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertNotNull
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
}