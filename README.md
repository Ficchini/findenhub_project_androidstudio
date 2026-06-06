# EventMarket

**Marketplace de eventos acadêmico — Android Studio + Java + Firebase**

MVP desenvolvido como projeto acadêmico conectando clientes que organizam eventos com fornecedores de serviços.

---

## Sobre o projeto

EventMarket é um aplicativo Android nativo que funciona como marketplace entre dois perfis de usuário:

- **Cliente** — cria eventos, explora fornecedores e envia propostas
- **Fornecedor** — cadastra serviços, recebe e responde solicitações

---

## Stack tecnológica

| Camada | Tecnologia |
|---|---|
| Linguagem | Java |
| Interface | XML (sem Jetpack Compose) |
| Autenticação | Firebase Authentication (email/senha + Google Sign-In) |
| Banco de dados | Cloud Firestore |
| Arquitetura | MVVM simplificado + Repository Pattern |
| Navegação | Activities + Fragments + BottomNavigationView |
| UI | Material Design 3 |

---

## Pré-requisitos

- Android Studio Hedgehog (2023.1.1) ou superior
- JDK 8+
- Conta no Firebase (gratuita)
- Android SDK 34, minSdk 24 (Android 7.0)

---

## Configuração do Firebase

### 1. Criar projeto no Firebase Console

