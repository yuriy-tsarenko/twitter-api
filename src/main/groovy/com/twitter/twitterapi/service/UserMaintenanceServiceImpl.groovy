package com.twitter.twitterapi.service

import com.twitter.twitterapi.config.Roles
import com.twitter.twitterapi.dto.UserDto
import com.twitter.twitterapi.dto.UserRegisterDto
import com.twitter.twitterapi.mapper.UserMapper
import com.twitter.twitterapi.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserMaintenanceServiceImpl implements UserMaintenanceService {

    private static final Logger log = LoggerFactory.getLogger(UserMaintenanceServiceImpl.class)

    private final UserRepository userRepository
    private final UserMapper userMapper
    private final PasswordEncoder passwordEncoder

    UserMaintenanceServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository
        this.userMapper = userMapper
        this.passwordEncoder = passwordEncoder
    }

    UserDto register(UserRegisterDto userRegisterDto) {
        log.info("Registering user: {}", userRegisterDto)
        def entity = userMapper.toUserEntity(userRegisterDto)
        entity.password = passwordEncoder.encode(entity.password)
        entity.authorities = List.of(new SimpleGrantedAuthority(Roles.USER.name()))
        def saved = userRepository.save(entity)
        userMapper.toUserDto(saved)
    }
}
