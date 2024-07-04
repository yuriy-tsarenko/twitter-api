package com.twitter.twitterapi.service

import com.twitter.twitterapi.dto.UserDto
import com.twitter.twitterapi.dto.UserRegisterDto

interface UserMaintenanceService {
    UserDto register(UserRegisterDto userRegisterDto)

    UserDto update(UserDto userDto)

    void delete(String id, String actorName)
}