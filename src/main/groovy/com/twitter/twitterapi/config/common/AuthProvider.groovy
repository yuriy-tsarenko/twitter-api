package com.twitter.twitterapi.config.common

import com.twitter.twitterapi.dto.UserDto
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails

class AuthProvider extends DaoAuthenticationProvider {
    @Override
    protected UserAuthentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        def successAuthentication = super.createSuccessAuthentication(principal, authentication, user)
        def authPrincipal = successAuthentication.principal as UserDto
        def userAuthentication = new UserAuthentication(
                authPrincipal.username,
                successAuthentication.credentials,
                successAuthentication.authorities
        )
        userAuthentication.setDetails(authPrincipal)
        userAuthentication
    }
}
