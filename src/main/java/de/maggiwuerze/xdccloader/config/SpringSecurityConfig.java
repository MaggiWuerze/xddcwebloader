package de.maggiwuerze.xdccloader.config;

import de.maggiwuerze.xdccloader.model.entity.User;
import de.maggiwuerze.xdccloader.persistency.UserRepository;
import de.maggiwuerze.xdccloader.security.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.Exception;


//@EnableWebMvc
@PropertySource("classpath:application.properties")
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
                .successHandler(authenticationSuccessHandler())
                .failureUrl("/login?error=true")
                .and()
                .logout()
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/login?logout=true")
                .logoutUrl("/logout");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry
                .addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

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

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {

            @Autowired
            UserRepository userRepository;

            private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                //do what you want with

                Long sessionTimeout;
                if (authentication != null) {

                    String currentPrincipalName = authentication.getName();

                    User user = userRepository.findUserByName(currentPrincipalName).orElse(null);
                    sessionTimeout = user.getUserSettings().getSessionTimeout();

                    request.getSession().setMaxInactiveInterval(sessionTimeout.intValue());
                }
                handle(request, response, authentication);
                clearAuthenticationAttributes(request);
            }

            protected void handle(HttpServletRequest request,
                                  HttpServletResponse response, Authentication authentication)
                    throws IOException {

                String targetUrl = "/";
                if (response.isCommitted()) {
                    return;
                }

                redirectStrategy.sendRedirect(request, response, targetUrl);
            }

            protected void clearAuthenticationAttributes(HttpServletRequest request) {
                HttpSession session = request.getSession(false);
                if (session == null) {
                    return;
                }
                session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            }
        };
    }


}