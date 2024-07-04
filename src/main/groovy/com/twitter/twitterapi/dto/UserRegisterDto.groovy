package com.twitter.twitterapi.dto

import java.time.LocalDate

record UserRegisterDto(String username,
                       String password,
                       String email,
                       String phone,
                       String address,
                       String fullName,
                       String gender,
                       LocalDate dateOfBirth) {
}