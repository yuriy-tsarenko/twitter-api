package com.twitter.twitterapi.mapper

import com.twitter.twitterapi.dto.UserDto
import com.twitter.twitterapi.dto.UserRegisterDto
import com.twitter.twitterapi.entity.UserEntity
import org.springframework.stereotype.Component

@Component
class UserMapper {

    UserEntity toUserEntity(UserRegisterDto request) {
        Optional.ofNullable(request)
                .map {
                    new UserEntity(
                            id: null,
                            username: request.username(),
                            password: request.password(),
                            email: request.email(),
                            phone: request.phone(),
                            address: request.address(),
                            fullName: request.fullName(),
                            gender: request.gender(),
                            dateOfBirth: request.dateOfBirth(),
                            authorities: []
                    )
                }.orElseThrow(IllegalArgumentException::new)

    }

    UserEntity toUserEntity(UserDto request) {
        Optional.ofNullable(request)
                .map {
                    new UserEntity(
                            id: request.id,
                            username: request.username,
                            password: request.password,
                            email: request.email,
                            phone: request.phone,
                            address: request.address,
                            fullName: request.fullName,
                            gender: request.gender,
                            dateOfBirth: request.dateOfBirth,
                            authorities: request.authorities
                    )
                }.orElseThrow(IllegalArgumentException::new)
    }

    UserDto toUserDto(UserEntity entity) {
        Optional.ofNullable(entity)
                .map {
                    new UserDto(
                            id: entity.id,
                            username: entity.username,
                            password: entity.password,
                            email: entity.email,
                            phone: entity.phone,
                            address: entity.address,
                            fullName: entity.fullName,
                            gender: entity.gender,
                            dateOfBirth: entity.dateOfBirth,
                            authorities: entity.authorities)
                }
                .orElseThrow(IllegalArgumentException::new)
    }

}