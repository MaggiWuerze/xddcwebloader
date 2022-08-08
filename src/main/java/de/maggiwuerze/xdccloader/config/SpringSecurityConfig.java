package de.maggiwuerze.xdccloader.config;

import de.maggiwuerze.xdccloader.security.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
class SpringSecurityConfig {

	private final CustomUserDetailService userDetailsService;
	private final CustomAuthSuccessHandler customAuthSuccessHandler;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
				http
					.headers()
					.frameOptions().sameOrigin()
					.and()
					.authorizeRequests()
					.antMatchers("/register").permitAll()
					.requestMatchers(PathRequest.toH2Console()).permitAll()
					.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
					.antMatchers("/webjars/**", "/resources/**").permitAll()
					.anyRequest().authenticated()
					.and()
					.csrf().disable()
					.formLogin()
					.loginPage("/login").permitAll()
					.defaultSuccessUrl("/", true)
					.successHandler(customAuthSuccessHandler)
					.failureUrl("/login?error=true")
					.and()
					.logout()
					.invalidateHttpSession(true)
					.logoutSuccessUrl("/login?logout=true")
					.logoutUrl("/logout");

		return http.build();
	}

	@Bean
	public AuthenticationManager authManager(HttpSecurity http, CustomUserDetailService userDetailService)
			throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(userDetailsService)
				.passwordEncoder(encoder())
				.and()
				.build();
	}

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder(11);
	}

}