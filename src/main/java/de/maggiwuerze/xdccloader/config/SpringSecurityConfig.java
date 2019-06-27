package de.maggiwuerze.xdccloader.config;

import de.maggiwuerze.xdccloader.security.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.header.writers.frameoptions.WhiteListedAllowFromStrategy;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.lang.Exception;
import java.util.*;


//@EnableWebMvc
@Configuration
class SpringSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    @Autowired
    CustomUserDetailService userDetailsService;


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) {

        auth.authenticationProvider(authenticationProvider());

    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http
                .headers()
                .frameOptions().sameOrigin()
                .and()
                .authorizeRequests()
                .requestMatchers(PathRequest.toH2Console()).permitAll()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .antMatchers("/webjars/**", "/resources/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .formLogin()
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .and()
                .logout()
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/login?logout=true")
                .logoutUrl("/logout");

//        http
//            .headers()
//            .frameOptions().sameOrigin()
//            .and()
//            .csrf().disable().authorizeRequests()
//            .antMatchers("/login*").permitAll()
//            .anyRequest().authenticated()
//            .and()
//            .formLogin().loginPage("/login")
//            .defaultSuccessUrl("/", true)
//            .failureUrl("/login?error=true")
//            .and()
//            .logout()
//            .invalidateHttpSession(true)
//            .logoutSuccessUrl("/login")
//            .logoutUrl("/logout");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry
                .addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
//        registry
//                .addResourceHandler("/resources/**")
//                .addResourceLocations("/resources/")
//        registry
//                .addResourceHandler("/webjars/**")
//                .addResourceLocations("classpath:/webjars/")
//                .resourceChain(false)

    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }


}