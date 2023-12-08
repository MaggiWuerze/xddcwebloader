package de.maggiwuerze.xdccloader.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


//@EnableWebMvc
@PropertySource("classpath:application.properties")
@Configuration
class SpringSecurityConfig implements WebMvcConfigurer {

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {

//		http
//			.headers()
//			.frameOptions().sameOrigin();

		http
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(PathRequest.toH2Console()).permitAll()
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
				.requestMatchers("/webjars/**", "/resources/**").permitAll()
			)
			.csrf(AbstractHttpConfigurer::disable);

		return http.build();
	}


	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry
			.addResourceHandler("/static/**")
			.addResourceLocations("classpath:/static/");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**");
	}

}