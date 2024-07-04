package com.twitter.twitterapi.service

import com.twitter.twitterapi.config.MockitoBased
import com.twitter.twitterapi.dto.UserDto
import com.twitter.twitterapi.entity.UserEntity
import com.twitter.twitterapi.mapper.UserMapper
import com.twitter.twitterapi.repository.UserRepository
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.security.core.userdetails.UsernameNotFoundException

import static org.mockito.Mockito.any
import static org.mockito.Mockito.when

class UserDetailsServiceImplTest extends MockitoBased {

    @Mock
    UserRepository userRepository

    @Mock
    UserMapper userMapper

    @InjectMocks
    UserDetailsServiceImpl userDetailsService

    def "loadUserByUsername should return UserDetails for existing username"() {
        given:
        String username = "existingUser"
        UserDto userDto = new UserDto(username: username, password: "password", email: "email")
        UserEntity userEntity = new UserEntity(username: username, password: "password", email: "email")
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity))
        when(userMapper.toUserDto(any(UserEntity))).thenReturn(userDto)

        when:
        def userDetails = userDetailsService.loadUserByUsername(username)

        then:
        userDetails.username == username
    }

    def "loadUserByUsername should throw UsernameNotFoundException for non-existing username"() {
        given:
        String username = "nonExistingUser"
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty())

        when:
        userDetailsService.loadUserByUsername(username)

        then:
        thrown(UsernameNotFoundException)
    }
}