# no-bolso-api
API de controle de finanças pessoais.

## Tecnologias

- Java 21 + Spring Boot 3.3
- Maven
- H2 in-memory (perfil `dev`)
- Swagger UI (springdoc-openapi)
- Docker
- Railway (deploy em produção)

## Como executar

### Local

**Pré-requisitos:** JDK 21 e Maven instalados.

```bash
mvn spring-boot:run
```

### Docker

```bash
# primeira vez (com build)
docker compose up -d --build

# próximas vezes
docker compose up -d

# parar
docker compose down
```

A API estará disponível em `http://localhost:8080`.

> O backend e o frontend (`tech-challenge-mz`) compartilham a rede `nobolso-network`. Cada projeto cria a rede automaticamente se ela não existir, portanto não há ordem obrigatória para subir os projetos.

## Perfis

| Perfil | Descrição |
|---|---|
| `dev` | Usa repositórios mock em memória (H2). Não requer banco de dados. Ativado por padrão. |
| `prod` | Requer banco de dados real e variáveis de ambiente configuradas. |

O perfil é controlado pela variável `SPRING_PROFILES_ACTIVE`. No perfil `dev`, os dados são populados automaticamente com transações de exemplo ao iniciar a aplicação.

## Documentação

**Local:**
```
http://localhost:8080/api/swagger-ui/index.html
```

**Produção:**
[Swagger UI](https://no-bolso-api-production.up.railway.app/api/swagger-ui/index.html)

## Variáveis de ambiente

| Variável | Padrão | Descrição |
|---|---|---|
| `PORT` | `8080` | Porta do servidor |
| `SPRING_PROFILES_ACTIVE` | `dev` | Perfil ativo (`dev` usa mock em memória) |
| `CORS_ALLOWED_ORIGINS` | `*` | Origens permitidas para CORS |
