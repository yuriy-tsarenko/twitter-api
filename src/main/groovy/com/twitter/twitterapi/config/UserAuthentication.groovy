package com.twitter.twitterapi.config

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
    Collection<String> getAuthorities() {
        return super.getAuthorities().stream()
                .map { auth.authority }
                .collect(Collectors.toList()) as Collection<String>
    }
}
