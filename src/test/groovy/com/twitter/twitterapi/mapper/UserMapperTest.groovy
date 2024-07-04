package com.twitter.twitterapi.mapper

import com.twitter.twitterapi.config.MockitoBased
import com.twitter.twitterapi.dto.UserRegisterDto
import com.twitter.twitterapi.entity.UserEntity
import org.mockito.Spy
import org.springframework.security.core.authority.SimpleGrantedAuthority

import java.time.LocalDate

class UserMapperTest extends MockitoBased {

    @Spy
    UserMapper userMapper

    def "ToUserEntity - all fields"() {
        given: "A UserRegisterDto with sample data"
        def userRegisterDto = new UserRegisterDto(username: 'testUser',
                password: 'password', email: 'test@example.com', phone: '1234567890', address: 'Test Address',
                fullName: 'Test User', gender: 'Male', dateOfBirth: LocalDate.of(1990, 1, 1)
        )

        when: "toUserEntity is called with the UserRegisterDto"
        def result = new UserMapper().toUserEntity(userRegisterDto)

        then: "The result is a UserEntity with expected values for all fields"
        result.username == 'testUser'
        result.password == 'password'
        result.email == 'test@example.com'
        result.phone == '1234567890'
        result.address == 'Test Address'
        result.fullName == 'Test User'
        result.gender == 'Male'
        result.dateOfBirth == LocalDate.of(1990, 1, 1)
    }

    def "ToUserEntity - NPE case"() {
        when: "toUserEntity is called with null"
        new UserMapper().toUserEntity(null)

        then: "An IllegalArgumentException is thrown"
        thrown(IllegalArgumentException)
    }

    def "ToUserDto - all fields"() {
        given: "A UserEntity with sample data"
        def userEntity = new UserEntity(username: 'testUser', password: 'password', email: 'test@example.com',
                phone: '1234567890', address: 'Test Address', fullName: 'Test User', gender: 'Male',
                dateOfBirth: LocalDate.of(1990, 1, 1), authorities: [new SimpleGrantedAuthority("ROLE_USER")]
        )

        when: "toUserDto is called with the UserEntity"
        def result = new UserMapper().toUserDto(userEntity)

        then: "The result is a UserDto with expected values for all fields"
        result.username == 'testUser'
        result.email == 'test@example.com'
        result.phone == '1234567890'
        result.address == 'Test Address'
        result.fullName == 'Test User'
        result.gender == 'Male'
        result.dateOfBirth == LocalDate.of(1990, 1, 1)
        result.authorities.contains(new SimpleGrantedAuthority("ROLE_USER"))
    }

    def "ToUserDto - NPE case"() {
        when: "toUserDto is called with null"
        new UserMapper().toUserDto(null)

        then: "An IllegalArgumentException is thrown"
        thrown(IllegalArgumentException)
    }
}
