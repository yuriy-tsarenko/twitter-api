package com.twitter.twitterapi.config.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import com.twitter.twitterapi.config.UserAuthentication
import com.twitter.twitterapi.dto.UserDto
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import javax.crypto.SecretKey
import java.text.ParseException
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class JwtService {
    static final HEADER_DELIMITER = " "

    private final SecretKey secretKey;
    private final JwtConfig config
    private final ObjectMapper objectMapper

    @Value('${spring.application.name}')
    private String issuer

    JwtService(SecretKey secretKey, JwtConfig config, ObjectMapper objectMapper) {
        this.secretKey = secretKey
        this.config = config
        this.objectMapper = objectMapper
    }

    String generateToken(UserAuthentication authentication) throws ParseException {
        def now = Instant.now()
        def expirationDate = now.plus(config.getTokenExpirationAfterDays(), ChronoUnit.DAYS)
        return Jwts.builder()
                .subject(authentication.getPrincipal())
                .claim("details", objectMapper.writeValueAsString(authentication.getDetails()))
                .issuedAt(new Date(now.toEpochMilli()))
                .expiration(new Date(expirationDate.toEpochMilli()))
                .signWith(secretKey)
                .issuer(issuer)
                .compact()
    }

    String generateAuthHeader(UserAuthentication authentication) {
        def token = generateToken(authentication)
        String.join(HEADER_DELIMITER, config.tokenPrefix, token)
    }


    UserAuthentication parseToken(String token) {
        def parser = Jwts.parser()
                .verifyWith(secretKey)
                .decryptWith(secretKey)
                .requireIssuer(issuer)
                .build()

        def claims = parser.parseSignedClaims(token).payload
        def username = claims.getSubject()
        def details = claims.get("details", String.class)
        def userDto = objectMapper.readValue(details, UserDto.class)
        def authentication = new UserAuthentication(username, null, userDto.authorities)
        authentication.setDetails(userDto)
        authentication
    }
}
