package de.maggiwuerze.xdccloader.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.header.writers.frameoptions.WhiteListedAllowFromStrategy
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import java.lang.Exception
import java.util.*


//@EnableWebMvc
@Configuration
class SpringSecurityConfig(

        @Autowired
        val userDetailsService: CustomUserDetailService

) : WebSecurityConfigurerAdapter(), WebMvcConfigurer {

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {

        auth.authenticationProvider(authenticationProvider())

    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {

        http
                .headers()
                .frameOptions().sameOrigin()
                .and()
                .authorizeRequests()
                .requestMatchers(PathRequest.toH2Console()).permitAll()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .antMatchers("/webjars/**", "/resources/**").permitAll()
                .and()
                .csrf().disable()
                .formLogin()
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/", true)
                .failureUrl("/denied")
                .and()
                .logout()
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/login")
                .logoutUrl("/logout")
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {

        registry
                .addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
//        registry
//                .addResourceHandler("/resources/**")
//                .addResourceLocations("/resources/")
//        registry
//                .addResourceHandler("/webjars/**")
//                .addResourceLocations("classpath:/webjars/")
//                .resourceChain(false)

    }

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailsService)
        authProvider.setPasswordEncoder(encoder())
        return authProvider
    }

    @Bean
    fun encoder(): PasswordEncoder {
        return BCryptPasswordEncoder(11)
    }


}