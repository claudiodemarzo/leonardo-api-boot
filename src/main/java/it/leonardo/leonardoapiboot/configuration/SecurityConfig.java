package it.leonardo.leonardoapiboot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.security.SecureRandom;

@Configuration
public class SecurityConfig{

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		CookieCsrfTokenRepository cctr = CookieCsrfTokenRepository.withHttpOnlyFalse();
		cctr.setCookiePath("/");
		http.csrf().csrfTokenRepository(cctr);
		return http
				.requiresChannel(channel ->
						channel.anyRequest().requiresSecure())
				.authorizeRequests(authorize ->
						authorize.anyRequest().permitAll())
				.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10, new SecureRandom());
	}


}