package com.framework.base_set.global.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.framework.base_set.global.jpa.repository.UsersRepository;
import com.framework.base_set.global.security.model.UserType;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    private final UsersRepository usersRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationConfiguration authenticationConfiguration;

    private final CustomLoginSuccessHandler customLoginSuccessHandler;

    private final CustomLoginFailureHandler customLoginFailureHandler;

    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    private final CustomAuthenticationEntryPointHandler customAuthenticationEntryPointHandler;

    private final String[] PERMIT_URL = {
        // "/v1/user/register",
        // "/v1/user/login",
        // // "/api/v1/user/register",
        "/**"
    };  

    private final String[] AUTHENTICATION_URL = {
        // "/v1/user/register",
        // "/v1/user/login",
        // // "/api/v1/user/register",
    };  

    private final String[] COMPANY_AUTH = {
        "/company/**"
    };

    private final String[] ADMIN_AUTH = {
        "/admin/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(this.corsConfigurationSource()))
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
            .formLogin(formLogin -> formLogin.disable())
            .authorizeHttpRequests(authorizeRequests -> {
                authorizeRequests
                    .requestMatchers(PERMIT_URL).permitAll()
                    .requestMatchers(AUTHENTICATION_URL).authenticated()
                    .requestMatchers(COMPANY_AUTH).hasAnyAuthority(UserType.COMPANY.getDesc(),UserType.ADMIN.getDesc())
                    .requestMatchers(ADMIN_AUTH).hasAnyAuthority(UserType.ADMIN.getDesc())
                    .anyRequest().authenticated();
            })
            .addFilter(jwtAuthenticationFilter())
            .addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(exception -> {
                exception.accessDeniedHandler(this.customAccessDeniedHandler);
                exception.authenticationEntryPoint(this.customAuthenticationEntryPointHandler);  
            })
            .build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("X-Requested-With", "Content-Type", "Authorization", "X-XSRF-token"));
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }

    @Bean
    public CustomAuthentication customAuthentication(){
        return new CustomAuthentication(userDetailsService, passwordEncoder());        
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter(){
        return new JwtAuthorizationFilter(jwtTokenProvider);
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManagerBean(), jwtTokenProvider, usersRepository);
        jwtAuthenticationFilter.setAuthenticationSuccessHandler(customLoginSuccessHandler);
        jwtAuthenticationFilter.setAuthenticationFailureHandler(customLoginFailureHandler);

        return jwtAuthenticationFilter;
    }

}
