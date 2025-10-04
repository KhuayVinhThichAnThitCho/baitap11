package com.example.demo3.config;

import com.example.demo3.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // TƯƠNG ĐƯƠNG @Autowired CustomUserDetailsService userDetailsService; ở ảnh
    private final CustomUserDetailsService userDetailsService;

    // TƯƠNG ĐƯƠNG @Bean BCryptPasswordEncoder passwordEncoder()
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // TƯƠNG ĐƯƠNG @Bean DaoAuthenticationProvider authenticationProvider()
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // TƯƠNG ĐƯƠNG phần AuthenticationManager trong ảnh
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    // TƯƠNG ĐƯƠNG phần http bảo vệ tài nguyên trong slide
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/").hasAnyAuthority("ROLE_USER","ROLE_ADMIN","ROLE_EDITOR","ROLE_CREATOR")
                .requestMatchers("/new").hasAnyAuthority("ROLE_ADMIN","ROLE_CREATOR")
                .requestMatchers("/edit/**").hasAnyAuthority("ROLE_ADMIN","ROLE_EDITOR")
                .requestMatchers("/delete/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/login", "/hello").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(httpBasic -> {})
            .formLogin(login -> login
                .loginPage("/login").permitAll()
            )
            .logout(logout -> logout.permitAll())
            .exceptionHandling(e -> e.accessDeniedPage("/403"))
            // gắn DaoAuthenticationProvider (thay cho configure(auth).authenticationProvider(...))
            .authenticationProvider(authenticationProvider())
            .build();
    }
}
