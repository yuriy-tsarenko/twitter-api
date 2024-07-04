package com.twitter.twitterapi.config

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.filter.OncePerRequestFilter

class UserJwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(UserJwtAuthenticationFilter)

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        log.info("JWT authentication filter")
        log.info("Request: {}", request.getRequestURI())
        //TODO: implement JWT authentication here (TWIT-004)
        filterChain.doFilter(request, response);
    }
}
