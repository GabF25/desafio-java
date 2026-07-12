package com.gabriel.desafio_java.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI livrosOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Livros — Desafio Java")
                        .description("""
                                API REST para gerenciamento de livros (CRUD).
                                Permite listar, criar, atualizar e remover livros,
                                com validação de payload e tratamento de erros
                                (400 inválido, 404 inexistente, 409 título duplicado).""")
                        .version("0.0.1-SNAPSHOT")
                        .contact(new Contact()
                                .name("Gabriel Felipe")));
    }
}
