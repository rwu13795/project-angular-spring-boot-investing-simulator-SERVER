package com.raywu.investingsimulator.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Autowired
    private JWTFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
            // the filter will check some route path that matches the patterns
            .addFilterBefore(jwtFilter, BasicAuthenticationFilter.class)
            // if the filter does not forbid the request, then process the request
            .authorizeHttpRequests((auth) ->
                    auth.anyRequest().permitAll()
            )
//        .cors().disable()
            .csrf().disable()
            .exceptionHandling();

        return http.build();
    }

}