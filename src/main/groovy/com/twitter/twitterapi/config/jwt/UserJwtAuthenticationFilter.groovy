package com.twitter.twitterapi.config.jwt


import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter

class UserJwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(UserJwtAuthenticationFilter)

    private final JwtConfig jwtConfig
    private final JwtService jwtService

    UserJwtAuthenticationFilter(JwtConfig jwtConfig, JwtService jwtService) {
        this.jwtConfig = jwtConfig
        this.jwtService = jwtService
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        log.info("JWT authentication filter")
        log.info("Request: {}", request.getRequestURI())
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
            String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "").trim()

            try {
                def userAuthentication = jwtService.parseToken(token)
                SecurityContextHolder.getContext().setAuthentication(userAuthentication)

            } catch (JwtException e) {
                log.error("Token cannot be parsed: {}", e)
            }
        }
        filterChain.doFilter(request, response);
    }
}
