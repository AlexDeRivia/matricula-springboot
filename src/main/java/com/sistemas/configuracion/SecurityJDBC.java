package com.sistemas.configuracion;

import static org.springframework.security.config.Customizer.withDefaults;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityJDBC {
	
	@Autowired private DataSource dataSource;
	@Autowired PasswordEncoder passwordEncoder;
	
	@Bean
	UserDetailsService userDetailsService() {
		JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
		
		manager.setRolePrefix("ROLE_");
		manager.setUsersByUsernameQuery(
				"select nombre as username, "
				+ "clave as password, estado as enabled "
				+ "from usuario as users where nombre = ?");
		manager.setAuthoritiesByUsernameQuery(
				"select nombre as username, rol as authority "
				+ "from usuario as authorities where nombre = ?");
		
		return manager;
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		return http
				.httpBasic(withDefaults())
				.authorizeHttpRequests((request)->
					request
						.requestMatchers("/css/**").permitAll()
						.requestMatchers("/images/**").permitAll()
						.requestMatchers("/js/**").permitAll()
						.requestMatchers("/login/**").permitAll()
						.requestMatchers("/usuario/denegado").permitAll()
						.requestMatchers("/usuario/poblar").permitAll()
						.requestMatchers("/*/index").permitAll()
						.requestMatchers("/*/nuevo").hasRole("ADMIN")
						.requestMatchers("/*/editar/*").hasRole("ADMIN")
						.requestMatchers("/*/eliminar/*").hasRole("ADMIN")
						.requestMatchers("/*/matricular/*").hasAnyRole("ADMIN", "USER")
						.anyRequest().authenticated())
				.exceptionHandling((exceptionHandling) ->
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
				.logout((logout) ->
						logout.permitAll()
							.logoutSuccessUrl("/login")
							.clearAuthentication(true))
				.build();
		
		
		
	}
	
	
	
}

