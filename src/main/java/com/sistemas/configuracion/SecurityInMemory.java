package com.sistemas.configuracion;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration. EnableWebSecurity; 
import org.springframework.security.core.userdetails. User;
import org.springframework.security.core.userdetails. UserDetailsService; 
import org.springframework.security.crypto.password. PasswordEncoder;
import org.springframework.security.provisioning. InMemoryUserDetailsManager; 
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityInMemory {
	@Autowired PasswordEncoder passwordEncoder;
	

	@Bean
	UserDetailsService userDetailsService() { 
		return new InMemoryUserDetailsManager (
			User.withUsername("usuario")
			.password(passwordEncoder.encode("123"))
			.roles("USER")
			.build(),
		  User.withUsername("admin")
		    .password(passwordEncoder.encode("123"))
		    .roles("USER","ADMIN")
		    .build()
		) ;
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.httpBasic(withDefaults ())
			.authorizeHttpRequests((request)->
			   request
			  .requestMatchers("/css/**").permitAll()
			  .requestMatchers("/images/**").permitAll()
			  .requestMatchers("/js/**").permitAll()
			  .requestMatchers("/login /**").permitAll()
			  .requestMatchers("/usuario/denegado").permitAll()
			  .requestMatchers("/*/index").permitAll()
			  .requestMatchers("/*/nuevo").hasRole("ADMIN")
			  .requestMatchers("/*/editar**").hasRole("ADMIN")
			  .requestMatchers("/*/eliminar**").hasRole("ADMIN")
			  .requestMatchers("/*/matricular**").hasAnyRole("ADMIN","USER")
			  .anyRequest().authenticated())
		.exceptionHandling((exceptionHandling)->
		   exceptionHandling
		   		.accessDeniedPage("/usuario/denegado"))
		.formLogin(form -> 
			form
				.permitAll()	
				.usernameParameter("username")
				.passwordParameter("password")
				.loginPage("/usuario/login")
				.failureUrl("/usuario/login?error=true")
				.loginProcessingUrl("/login")
				.defaultSuccessUrl("/",true)
			)
		.logout((logout)->
			logout.permitAll()
				.logoutSuccessUrl("/login")
				.clearAuthentication(true))
		.build();
	}
}
