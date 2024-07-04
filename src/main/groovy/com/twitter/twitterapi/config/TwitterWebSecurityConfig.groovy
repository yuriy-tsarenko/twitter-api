package com.twitter.twitterapi.config

import com.twitter.twitterapi.config.jwt.JwtConfig
import com.twitter.twitterapi.config.jwt.JwtService
import com.twitter.twitterapi.config.jwt.UserJwtAuthenticationFilter
import com.twitter.twitterapi.service.UserDetailsServiceImpl
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
class TwitterWebSecurityConfig {

    @Value('${security.encoding-strength-level:10}')
    private Integer encodingStrengthLevel

    private final UserDetailsServiceImpl userService
    private final JwtConfig jwtConfig;
    private final JwtService jwtService;

    TwitterWebSecurityConfig(UserDetailsServiceImpl userService, JwtConfig jwtConfig, JwtService jwtService) {
        this.userService = userService
        this.jwtConfig = jwtConfig
        this.jwtService = jwtService
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement { sessionManagement ->
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                }
                .csrf { csrf ->
                    csrf.disable()
                }
                .formLogin { formLogin ->
                    formLogin.disable()
                }
                .authorizeHttpRequests { authorizeRequests ->
                    authorizeRequests
                            .requestMatchers(HttpMethod.POST, "/v1/auth/login").permitAll()
                            .requestMatchers(HttpMethod.POST, "/v1/users/register").permitAll()
                            .requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                            .anyRequest().authenticated()
                }
                .authenticationProvider(userAuthProvider())
                .addFilterBefore(
                        new UserJwtAuthenticationFilter(jwtConfig, jwtService),
                        UsernamePasswordAuthenticationFilter.class
                )

        return http.build()
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        new BCryptPasswordEncoder(encodingStrengthLevel)
    }

    @Bean
    AuthenticationProvider userAuthProvider() {
        def provider = new AuthProvider()
        provider.setPasswordEncoder(passwordEncoder())
        provider.setUserDetailsService(userService)
        provider
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager()
    }

    @Bean
    WebMvcConfigurer corsConfigurer(@Value('${cors.origins.allowed}') String allowedOrigins) {
        def configurer = new WebMvcConfigurer() {
            @Override
            void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(allowedOrigins)
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(false)
                        .maxAge(3600);
            }
        }
        def registry = new InterceptorRegistry()
        registry.addInterceptor(new OriginHeaderCheckInterceptor())
        configurer.addInterceptors(registry)
        configurer
    }
}
