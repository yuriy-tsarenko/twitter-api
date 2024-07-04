package com.twitter.twitterapi.service

import com.twitter.twitterapi.config.Roles
import com.twitter.twitterapi.dto.UserDto
import com.twitter.twitterapi.dto.UserRegisterDto
import com.twitter.twitterapi.exception.ActionForbiddenException
import com.twitter.twitterapi.exception.UserNotFoundException
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

    @Override
    UserDto register(UserRegisterDto userRegisterDto) {
        log.info("Registering user: {}", userRegisterDto)
        def entity = userMapper.toUserEntity(userRegisterDto)
        entity.password = passwordEncoder.encode(entity.password)
        entity.authorities = List.of(new SimpleGrantedAuthority(Roles.USER.name()))
        def saved = userRepository.save(entity)
        userMapper.toUserDto(saved)
    }

    @Override
    UserDto update(UserDto userDto) {
        log.info("Updating user: {}", userDto)
        def entity = userMapper.toUserEntity(userDto)
        def saved = userRepository.save(entity)
        userMapper.toUserDto(saved)
    }

    @Override
    void delete(String userIdToDelete, String actorName) {
        log.info("Deleting user with userIdToDelete: {}", userIdToDelete)
        def actorId = userRepository.findByUsername(actorName)
                .map { user -> user.getId() }
                .orElseThrow(UserNotFoundException::new)
        if (!actorId.equals(userIdToDelete)) {
            throw new ActionForbiddenException("You can only delete your own account")
        }
        userRepository.deleteById(userIdToDelete)
    }

}
