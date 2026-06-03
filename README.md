# no-bolso-api
API para controle de finanças pessoais

## Tecnologias

- Java 21
- Spring Boot 3.3
- Maven
- H2 (in-memory) — mock local
- Swagger UI (springdoc-openapi)
- Docker

## Como executar

### Local

**Pré-requisitos:** JDK 21 e Maven instalados.

```bash
mvn spring-boot:run
```

### Docker

**Pré-requisito:** Docker instalado.

```bash
# buildar e subir
docker-compose up --build

# subir em background
docker-compose up -d --build

# derrubar
docker-compose down
```

A API estará disponível em `http://localhost:8080`.

## Documentação

Após iniciar o projeto, acesse o Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

## Endpoints

| Método | Path | Descrição |
|--------|------|-----------|
| GET | `/api/transacoes` | Pesquisar transações (suporta filtros) |
| GET | `/api/transacoes/{id}` | Buscar transação por ID |
| POST | `/api/transacoes` | Criar transação |
| PUT | `/api/transacoes/{id}` | Atualizar transação |
| DELETE | `/api/transacoes/{id}` | Deletar transação |
| GET | `/api/transacoes/saldo` | Consultar saldo |
| GET | `/api/transacoes/recentes` | Últimas transações |

## Filtros disponíveis (GET /api/transacoes)

| Parâmetro | Tipo | Descrição |
|-----------|------|-----------|
| `tipo` | int | 1=Pix, 2=Depósito, 3=Transferência, 4=Saque, 5=Outros |
| `direcao` | int | 1=Entrada, 2=Saída |
| `categoria` | int | 1=Alimentação, 2=Lazer, 3=Assinatura, 4=Casa, 5=Educação, 6=Receitas Fixas, 7=Outros |
| `descricao` | string | Busca por texto na descrição |
| `dataInicio` | LocalDateTime | Data início do período (ex: `2026-01-01T00:00:00`) |
| `dataFim` | LocalDateTime | Data fim do período (ex: `2026-12-31T23:59:59`) |
