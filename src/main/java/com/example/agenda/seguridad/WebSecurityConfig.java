package com.example.agenda.seguridad;

import com.example.agenda.entidades.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
class WebSecurityConfig {

    @Autowired
    JWTAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.POST, Constans.LOGIN_URL).permitAll()
                        // Solo ADMIN puede borrar
                        .requestMatchers(HttpMethod.DELETE, "/contactos/**").hasAuthority("ROLE_" + Rol.ADMIN)
                        // ADMIN y USER pueden crear (POST) y actualizar (PUT)
                        .requestMatchers(HttpMethod.POST, "/contactos/**").hasAnyAuthority("ROLE_" + Rol.ADMIN, "ROLE_" + Rol.USER)
                        .requestMatchers(HttpMethod.PUT, "/contactos/**").hasAnyAuthority("ROLE_" + Rol.ADMIN, "ROLE_" + Rol.USER)
                        // Cualquier otra petición (como el GET) solo requiere estar autenticado (sirve para ADMIN, USER y READER)
                        .anyRequest().authenticated())
                .addFilterAfter(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}