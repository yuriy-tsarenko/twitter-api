package com.twitter.twitterapi.config.common

import com.twitter.twitterapi.dto.UserDto
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class UserAuthentication extends UsernamePasswordAuthenticationToken {
    UserAuthentication(Object principal, Object credentials) {
        super(principal, credentials)
    }

    UserAuthentication(String principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities)
    }

    @Override
    String getPrincipal() {
        super.getPrincipal()
    }

    @Override
    UserDto getDetails() {
        super.getDetails() as UserDto
    }

    @Override
    Collection<? extends GrantedAuthority> getAuthorities() {
        super.getAuthorities()
    }
}
