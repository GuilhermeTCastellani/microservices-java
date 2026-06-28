<div align="center">

# ⚙️ AutoParts — Backend

**Microsserviços do marketplace de peças automotivas** · Spring Cloud

[![Java](https://img.shields.io/badge/Java-17-007396?logo=openjdk&logoColor=white)](https://openjdk.org)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-Eureka%20%2B%20Gateway-6DB33F?logo=spring&logoColor=white)](https://spring.io/projects/spring-cloud)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?logo=postgresql&logoColor=white)](https://www.postgresql.org)
[![Flyway](https://img.shields.io/badge/Flyway-migrations-CC0200?logo=flyway&logoColor=white)](https://flywaydb.org)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker&logoColor=white)](https://www.docker.com)

</div>

---

## 🧭 Sobre

Backend de microsserviços do **AutoParts** (marketplace de peças automotivas). Arquitetura **Spring Cloud** com descoberta de serviços via **Eureka** e um **API Gateway** como único ponto de entrada do app.

O catálogo é populado com **peças automotivas** (FuelTech, turbinas, pistões, freios, suspensão…) em **moeda mista**: nacionais em **BRL**, importadas em **USD** (convertidas para BRL pelo `currency-service`).

> 🔗 Consumido pelo [app mobile (Expo / React Native)](../TB_AutoParts/README.md).

---

## 🗺️ Arquitetura

```
                          ┌──────────────────┐
        App (Expo) ─────► │  gateway-service │  :8765   ◄── único ponto de entrada
                          └────────┬─────────┘
                                   │ valida JWT (/ws/**) + injeta X-User-*
        ┌──────────────┬──────────┼──────────────┬──────────────┐
        ▼              ▼           ▼              ▼              ▼
  auth-service   product-service  order-service  currency-service
   (db_user)      (db_product)    (db_order)     (db_currency)
        └──────────────┴──────────┼──────────────┴──────────────┘
                                   ▼
                          ┌──────────────────┐
                          │ discovery-service│  :8761   (Eureka)
                          └──────────────────┘
```

| Serviço | Porta | Banco | Papel |
|---|---|---|---|
| **discovery-service** | `8761` | — | Eureka (registro de serviços) |
| **gateway-service** | `8765` | — | Roteamento + valida JWT em `/ws/**` |
| **auth-service** | _interna_ | `db_user` | Cadastro/login (JWT, BCrypt) + endereços por usuário |
| **product-service** | _interna_ | `db_product` | Produtos, categorias e avaliações |
| **order-service** | _interna_ | `db_order` | Pedidos (recalcula preço no servidor) |
| **currency-service** | _interna_ | `db_currency` | Conversão de moeda (USD → BRL etc.) |

> Só **discovery (8761)** e **gateway (8765)** expõem portas ao host. Os demais serviços conversam pela rede interna do Docker, registrados no Eureka.
> `config-service` e `greeting-service` estão **desativados** no compose e **não** são necessários.

---

## 🚀 Como rodar

### Pré-requisitos
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) aberto

### Subir tudo

```bash
docker compose up --build
```

Sobe: `discovery` (8761), 4 Postgres, `currency`, `product`, `auth`, `order` e `gateway` (8765).

### 🔥 Firewall (Windows) — liberar portas para o celular

Necessário só se o app rodar em celular físico. PowerShell **como Administrador**:

```powershell
New-NetFirewallRule -DisplayName "Gateway 8765" -Direction Inbound -LocalPort 8765 -Protocol TCP -Action Allow
New-NetFirewallRule -DisplayName "Expo Metro 8081" -Direction Inbound -LocalPort 8081 -Protocol TCP -Action Allow
```

### ✅ Testar

```bash
curl "http://localhost:8765/products?targetCurrency=BRL"
```

Deve retornar a lista de produtos em JSON.

---

## 🔐 Autenticação e segurança

- **JWT** emitido pelo `auth-service` no login. Payload: `id`, `email`, `type` (**0 = admin**, **1 = comum**).
- O **gateway** é o único ponto exposto. Ele valida o JWT em `/ws/**` e injeta os headers `X-User-Id`, `X-User-Email`, `X-User-Type` para os serviços de baixo.
- **Confiança no gateway:** os serviços internos **não revalidam** o JWT em `/ws/**` — confiam nos headers `X-User-*`. A proteção real é o gateway.

| Tipo de rota | Exemplos | Auth |
|---|---|---|
| Pública | `/auth/**`, `/products/**`, `/currency/**` | ❌ |
| Protegida | `/ws/**` | ✅ `Authorization: Bearer <jwt>` |

> ⚠️ **`type` no objeto `user`:** o `/auth/signin` serializa `type` como nome do enum (`"Admin"`/`"Common"`), enquanto o **JWT** carrega `0`/`1`. O app trata os dois formatos.

### Contas seed
| Conta | Credenciais | Papel |
|---|---|---|
| Admin | `admin@admin.dev` / `admin123` | Cadastra/edita produtos |
| Comum | criada via `/auth/signup` | Compra e avalia |

---

## 📡 Contrato da API

| Método | Rota | Auth | Body / Query |
|---|---|---|---|
| `POST` | `/auth/signup` | público | `{name,email,password}` |
| `POST` | `/auth/signin` | público | `{email,password}` → `{user, token}` |
| `GET` | `/products` | público | `?targetCurrency=BRL&category=&page=&size=` |
| `GET` | `/products/{id}` | público | `?targetCurrency=BRL` |
| `GET` | `/products/{id}/reviews` | público | → `{average, count, items[]}` |
| `POST` | `/ws/products/{id}/reviews` | JWT | `{rating(1-5), comment}` |
| `POST` | `/ws/products` | JWT admin | `{description,brand,model,category,currency,price,imageURL}` |
| `POST` | `/ws/orders` | JWT | `{items:[{productId,quantity}]}` |
| `GET` | `/ws/orders` | JWT | `?targetCurrency=BRL` |
| `GET` | `/ws/addresses` | JWT | → `[AddressDTO]` (só do usuário logado) |
| `POST` | `/ws/addresses` | JWT | `{label,recipientName,phone,zipCode,street,number,district,city,state,complement}` |
| `PUT` | `/ws/addresses/{id}` | JWT | mesmo body do POST (só atualiza se for do usuário) |
| `DELETE` | `/ws/addresses/{id}` | JWT | remove se for do usuário |

> As rotas são registradas explicitamente em `GatewayConfig.java` (lista manual, não auto-discovery).

---

## 🗄️ Banco de dados & migrations

- `spring.jpa.hibernate.ddl-auto=none` + **Flyway**: o schema muda **somente via migrations** em `src/main/resources/db_migration/V*.sql`. Não alterar tabela por entidade.
- Regras de negócio sensíveis ficam **no servidor** (ex.: `order-service` recalcula o preço via Feign — `priceAtPurchase`; permissão de admin em `/ws/products`).

### Principais migrations
| Serviço | Migration | O que faz |
|---|---|---|
| product | `V4__AddCategoryAndSeller.sql` | colunas `category`, `seller_id`, `seller_name` |
| product | `V5__CreateTableReview.sql` | tabela `tb_review` (1 avaliação por usuário, nota 1–5) |
| product | `V6__ReplaceWithAutoParts.sql` | seed de **32 peças automotivas** (BRL/USD) |
| order | `V2__AddDeliveryAddress.sql` | `delivery_address` (snapshot do endereço na compra) |
| auth | `V3__CreateTableTbAddress.sql` | tabela `tb_address` (endereços por usuário) |

---

## 🐛 Troubleshooting

| Sintoma | Causa / solução |
|---|---|
| `503` ou sem conversão de moeda logo após subir | Aguarde todos re-registrarem no Eureka (`http://localhost:8761`) |
| Histórico de pedidos (`GET /ws/orders`) quebra | `currency-service` fora do ar (esse endpoint **não tem fallback**) |
| Pedido antigo quebra o histórico | Produto referenciado foi removido. Limpe órfãos: `DELETE FROM tb_order_item; DELETE FROM tb_order;` |
| Migration nova não aplica no rebuild | Cache do Docker (Windows). Force: `docker compose build --no-cache <serviço>` + `up -d --force-recreate <serviço>` |
| Resetar tudo (recria seeds) | `docker compose down -v` |

---

## 📌 Melhorias futuras

- [ ] Fallback de moeda no `order-service` (histórico)
- [ ] Estoque / baixa de estoque ao fechar pedido
- [ ] Paginação real no catálogo

---

<div align="center">
<sub>Trabalho de faculdade · AutoParts ⚙️</sub>
</div>
