# Sistema de Agendamento de Serviços

## Sobre o projeto

O **Sistema de Agendamento de Serviços** é uma aplicação desktop desenvolvida em Java, com o objetivo de facilitar o gerenciamento de clientes, profissionais, serviços e agendamentos de um estabelecimento.

O projeto surgiu da necessidade de organizar os horários de atendimento, centralizar as informações dos clientes e profissionais e facilitar o controle dos serviços oferecidos.

A aplicação permite realizar operações de cadastro, consulta, edição e exclusão, além de relacionar diferentes entidades por meio de um sistema de agendamento.

## Objetivo

Desenvolver uma aplicação desktop que permita gerenciar os agendamentos de um estabelecimento de maneira organizada, utilizando conceitos de orientação a objetos, JavaFX, persistência de dados com JPA/Hibernate e banco de dados SQLite.

## Integrantes

- Emily Schmitt Haas
- [Nome do segundo integrante, se houver]

## Funcionalidades

O sistema possui as seguintes funcionalidades:

- **Painel inicial:** apresenta um resumo das informações cadastradas e dos próximos agendamentos.
- **Gerenciamento de clientes:** permite cadastrar, consultar, editar e excluir clientes.
- **Gerenciamento de profissionais:** permite cadastrar, consultar, editar e excluir profissionais, incluindo especialidade e situação.
- **Gerenciamento de serviços:** permite cadastrar, consultar, editar e excluir serviços, com informações de preço, descrição e duração.
- **Gerenciamento de agendamentos:** permite criar, consultar, editar e excluir agendamentos, relacionando clientes, profissionais e serviços.
- **Validação de dados:** verifica informações preenchidas e evita inconsistências nos cadastros e agendamentos.
- **Pesquisa:** permite localizar registros cadastrados.

## Tecnologias utilizadas

- Java 11
- JavaFX 13
- FXML
- Scene Builder
- Maven
- JPA
- Hibernate 6
- SQLite
- Git e GitHub
- NetBeans IDE

## Estrutura do projeto

O projeto está organizado em pacotes para separar as responsabilidades:

```text
AgendamentoServico/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── ifrs/ibiruba/agendamentoservico/
│       │       ├── model/
│       │       ├── repository/
│       │       ├── controller/
│       │       ├── persistencia/
│       │       └── App.java
│       │
│       └── resources/
│           ├── META-INF/
│           │   └── persistence.xml
│           │
│           └── ifrs/ibiruba/agendamentoservico/
│               ├── telaPrincipal.fxml
│               ├── telaClientes.fxml
│               ├── telaProfissionais.fxml
│               ├── telaServicos.fxml
│               └── telaAgendamentos.fxml
│
├── docs/
│   ├── diagrama-classes.png
│   └── imagens/
│
├── pom.xml
├── .gitignore
└── README.md
```

## Modelagem e banco de dados

O sistema utiliza SQLite para armazenar os dados e JPA/Hibernate para realizar a persistência das informações.

O projeto possui quatro entidades principais:

- **Cliente:** armazena nome, telefone e e-mail.
- **Profissional:** armazena nome, especialidade, telefone e situação.
- **Serviço:** armazena nome, descrição, preço, duração e situação.
- **Agendamento:** armazena data, horário e situação do agendamento.

A entidade Agendamento possui relacionamentos `@ManyToOne` com Cliente, Profissional e Serviço. Dessa forma, cada agendamento está associado a um cliente, um profissional e um serviço.

A persistência é realizada por meio de repositórios, utilizando EntityManager e transações JPA.

## Diagrama de classes

O diagrama representa as principais classes do sistema, seus atributos e os relacionamentos entre as entidades.

![Diagrama de classes](docs/diagrama-classes.png)

## Interface do sistema

As telas possuem uma identidade visual padronizada, com tons de cinza, buscando proporcionar uma interface simples e organizada.

### Tela inicial

![Tela inicial](docs/imagens/tela-inicial.png)

### Cadastro de clientes

![Tela de clientes](docs/imagens/tela-clientes.png)

### Cadastro de profissionais

![Tela de profissionais](docs/imagens/tela-profissionais.png)

### Cadastro de serviços

![Tela de serviços](docs/imagens/tela-servicos.png)

### Gerenciamento de agendamentos

![Tela de agendamentos](docs/imagens/tela-agendamentos.png)

## Como executar o projeto

### Pré-requisitos

- Java JDK 11 instalado.
- NetBeans IDE.
- Maven.
- Git (opcional, para clonar o repositório).

### Executando pelo NetBeans

1. Clone ou baixe o repositório do projeto.
2. Abra o NetBeans.
3. Selecione **File > Open Project**.
4. Localize a pasta `AgendamentoServico`.
5. Aguarde o Maven baixar as dependências necessárias.
6. Execute o projeto pela classe principal `App.java`.

### Banco de dados

O banco de dados utilizado é o SQLite.

O arquivo `agendamento.db` é criado automaticamente na execução da aplicação, caso ainda não exista. A configuração da persistência está localizada em:

`src/main/resources/META-INF/persistence.xml`

O Hibernate é responsável pelo mapeamento das entidades e pela criação e atualização da estrutura das tabelas.

## Conceitos aplicados

Durante o desenvolvimento foram utilizados conceitos estudados na disciplina:

- Programação orientada a objetos.
- JavaFX e FXML.
- Separação entre Model, Repository e Controller.
- Coleções observáveis (`ObservableList`).
- Componentes de seleção e visualização, como ComboBox e TableView.
- Bindings e listeners.
- Persistência com JPA/Hibernate.
- Relacionamentos entre entidades.
- Transações e tratamento de exceções.
- Expressões lambda e Stream API.
- Controle de versão com Git e GitHub.

## Apresentação do projeto

Vídeo de demonstração da aplicação:

[Link do vídeo no YouTube](INSERIR_LINK_DO_VIDEO)

## Repositório

[GitHub - Agendamento de Serviços](INSERIR_LINK_DO_REPOSITORIO)

