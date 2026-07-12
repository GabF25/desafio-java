# 📚 API de Livros — Desafio Técnico Java

Projeto desenvolvido como parte de um desafio técnico para a vaga de Desenvolvedor
Java Júnior. A aplicação é uma **API REST** para gerenciamento de livros, com CRUD
completo, validação de dados, tratamento de erros e documentação interativa via
Swagger.

## 🚀 Tecnologias Utilizadas

- Java 21
- Spring Boot 4.1
- Spring Data JPA / Hibernate
- H2 Database (banco em memória)
- Bean Validation
- springdoc-openapi (Swagger UI)
- JUnit 5 + MockMvc
- Maven

## 📋 Funcionalidades

- Cadastro de livros (título, autor e ano de publicação)
- Listagem, atualização e remoção de livros
- Validação de payload (campos obrigatórios, tamanho, ano não futuro)
- Tratamento de erros com status HTTP coerentes (`400`, `404`, `409`)

## 🛣️ Rotas

| Método | Rota              | Ação      |
|--------|-------------------|-----------|
| GET    | `/api/livro`      | Listar    |
| POST   | `/api/livro`      | Criar     |
| PUT    | `/api/livro/{id}` | Atualizar |
| DELETE | `/api/livro/{id}` | Remover   |

## 📖 Documentação (Swagger)

Com a aplicação no ar, acesse: **http://localhost:8080/api/swagger-ui.html**

## 📦 Como rodar o projeto localmente

```bash
# Clone o repositório
git clone https://github.com/GabF25/desafio-java.git

# Acesse a pasta
cd desafio-java

# Rode a aplicação
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080/api`.

## 🧪 Testes

```bash
./mvnw test
```
