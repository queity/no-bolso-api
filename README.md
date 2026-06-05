# no-bolso-api
API de controle de finanças pessoais.

## Tecnologias

- Java 21 + Spring Boot 3.3
- Maven
- H2 in-memory (perfil `dev`)
- Swagger UI (springdoc-openapi)
- Docker

## Como executar

### Local

**Pré-requisitos:** JDK 21 e Maven instalados.

```bash
mvn spring-boot:run
```

### Docker

```bash
docker-compose up --build
```

A API estará disponível em `http://localhost:8080`.

## Documentação

```
http://localhost:8080/api/swagger-ui.html
```

## Variáveis de ambiente

| Variável | Padrão | Descrição |
|---|---|---|
| `PORT` | `8080` | Porta do servidor |
| `SPRING_PROFILES_ACTIVE` | `dev` | Perfil ativo (`dev` usa mock em memória) |
| `CORS_ALLOWED_ORIGINS` | `*` | Origens permitidas para CORS |
