# 🔑 Microserviço de Autenticação (auth-cinema)

| Aluno                        | Matrícula   | Disciplina                                           |
|------------------------------|-------------|------------------------------------------------------|
| Cleber de Oliveira Brant     | 200061216   | Técnicas de Programação em Plataformas Emergentes     |

---

## Sobre

O **auth-cinema** é o microserviço responsável pela autenticação, cadastro e gerenciamento de usuários no sistema de cinema. Ele fornece endpoints para registro, login, gerenciamento de perfis e geração de tokens JWT, garantindo a segurança e integridade do acesso aos demais serviços da arquitetura.

---

## Integração com a Arquitetura

- **Tecnologias:** Spring Boot 3, Java 21, Spring Security, JWT, PostgreSQL, Flyway, Lombok, Swagger (SpringDoc OpenAPI)
- **Banco de Dados:** PostgreSQL exclusivo para autenticação (`auth_cinema`)
- **Comunicação:** REST, com autenticação baseada em JWT
- **Escopo:** Autenticação, registro, gerenciamento de usuários e localidades

---

## Fluxo de Autenticação

1. **Cadastro e Login:**  
   - O usuário se cadastra ou faz login no microserviço de autenticação.
   - Recebe um token JWT válido por um período definido.
2. **Acesso aos Serviços:**  
   - O token JWT é enviado no header `Authorization` das requisições aos demais microserviços (ex: backend do cinema).
   - Cada serviço valida o token localmente, garantindo autenticação e autorização.

---

## Principais Funcionalidades

- **Cadastro de usuários (admin e clientes)**
- **Login e geração de tokens JWT**
- **Gerenciamento de perfis e localidades**
- **Controle de acesso por roles (admin, user)**
- **Documentação de API via Swagger**
- **Versionamento de banco de dados com Flyway**

---

## Estrutura de Pastas

```
auth-cinema/
├── src/
│ └── main/
│ ├── java/
│ │ └── com/
│ │ └── cleber/
│ │ └── auth_cinema/
│ │ ├── config/
│ │ ├── controller/
│ │ ├── dto/
│ │ ├── enums/
│ │ ├── exception/
│ │ ├── model/
│ │ ├── repositories/
│ │ ├── security/
│ │ ├── services/
│ │ └── AuthCinemaApplication.java
│ └── resources/
│ ├── application.properties
│ └── db/migration/
└── Dockerfile
```

---

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3**
- **Spring Security**
- **JWT (io.jsonwebtoken)**
- **PostgreSQL 16**
- **Flyway**
- **Lombok**
- **Swagger (SpringDoc OpenAPI)**
- **Docker**

---

## Ambientação

### 1. Rodando com Docker

O microserviço é orquestrado pelo `docker-compose.yml` da solução completa.

**Comandos básicos:**
docker-compose up --build -d

text
**Parar os containers:**
docker-compose down

text

---

### 2. Rodando localmente (IDE)

1. **Instale o PostgreSQL 16** e crie o banco `auth_cinema`.
2. **Configure o `application.properties`** com as credenciais do banco.
3. **Execute a classe principal** `AuthCinemaApplication`.
4. **O Flyway aplica as migrations automaticamente.**

---

## Migrations e Inicialização

- **Flyway:** Versionamento automático do banco de dados.
- **Usuário admin padrão:**  
  - **E-mail:** `admin@gmail.com`
  - **Senha:** `admin`
  - **Role:** `ROLE_ADMIN`
    
- **Usuário padrão:**  
  - **E-mail:** `usuario@gmail.com`
  - **Senha:** `usuario`
  - **Role:** `ROLE_USER`

---

## Documentação da API

- **Swagger UI:** [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)

---

## Considerações Finais

- **Segurança:** Autenticação robusta via JWT, com validação de token em todos os serviços.
- **Escalabilidade:** O microserviço é independente e pode ser escalado conforme a demanda.
- **Integração:** Fácil integração com outros serviços via REST e JWT.

---
