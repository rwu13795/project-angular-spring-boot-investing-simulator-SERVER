package com.raywu.investingsimulator.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {
    @Autowired
    private JWTFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // the filter will check some route path that matches the patterns
            // if the filter does not forbid the request, then process the request
            .addFilterBefore(jwtFilter, BasicAuthenticationFilter.class)
            .httpBasic().disable()
            .authorizeHttpRequests((auth) ->
                    auth.anyRequest().permitAll()
            )
            .cors().and()
            .csrf().disable()
            .exceptionHandling();

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins
                (Arrays.asList("http://localhost:4200",
                        "https://www.trading-simulator.live"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE", "OPTIONS"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}