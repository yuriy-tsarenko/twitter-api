package com.twitter.twitterapi.service

import com.twitter.twitterapi.dto.UserRegisterDto
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

interface UserMaintenanceService {
    UserDetails register(UserRegisterDto userRegisterDto)
}