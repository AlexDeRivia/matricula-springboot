package com.sistemas.configuracion;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    UserDetailsService userDetailsService(PasswordEncoder encoder,
            @Value("${demo.admin.password:}") String adminPassword,
            @Value("${demo.public-read-only:false}") boolean publicReadOnly) {
        InMemoryUserDetailsManager users = new InMemoryUserDetailsManager(
                User.withUsername("demo")
                        .password(encoder.encode("demo123"))
                        .roles("VIEWER")
                        .build());
        if (!publicReadOnly && !adminPassword.isBlank()) {
            users.createUser(User.withUsername("admin-demo")
                    .password(encoder.encode(adminPassword))
                    .roles("ADMIN")
                    .build());
        }
        return users;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/", "/css/**", "/images/**", "/js/**",
                                "/usuario/login", "/usuario/denegado").permitAll()
                        .requestMatchers(HttpMethod.GET, "/alumno/index", "/docente/index", "/curso/index")
                        .authenticated()
						.requestMatchers(HttpMethod.GET, "/nota/boleta/**").authenticated()
                        .requestMatchers("/alumno/**", "/docente/**", "/curso/**", "/nota/**")
                        .hasRole("ADMIN")
                        .anyRequest().authenticated())
                .exceptionHandling(errors -> errors.accessDeniedPage("/usuario/denegado"))
                .formLogin(form -> form.loginPage("/usuario/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll())
                .logout(logout -> logout.logoutSuccessUrl("/usuario/login?logout"))
                .build();
    }
}
