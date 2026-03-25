package com.virtuallink.virtualbank.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration OpenAPI 3 (Springdoc).
 *
 * <p>Swagger UI disponible sur : <a href="http://localhost:8080/swagger-ui.html">/swagger-ui.html</a>
 * <br>Spec JSON sur : <a href="http://localhost:8080/v3/api-docs">/v3/api-docs</a>
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI virtualBankOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("VirtualBank API")
                        .description("""
                                API REST de gestion de comptes bancaires.

                                ## Fonctionnalités
                                - Création et consultation de comptes
                                - Dépôt et retrait
                                - Virement entre comptes
                                - Historique des opérations
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("VirtualLink")
                                .email("contact@virtuallink.com")
                                .url("https://github.com/virtuallink/virtualbank"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentation complète du projet")
                        .url("https://github.com/virtuallink/virtualbank/wiki"))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Serveur local"),
                        new Server().url("https://api.virtualbank.io").description("Production")
                ));
    }
}

