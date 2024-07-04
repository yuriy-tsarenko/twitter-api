package com.twitter.twitterapi.config.jwt;

import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import javax.crypto.SecretKey;

@Configuration
class JwtConfig {

    @Value('${application.jwt.secretKey}')
    String tokenKey

    @Value('${application.jwt.tokenExpirationAfterDays}')
    Integer tokenExpirationAfterDays

    @Value('${application.jwt.tokenPrefix}')
    String tokenPrefix

    @Bean
    SecretKey getSecretKey() {
       Keys.hmacShaKeyFor(tokenKey.getBytes())
    }
}
