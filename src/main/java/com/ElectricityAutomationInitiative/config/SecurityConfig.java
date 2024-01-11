package com.ElectricityAutomationInitiative.config;

import com.ElectricityAutomationInitiative.security.JwtAuthenticationEntryPoint;
import com.ElectricityAutomationInitiative.security.JwtRequestFilter;

import org.modelmapper.ModelMapper;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.SessionTrackingMode;

import java.util.Collections;


/*
@Configuration:The@Configuration
 annotation is a Spring annotation that indicates that a class is a configuration class.
 Configuration classes are used to define and configure beans for the application context. By marking
 a class with @Configuration, Spring will treat it as a source of bean
definitions, and the beans defined within this class can be accessed and used throughout the
application
 */
@Configuration
/*
The @EnableWebSecurity annotation is a specific Spring Security annotation that is used to enable Spring
 Security's web security features in your application. It should be used on a configuration class
 (a class annotated with @Configuration).

When you annotate a configuration class with @EnableWebSecurity, it does the following:

It tells Spring to enable web security features provided by Spring Security for your application.

It automatically registers the SpringSecurityFilterChain, a special filter chain responsible for
handling security-related aspects of web requests, including authentication and authorization.

It sets up the necessary configurations to secure your application based on the rules defined in your
 */
@EnableWebSecurity
@Order(1)
//for file handling
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final UserDetailsService jwtUserDetailsService;
    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            UserDetailsService jwtUserDetailsService,
            JwtRequestFilter jwtRequestFilter
    ) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/login/admin", "/register/admin", "/api/get-customer-id").permitAll()
                .antMatchers("/api/connections", "/api/verify-otp", "/api/documents/upload","/api/verify","/api/change","/admin/api/change").permitAll()
                .antMatchers("/admin/api/register", "/api/login", "/admin/api/otp","/api/initiate","/admin/api/initiate","/admin/api/verify").permitAll()
                .antMatchers("/admin/api/show").hasRole("ADMIN")
                .anyRequest().authenticated().and()

                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .cors();

        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }



//    @Bean
//    public WebMvcConfigurer crosConfig() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedOrigins("http://localhost:4200")
//                        .allowedMethods(HttpMethod.GET.name(),
//                                HttpMethod.POST.name(),
//                                HttpMethod.DELETE.name(),
//                                HttpMethod.PUT.name())
//                        .allowedHeaders(HttpHeaders.CONTENT_TYPE, HttpHeaders.AUTHORIZATION);
//            }
//        };
//    }

    @Bean
    public ServletContextInitializer servletContextInitializer() {
        return (servletContext) -> {
            servletContext.setSessionTrackingModes(Collections.singleton(SessionTrackingMode.COOKIE));
        };
    }
}
//.antMatchers("/api/register/admin", "/api/login", "/register/admin").permitAll()