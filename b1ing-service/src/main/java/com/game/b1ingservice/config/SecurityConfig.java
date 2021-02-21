package com.game.b1ingservice.config;

import com.game.b1ingservice.exception.CustomizedResponseEntityExceptionHandler;
import com.game.b1ingservice.interceptor.JwtAuthorizationTokenFilter;
import com.game.b1ingservice.interceptor.LogTransactionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthorizationTokenFilter jwtAuthorizationTokenFilter;
    @Autowired
    private CustomizedResponseEntityExceptionHandler exceptionHandler;
    @Autowired
    private LogTransactionFilter logTransactionFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/api/test/**",
                        "/api/master/**",
                        "/api/user/prefix",
                        "/api/user/register",
                        "/api/user/auth",
                        "/api/file/downloadFile/**"
                ).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilterBefore(logTransactionFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilterBefore(
                        jwtAuthorizationTokenFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
        ;
        http.exceptionHandling()
                .authenticationEntryPoint(exceptionHandler);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
        web.ignoring().antMatchers(
                "/api/master/**",
                "/api/test/**",
                "/api/admin/auth"
        );
    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        final CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Collections.singletonList("*"));
//        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
//        configuration.setAllowCredentials(true);
//        configuration.setAllowedHeaders(Collections.singletonList("*"));
//        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

//    cherry pick test

}

