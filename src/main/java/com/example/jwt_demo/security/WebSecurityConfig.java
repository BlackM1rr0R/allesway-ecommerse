package com.example.jwt_demo.security;

import com.example.jwt_demo.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
public class WebSecurityConfig {
    @Autowired
    CustomUserDetailsService userDetailsService;
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configure(http))  // CORS'u aktifleştir
                .csrf(csrf -> csrf.disable()) // CSRF'yi kapat
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(unauthorizedHandler)
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                // Genel erişim izinleri
                                .requestMatchers("/api/tasks/allTask").permitAll()
                                .requestMatchers("/all/category/get/all/category").permitAll()
                                .requestMatchers("/api/tasks/categories/sub/**").permitAll()
                                .requestMatchers("/all/subCategories/**").permitAll()
                                .requestMatchers("/api/auth/**", "/api/test/all").permitAll()
                                .requestMatchers("/api/tasks/task/categories/**").permitAll()
                                .requestMatchers("/api/tasks/details/{taskId}").permitAll()
                                .requestMatchers("/api/tasks/search/**").permitAll()
                                .requestMatchers("/api/tasks/remove/").permitAll()

                                .requestMatchers("/image").permitAll()
                                .requestMatchers("/image/**").permitAll()
                                // Admin ve User yetkisi gerektiren endpoints
                                .requestMatchers("/api/tasks/create").hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers("/api/tasks/createTask/").hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers("/api/tasks/get/").hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers("/add/delete/").hasAnyAuthority("ADMIN", "USER")

                                .anyRequest().authenticated()
                );

        // JWT Token kontrolü
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
