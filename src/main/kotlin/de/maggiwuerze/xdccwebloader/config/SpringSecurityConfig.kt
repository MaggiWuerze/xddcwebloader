package de.maggiwuerze.xdccwebloader.config

import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

//@EnableWebMvc
@PropertySource("classpath:application.yaml")
@Configuration
class SpringSecurityConfig : WebMvcConfigurer {
    @Bean
    @Throws(Exception::class)
    fun configure(http: HttpSecurity): SecurityFilterChain? {
        http
            .authorizeHttpRequests(Customizer { authorize ->
                authorize
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                    .requestMatchers(
                        "/",
                        "/webjars/**",
                        "/resources/**",
                        "/swagger/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                    ).permitAll()
                    .anyRequest().permitAll()
            }
            )
        http.csrf(Customizer { obj: CsrfConfigurer<HttpSecurity> -> obj.disable() })
        http.cors(Customizer { obj: CorsConfigurer<HttpSecurity> -> obj.disable() })

        return http.build()
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry
            .addResourceHandler("/static/**")
            .addResourceLocations("classpath:/static/")
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
        registry.addMapping("/initialization**")
    }
}