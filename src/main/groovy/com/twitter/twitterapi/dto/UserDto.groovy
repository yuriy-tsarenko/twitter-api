package com.twitter.twitterapi.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.twitter.twitterapi.util.GrantedAuthorityListDeserializer
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

import java.time.LocalDate

@JsonIgnoreProperties(ignoreUnknown = true, value = "password")
class UserDto implements UserDetails {

    String id
    String username
    String password
    String email
    String phone
    String address
    String fullName
    String gender
    LocalDate dateOfBirth
    @JsonDeserialize(using = GrantedAuthorityListDeserializer)
    List<GrantedAuthority> authorities = []

    @Override
    Collection<? extends GrantedAuthority> getAuthorities() {
        authorities
    }

    @Override
    String getPassword() {
        password
    }

    @Override
    String getUsername() {
        username
    }

    @Override
    String toString() {
        """
    UserDto{
        id='${id}',
        username='${username}',
        password='secured',
        email='${email}',
        phone='${phone}',
        address='${address}',
        fullName='${fullName}',
        gender='${gender}',
        dateOfBirth=$dateOfBirth,
        authorities=$authorities
    }
    """
    }
}