1. Acesse [console.firebase.google.com](https://console.firebase.google.com)
2. Clique em **Adicionar projeto**
3. Dê o nome `EventMarket` e siga os passos

### 2. Registrar o app Android

1. No painel do projeto, clique em **Adicionar app → Android**
2. Package name: `br.com.eventmarketplace`
3. Clique em **Registrar app**
4. Baixe o arquivo `google-services.json`
5. Cole o arquivo em `app/google-services.json`

### 3. Ativar Authentication

1. No Console: **Authentication → Método de login**
2. Ative **E-mail/senha**
3. Ative **Google**
4. Copie o **Web Client ID** e cole em `res/values/strings.xml`:

```xml
<string name="default_web_client_id">SEU_WEB_CLIENT_ID_AQUI</string>
```

### 4. Criar o banco Firestore

1. No Console: **Firestore Database → Criar banco de dados**
2. Selecione **Modo de teste** (para desenvolvimento)
3. Escolha a região mais próxima (ex: `southamerica-east1`)

### 5. Aplicar as regras de segurança

No Console: **Firestore → Regras**, cole o conteúdo do arquivo `firestore.rules` deste repositório.

### 6. Criar os índices compostos

Na primeira execução de cada query o Logcat exibirá um link como:

```
FAILED_PRECONDITION: The query requires an index. You can create it here: https://console.firebase.google.com/...
```

Clique no link → **Criar índice** → aguarde ~1 minuto. Índices necessários:

| Coleção | Campo 1 | Campo 2 | Ordem |
|---|---|---|---|
| `services` | `supplierId` (ASC) | `createdAt` (DESC) | — |
| `events` | `clientId` (ASC) | `createdAt` (DESC) | — |
| `requests` | `supplierId` (ASC) | `createdAt` (DESC) | — |

---

## Como rodar o projeto

```bash
# 1. Clone o repositório
git clone https://github.com/seu-usuario/eventmarket.git

# 2. Abra no Android Studio
# File → Open → selecione a pasta clonada

# 3. Certifique-se de que o google-services.json está em app/

# 4. Sincronize as dependências
# File → Sync Project with Gradle Files

# 5. Execute
# Run → Run 'app' (emulador ou dispositivo físico)
```

---

## Estrutura do projeto

```
app/src/main/
├── java/br/com/eventmarketplace/
│   ├── application/
│   │   └── App.java                        # Inicialização do Firebase
│   ├── data/
│   │   ├── callback/
│   │   │   └── FirestoreCallback.java      # Interface genérica de callback
│   │   ├── model/
│   │   │   ├── User.java
│   │   │   ├── Event.java
│   │   │   ├── Service.java
│   │   │   ├── Request.java
│   │   │   └── Favorite.java
│   │   ├── remote/
│   │   │   ├── FirebaseAuthManager.java    # Encapsula Firebase Auth
│   │   │   ├── FirebaseCollections.java    # Nomes das coleções
│   │   │   └── FirestoreManager.java       # Encapsula Firestore CRUD
│   │   └── repository/
│   │       ├── AuthRepository.java
│   │       ├── UserRepository.java
│   │       ├── EventRepository.java
│   │       ├── ServiceRepository.java
│   │       ├── RequestRepository.java
│   │       └── FavoriteRepository.java
│   ├── ui/
│   │   ├── auth/
│   │   │   ├── AuthViewModel.java
│   │   │   ├── GoogleSignInHelper.java
│   │   │   ├── LoginActivity.java
│   │   │   ├── RegisterChoiceActivity.java
│   │   │   ├── RegisterClientActivity.java
│   │   │   └── RegisterSupplierActivity.java
│   │   ├── splash/
│   │   │   └── SplashActivity.java
│   │   ├── onboarding/
│   │   │   └── WelcomeActivity.java
│   │   ├── main/
│   │   │   ├── MainActivity.java           # Roteador por userType
│   │   │   └── MainViewModel.java
│   │   ├── client/
│   │   │   ├── dashboard/ClientDashboardFragment.java
│   │   │   ├── events/
│   │   │   │   ├── ClientEventsFragment.java
│   │   │   │   ├── CreateEventActivity.java
│   │   │   │   ├── EditEventActivity.java
│   │   │   │   └── EventDetailsActivity.java
│   │   │   ├── suppliers/
│   │   │   │   ├── SupplierListFragment.java
│   │   │   │   └── SupplierDetailsActivity.java
│   │   │   └── profile/ClientProfileFragment.java
│   │   ├── supplier/
│   │   │   ├── dashboard/SupplierDashboardFragment.java
│   │   │   ├── services/
│   │   │   │   ├── SupplierServicesFragment.java
│   │   │   │   ├── CreateServiceActivity.java
│   │   │   │   └── EditServiceActivity.java
│   │   │   ├── requests/SupplierRequestsFragment.java
│   │   │   └── profile/SupplierProfileFragment.java
│   │   └── common/
│   │       ├── adapters/
│   │       │   ├── EventAdapter.java
│   │       │   ├── ServiceAdapter.java
│   │       │   ├── SupplierAdapter.java
│   │       │   └── RequestAdapter.java
│   │       └── components/
│   │           └── StateView.java          # Componente loading/empty/error
│   └── utils/
│       ├── Constants.java
│       ├── SessionManager.java
│       └── Validators.java
└── res/
    ├── layout/                             # 22 arquivos XML de tela
    ├── menu/                               # BottomNav cliente e fornecedor
    └── values/
        ├── colors.xml
        ├── strings.xml
        ├── themes.xml
        └── dimens.xml
```

---

## Modelo de dados (Firestore)

### `users/{uid}`
```
id          : String
name        : String
email       : String
cpf         : String
userType    : "CLIENT" | "SUPPLIER"
photoUrl    : String
phone       : String
category    : String   // apenas SUPPLIER
createdAt   : Timestamp
updatedAt   : Timestamp
```

### `events/{eventId}`
```
id          : String
clientId    : String
title       : String
category    : String
date        : String
location    : String
budget      : String
description : String
status      : "OPEN" | "CLOSED"
createdAt   : Timestamp
```

### `services/{serviceId}`
```
id          : String
supplierId  : String
title       : String
category    : String
description : String
priceBase   : String
city        : String
active      : Boolean
createdAt   : Timestamp
```

### `requests/{requestId}`
```
id          : String
eventId     : String
clientId    : String
supplierId  : String
serviceId   : String
message     : String
status      : "PENDING" | "ACCEPTED" | "REJECTED"
createdAt   : Timestamp
```

---

## Fluxo de navegação

```
SplashActivity
    │
    ├── usuário logado ──────────────────→ MainActivity
    │                                          │
    └── sem sessão → WelcomeActivity           ├── CLIENT → BottomNav Cliente
            │                                  │     ├── ClientDashboardFragment
            ├── Entrar → LoginActivity         │     ├── ClientEventsFragment
            │               │                 │     ├── SupplierListFragment
            │               └── Google         │     └── ClientProfileFragment
            │                                  │
            └── Criar conta                    └── SUPPLIER → BottomNav Fornecedor
                    │                                ├── SupplierDashboardFragment
                    └── RegisterChoiceActivity        ├── SupplierServicesFragment
                            │                         ├── SupplierRequestsFragment
                            ├── CLIENT                └── SupplierProfileFragment
                            │   └── RegisterClientActivity
                            └── SUPPLIER
                                └── RegisterSupplierActivity
```

---

## Fluxo principal de marketplace

```
1. Cliente cria evento (CreateEventActivity)
2. Cliente explora serviços (SupplierListFragment)
3. Cliente abre detalhes de um serviço (SupplierDetailsActivity)
4. Cliente seleciona o evento e envia proposta
5. Proposta salva em requests/{id} com status PENDING
6. Fornecedor vê solicitação (SupplierRequestsFragment)
7. Fornecedor aceita → status ACCEPTED
   Fornecedor recusa → status REJECTED
```

---

## Bugs conhecidos corrigidos durante o desenvolvimento

| Bug | Causa | Correção aplicada |
|---|---|---|
| Lista de serviços vazia após salvar | Query com 3 condições exigia índice composto não criado | Simplificação da query em `ServiceRepository` |
| `id` nulo após deserialização do Firestore | Campo `id` sem `@DocumentId` nos models | Adicionado `@DocumentId` em todos os models |
| Erro de carregamento sem feedback visível | `onFailure` usava apenas Toast | Adicionado `tvError` persistente e `Log.e` no Fragment |
| Campo de preço bloqueando vírgula | `inputType="number"` em vez de `numberDecimal` | Corrigido no layout |
| Proposta enviada sem evento associado | `eventId` sempre vazio | Dialog de seleção de evento adicionado |
| Logout Google não limpava sessão completamente | `GoogleSignInClient` não era instanciado no logout | `SessionManager.clearAll()` chama `googleSignInClient.signOut()` |

---

## Checklist de testes manuais

- [ ] Cadastro como Cliente (email/senha e Google)
- [ ] Cadastro como Fornecedor (email/senha e Google)
- [ ] Login com credenciais válidas e inválidas
- [ ] Persistência de sessão (fechar e reabrir o app)
- [ ] Criar evento como cliente
- [ ] Editar e excluir evento
- [ ] Criar serviço como fornecedor
- [ ] Editar e excluir serviço
- [ ] Buscar/filtrar fornecedores por título, categoria e cidade
- [ ] Enviar proposta associando um evento
- [ ] Aceitar e recusar proposta como fornecedor
- [ ] Logout com limpeza total de sessão Google

---

## Dependências principais

```groovy
// Firebase
implementation platform('com.google.firebase:firebase-bom:32.7.4')
implementation 'com.google.firebase:firebase-auth'
implementation 'com.google.firebase:firebase-firestore'

// Google Sign-In
implementation 'com.google.android.gms:play-services-auth:21.0.0'

// Material Design 3
implementation 'com.google.android.material:material:1.11.0'

// Lifecycle + ViewModel + LiveData
implementation 'androidx.lifecycle:lifecycle-viewmodel:2.7.0'
implementation 'androidx.lifecycle:lifecycle-livedata:2.7.0'

// Glide (imagens)
implementation 'com.github.bumptech.glide:glide:4.16.0'
```

---

## Licença

Projeto acadêmico. Uso livre para fins educacionais.
