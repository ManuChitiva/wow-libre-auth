package com.auth.wow.libre.infrastructure.security;

import com.auth.wow.libre.application.services.jwt.JwtPortService;
import com.auth.wow.libre.domain.model.security.UserDetailsServiceCustom;
import com.auth.wow.libre.infrastructure.filter.AuthenticationFilter;
import com.auth.wow.libre.infrastructure.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static com.auth.wow.libre.domain.model.constant.Constants.HEADER_TRANSACTION_ID;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsServiceCustom userDetailsServiceCustom;
    private final JwtPortService jwtPort;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter,
                                 UserDetailsServiceCustom userDetailsServiceCustom,
                                 JwtPortService jwtPort) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsServiceCustom = userDetailsServiceCustom;
        this.jwtPort = jwtPort;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000",
                "http://127.0.0.1:3000", "http://localhost:3001", "http://127.0.0.1:3001"));
        corsConfiguration.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.OPTIONS.name(),
                HttpMethod.DELETE.name()
        ));
        corsConfiguration.setAllowedHeaders(Arrays.asList(
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.AUTHORIZATION,
                HEADER_TRANSACTION_ID
        ));
        corsConfiguration.setAllowCredentials(true); // Permitir cookies y cabeceras de autorización

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(
                        endpoints -> endpoints.requestMatchers("/api/auth/login").authenticated()
                ).addFilterBefore(new AuthenticationFilter(authenticationProvider(), jwtPort),
                        UsernamePasswordAuthenticationFilter.class)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())).csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        request.requestMatchers(
                                        //INTERNAL API
                                        "/api/account/web/create",
                                        "/api/account/search",
                                        "/api/resources/country",
                                        "/api/resources/faqs",
                                        "/api/resources/benefit",
                                        "/api/account/verify/{account_id}/{account_web_id}",
                                        //SWAGGER
                                        "/v2/api-docs", "/swagger-resources",
                                        "/swagger-resources/**", "/configuration/ui",
                                        "/configuration/security", "/swagger-ui.html", "/webjars/**",
                                        "/v3/api-docs/**", "/swagger-ui/**")
                                .permitAll().anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider()).addFilterBefore(
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsServiceCustom);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


}
