package com.example.module_de_facturation.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration OpenAPI/Swagger pour la documentation de l'API
 * Fournit une interface utilisateur pour tester les endpoints
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Module de Facturation API")
                        .description("API REST complète pour la gestion des clients et des factures. " +
                                "Permet la création de clients, l'émission de factures avec calculs automatiques " +
                                "des montants (HT, TVA, TTC) et l'export au format JSON.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Équipe de Développement")
                                .email("dev@facturation.com")
                                .url("https://github.com/facturation"))
                        .license(new License()
                                .name("Challenge Technique")
                                .url("https://challenge-facturation.com")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Serveur de développement"),
                        new Server()
                                .url("https://api.facturation.com")
                                .description("Serveur de production")
                ));
    }
}

