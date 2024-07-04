package com.twitter.twitterapi.service


import com.twitter.twitterapi.mapper.UserMapper
import com.twitter.twitterapi.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl)

    private final UserRepository userRepository
    private final UserMapper userMapper

    UserDetailsServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository
        this.userMapper = userMapper
    }

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user by username: {}", username)
        def userEntity = userRepository.findByUsername(username).orElseThrow({
            new UsernameNotFoundException("User not found with username: " + username)
        })
        userMapper.toUserDto(userEntity)
    }
}
