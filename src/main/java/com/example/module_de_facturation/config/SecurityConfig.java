package com.example.module_de_facturation.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration de sécurité pour le module de facturation
 * Fournit une authentification basique en mémoire
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configuration de la chaîne de filtres de sécurité
     * @param http la configuration HTTP
     * @return la chaîne de filtres configurée
     * @throws Exception en cas d'erreur de configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions().disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/facturation/**").authenticated()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().permitAll()
                )
                .httpBasic(httpBasic -> {});

        return http.build();
    }


    /**
     * Service de gestion des utilisateurs en mémoire
     * Les mots de passe sont lus depuis les variables d'environnement ADMIN_PASSWORD et USER_PASSWORD
     * @return le service d'utilisateurs
     */
    @Bean
    public UserDetailsService userDetailsService(@Value("${ADMIN_PASSWORD}") String adminPassword,
                                                 @Value("${USER_PASSWORD}") String userPassword) {
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode(adminPassword))
                .roles("ADMIN")
                .build();

        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode(userPassword))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    /**
     * Encodeur de mots de passe
     * @return l'encodeur BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

