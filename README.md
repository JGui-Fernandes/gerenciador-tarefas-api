# Gerenciador de Tarefas — API REST

API RESTful para gerenciamento de tarefas com autenticação JWT, controle de acesso por papéis (RBAC) e documentação interativa via Swagger.

---

## Sumário

- [Visão Geral](#visão-geral)
- [Decisões Arquiteturais](#decisões-arquiteturais)
- [Modelagem de Dados](#modelagem-de-dados)
- [Fluxo de Requisições](#fluxo-de-requisições)
- [Configuração e Deploy](#configuração-e-deploy)
- [Testes Automatizados](#testes-automatizados)

---

## Visão Geral

### Objetivo

O sistema permite o cadastro e gerenciamento de usuários e tarefas. Cada tarefa possui um criador, um responsável (assignee) e pode ter participantes. O ciclo de vida da tarefa é controlado por um enum de status (`BACKLOG → TODO → IN_PROGRESS → REVIEW → DONE`). O acesso aos recursos é protegido por autenticação via JWT, com diferenciação entre os papéis `USER` e `ADMIN`.

### Contexto de uso

O projeto foi desenvolvido como trabalho acadêmico na Unisinos e serve como referência para uma API de gerenciamento colaborativo de tarefas, podendo ser consumido por qualquer front-end ou cliente HTTP.

### Instruções de instalação

**Pré-requisitos:**

- Java 21+
- Maven 3.9+
- MySQL 8+

**Passos:**

```bash
# 1. Clone o repositório
git clone https://github.com/<seu-usuario>/gerenciador-tarefas-api.git
cd gerenciador-tarefas-api

# 2. Crie o banco de dados no MySQL
CREATE DATABASE `gerenciador-tarefas`;

# 3. Configure as credenciais em src/main/resources/application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/gerenciador-tarefas
spring.datasource.username=<seu-usuario>
spring.datasource.password=<sua-senha>
jwt.secret=<chave-secreta-de-no-minimo-32-chars>

# 4. Execute a aplicação
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080`.  
A documentação Swagger estará disponível em `http://localhost:8080/swagger-ui/index.html`.

---

## Decisões Arquiteturais

### Arquitetura em Camadas (Layered Architecture)

O projeto adota a arquitetura em camadas clássica do ecossistema Spring Boot, dividida em:

| Camada | Responsabilidade |
|--------|-----------------|
| **Controller** | Recebe requisições HTTP, valida o payload com Bean Validation e delega ao Service |
| **Service** | Contém toda a lógica de negócio; é a única camada que conhece as regras da aplicação |
| **Repository** | Interface com o banco de dados via Spring Data JPA |
| **Entity** | Mapeamento objeto-relacional das tabelas do banco |
| **DTO** | Objetos de transferência de dados, separando o contrato da API da estrutura interna |

**Justificativa:** A separação por camadas garante baixo acoplamento, alta coesão e facilita a testabilidade — especialmente os testes de controller, onde o Service pode ser substituído por um mock sem impactar as demais camadas.

### Separação entre Interface e Implementação (Service)

Os serviços são definidos como interfaces (`AuthService`, `TaskService`, `UserService`) com suas implementações separadas (`AuthServiceImpl`, etc.).

**Justificativa:** Permite injeção de dependência baseada em abstração, facilita a criação de mocks nos testes e deixa o código aberto para extensão sem modificação (princípio Open/Closed do SOLID).

### Autenticação Stateless com JWT

A autenticação é feita com tokens JWT, processados pelo filtro `JwtAuthFilter` antes de qualquer controller. O logout é implementado com uma blacklist em memória (`TokenBlacklistService`), invalidando o token sem necessidade de estado no servidor.

**Justificativa:** Tokens JWT eliminam a necessidade de sessões server-side, tornando a API stateless e compatível com escalabilidade horizontal. A blacklist resolve o problema de invalidação imediata de tokens sem abrir mão das vantagens do modelo stateless para a maioria das requisições.

### Controle de Acesso por Papel (RBAC)

O `SecurityConfig` define regras de acesso baseadas nos papéis `USER` e `ADMIN`. Endpoints administrativos (listar todos os usuários, buscar/editar/deletar usuários por ID) são restritos ao papel `ADMIN`.

**Justificativa:** O RBAC centraliza as regras de autorização em um único ponto, evitando verificações espalhadas pela lógica de negócio.

### DTOs para Request e Response

Todos os endpoints recebem e retornam DTOs específicos, nunca entidades JPA diretamente.

**Justificativa:** Evita a exposição acidental de dados sensíveis (como `password`), permite evoluir o modelo de dados sem quebrar o contrato da API e torna as validações de entrada explícitas e documentadas.

### Tratamento Centralizado de Exceções

O `GlobalExceptionHandler` (anotado com `@RestControllerAdvice`) intercepta todas as exceções lançadas pelos serviços e retorna respostas padronizadas no formato `ErrorMessageResponse` ou `ValidationErrorResponse`.

**Justificativa:** Garante consistência no formato de erro em toda a API e desacopla o tratamento de erros da lógica de negócio.

### Soft Delete de Usuários

A deleção de usuários não remove o registro do banco; em vez disso, define `isActive = false`.

**Justificativa:** Preserva a integridade referencial com tarefas criadas, atribuídas ou das quais o usuário participou, evitando erros de constraint e permitindo auditoria histórica.

### Documentação com SpringDoc OpenAPI (Swagger)

Todos os endpoints são anotados com `@Operation`, `@ApiResponse` e exemplos de payload.

**Justificativa:** A documentação vive junto ao código, garantindo que esteja sempre atualizada. A interface Swagger UI permite testar a API sem ferramentas externas.

---

## Modelagem de Dados

### Diagrama

```
┌─────────────────────────────────┐        ┌──────────────────────────────────┐
│            users                │        │             tasks                │
├─────────────────────────────────┤        ├──────────────────────────────────┤
│ id          BIGINT  PK          │        │ id           BIGINT  PK          │
│ name        VARCHAR             │        │ name         VARCHAR             │
│ email       VARCHAR  UNIQUE     │        │ description  TEXT                │
│ password    VARCHAR             │        │ deadline     DATE                │
│ birth_date  DATE                │        │ status       INT (enum ordinal)  │
│ phone       VARCHAR             │        │ creator_id   BIGINT  FK → users  │
│ role        VARCHAR (enum)      │        │ assignee_id  BIGINT  FK → users  │
│ is_active   BOOLEAN             │        │ created_at   DATETIME            │
│ created_at  DATETIME            │        │ updated_at   DATETIME            │
│ updated_at  DATETIME            │        └──────────┬───────────────────────┘
└─────────────────────────────────┘                   │
                 │                                    │
                 └──────────┬─────────────────────────┘
                            │
               ┌────────────▼────────────┐
               │     task_participants   │
               ├─────────────────────────┤
               │ task_id   BIGINT  FK    │
               │ user_id   BIGINT  FK    │
               └─────────────────────────┘
```

### Descrição das tabelas

**`users`**  
Armazena os usuários do sistema. O campo `role` define o papel (`USER` ou `ADMIN`). O campo `is_active` implementa o soft delete. A entidade implementa `UserDetails` do Spring Security, integrando-se diretamente ao mecanismo de autenticação.

**`tasks`**  
Armazena as tarefas. O campo `status` persiste o ordinal do enum `TaskStatus` (`BACKLOG=0, TODO=1, IN_PROGRESS=2, REVIEW=3, DONE=4`). Cada tarefa possui um criador (`creator_id`) e opcionalmente um responsável (`assignee_id`), ambos chaves estrangeiras para `users`.

**`task_participants`**  
Tabela de junção para o relacionamento `ManyToMany` entre `tasks` e `users`, representando os participantes de uma tarefa.

---

## Fluxo de Requisições

Toda requisição passa pelo filtro `JwtAuthFilter` antes de chegar ao controller. O filtro valida o token JWT, verifica se está na blacklist e popula o `SecurityContext` com o usuário autenticado.

### Auth — `/auth`

| Método | Endpoint | Autenticação | Descrição |
|--------|----------|-------------|-----------|
| POST | `/auth/login` | Pública | Autentica o usuário e retorna um token JWT |
| POST | `/auth/logout` | Pública* | Invalida o token atual (adiciona à blacklist) |

**Exemplo — Login:**

```http
POST /auth/login
Content-Type: application/json

{
  "email": "usuario@email.com",
  "password": "senha123"
}
```

Resposta `200 OK`:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### Users — `/users`

| Método | Endpoint | Papel requerido | Descrição |
|--------|----------|----------------|-----------|
| POST | `/users` | Pública | Cadastra novo usuário |
| GET | `/users` | USER | Retorna dados do usuário autenticado |
| PUT | `/users` | USER | Edita o próprio usuário autenticado |
| DELETE | `/users` | USER | Desativa o próprio usuário (soft delete) |
| GET | `/users/list` | ADMIN | Lista todos os usuários ativos |
| GET | `/users/{id}` | ADMIN | Busca usuário por ID |
| PUT | `/users/{id}` | ADMIN | Edita usuário por ID |
| DELETE | `/users/{id}` | ADMIN | Desativa usuário por ID |

**Exemplo — Criar usuário:**

```http
POST /users
Content-Type: application/json

{
  "name": "João Silva",
  "email": "joao@email.com",
  "password": "senha123",
  "birthDate": "1995-04-20",
  "phone": "51999999999"
}
```

Resposta `201 Created` (sem body).

### Tasks — `/tasks`

| Método | Endpoint | Papel requerido | Descrição |
|--------|----------|----------------|-----------|
| POST | `/tasks` | USER | Cria nova tarefa |
| GET | `/tasks/{id}` | USER | Busca tarefa por ID |
| GET | `/tasks?assignedTo={userId}` | USER | Lista tarefas atribuídas a um usuário |
| PUT | `/tasks/{id}` | USER | Atualiza tarefa |
| DELETE | `/tasks/{id}` | USER | Remove tarefa |

**Exemplo — Criar tarefa:**

```http
POST /tasks
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Implementar autenticação",
  "description": "Configurar JWT no projeto",
  "deadline": "2026-07-01",
  "assigneeId": 2,
  "participantIds": [3, 4]
}
```

Resposta `201 Created` (sem body).

**Exemplo — Buscar tarefa por ID:**

```http
GET /tasks/1
Authorization: Bearer <token>
```

Resposta `200 OK`:
```json
{
  "id": 1,
  "name": "Implementar autenticação",
  "description": "Configurar JWT no projeto",
  "status": "IN_PROGRESS",
  "deadline": "2026-07-01",
  "creator": { "id": 1, "name": "João Silva" },
  "assignee": { "id": 2, "name": "Maria Souza" },
  "participants": [...],
  "createdAt": "2026-06-10T10:00:00",
  "updatedAt": "2026-06-10T12:00:00"
}
```

### Formato padrão de erros

```json
{
  "statusCode": 404,
  "message": "Nenhuma tarefa encontrada com esse ID"
}
```

Para erros de validação de campos:
```json
{
  "statusCode": 400,
  "fields": [
    { "field": "name", "message": "must not be blank" }
  ]
}
```

---

## Configuração e Deploy

### Variáveis de ambiente / application.properties

```properties
# Banco de dados
spring.datasource.url=jdbc:mysql://localhost:3306/gerenciador-tarefas
spring.datasource.username=root
spring.datasource.password=sua_senha

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# JWT
jwt.secret=sua-chave-secreta-de-no-minimo-32-caracteres
```

> ⚠️ **Atenção:** Nunca comite credenciais reais no repositório. Use variáveis de ambiente ou um arquivo `.env` ignorado pelo `.gitignore` em ambientes de produção.

### Dependências principais

| Dependência | Versão | Função |
|-------------|--------|--------|
| Spring Boot | 4.0.6 | Framework principal |
| Spring Data JPA | — | Persistência com Hibernate |
| Spring Security | — | Autenticação e autorização |
| Spring Web MVC | — | Camada HTTP REST |
| Spring Validation | — | Bean Validation nos DTOs |
| JJWT | 0.11.5 | Geração e validação de tokens JWT |
| MySQL Connector | — | Driver JDBC para MySQL |
| SpringDoc OpenAPI | 3.0.2 | Documentação Swagger UI |
| Lombok | — | Redução de boilerplate |
| Jackson Datatype JSR310 | — | Serialização de `LocalDate`/`LocalDateTime` |

### Executando com Maven

```bash
# Compilar e executar testes
./mvnw verify

# Executar a aplicação
./mvnw spring-boot:run

# Gerar o JAR
./mvnw package
java -jar target/gerenciador-tarefas-0.0.1-SNAPSHOT.jar
```

---

## Testes Automatizados

### Estratégia

Os testes são **testes de integração de controller**, utilizando `@SpringBootTest` combinado com `@AutoConfigureMockMvc`. Essa abordagem sobe o contexto completo do Spring (incluindo filtros de segurança JWT) e utiliza o `MockMvc` para simular requisições HTTP reais, sem necessidade de servidor embarcado.

A camada de serviço é substituída por **mocks Mockito** (`@MockitoBean`), isolando os testes do banco de dados e da lógica de negócio, e permitindo testar exclusivamente o comportamento do controller: mapeamento de rotas, validação de payload, serialização de resposta e regras de autorização.

### Estrutura dos testes

```
src/test/
└── controller/
│   ├── LoginTest.java           # Testes de autenticação (login/logout)
│   ├── TaskControllerTest.java  # Testes dos endpoints de tarefas
│   └── UserControllerTest.java  # Testes dos endpoints de usuários
├── mocks/
│   ├── request/                 # Fábricas de payloads de entrada (LoginBody, TaskBody, UserBody)
│   └── response/                # Fábricas de respostas mockadas (AuthMock, TaskMock, UserMock, ErrorMock)
├── constants/
│   ├── Endpoints.java           # Constantes das URLs testadas
│   ├── ErrorMessages.java       # Constantes das mensagens de erro esperadas
│   └── JsonPath.java            # Constantes dos caminhos JsonPath verificados
└── util/
    ├── JsonUtils.java           # Utilitário de serialização para os testes
    └── LoginTestService.java    # Utilitário que realiza login e retorna o header Authorization
```

### Cenários cobertos

**LoginTest** — 5 cenários:
- Login bem-sucedido com credenciais válidas → `200 OK`
- Login com senha incorreta → `401 Unauthorized`
- Login com campos ausentes/inválidos → `400 Bad Request`
- Logout com token válido → `204 No Content`
- Logout com header `Authorization` malformado → `400 Bad Request`

**TaskControllerTest** — 11 cenários:
- Criar tarefa com todos os campos → `201 Created`
- Criar tarefa apenas com campos obrigatórios → `201 Created`
- Criar tarefa com payload vazio → `400 Bad Request`
- Buscar tarefa por ID existente → `200 OK` + campos obrigatórios no body
- Buscar tarefa por ID inexistente → `404 Not Found` + mensagem de erro
- Listar tarefas atribuídas a um usuário → `200 OK` + array de tarefas
- Buscar tarefas de usuário inexistente → `404 Not Found`
- Atualizar todos os campos de uma tarefa → `200 OK` + dados atualizados
- Atualizar apenas o nome → `200 OK`
- Deletar tarefa existente → `204 No Content`
- Deletar tarefa inexistente → `404 Not Found`
- Tentar deletar sem autenticação → `401 Unauthorized`

**UserControllerTest** — cenários cobrindo:
- Criar usuário com todos os campos → `201 Created`
- Criar usuário apenas com campos obrigatórios → `201 Created`
- Criar usuário com email duplicado → `400 Bad Request`
- Buscar, editar e deletar usuários (com e sem permissão `ADMIN`)
- Operações no próprio usuário autenticado (sem ID na rota)
- Tentativas sem autenticação → `401 Unauthorized`
- Tentativas sem papel adequado → `403 Forbidden`

### Padrões adotados nos testes

Cada teste tem um comentário descrevendo o **cenário** e o **resultado esperado**, facilitando a leitura e a rastreabilidade:

```java
// Cenário: Criar tarefa com todos os campos preenchidos com usuário logado.
// Esperado: 201 Created (sem body na resposta).
@Test
void shouldCreateTaskWithAllAttributesSuccessfully() throws Exception { ... }
```

Os nomes de método seguem o padrão `should<Ação><Contexto>[Error]`, tornando explícito se o teste verifica o caminho feliz ou um cenário de erro.

### Executando os testes

```bash
./mvnw test
```

Os testes requerem que a aplicação consiga inicializar o contexto Spring. As dependências de banco de dados são desacopladas via `@MockitoBean` nos testes de controller, então **não é necessário um banco de dados ativo** para executar a suíte de testes.
