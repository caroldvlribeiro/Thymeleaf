# Cadastro de Produtos — Spring Boot + Thymeleaf

Aplicação web para cadastrar, listar, buscar e remover produtos, desenvolvida em Java com Spring Boot e Thymeleaf (Atividade 08 – FATEC Praia Grande, DSM).

## Funcionalidades

- Formulário de cadastro: nome, preço, categoria (select), estoque e descrição
- Validação no servidor (preço e estoque não podem ser negativos; nome e categoria obrigatórios)
- Listagem em tabela com total de produtos
- Busca por nome ou categoria
- Remoção de produto
- Persistência em banco H2 embarcado (Spring Data JPA)

## Tecnologias

Java 17 · Spring Boot 3 · Spring Web · Thymeleaf · Spring Data JPA · H2 · Bean Validation · Bootstrap 4

## Arquitetura (MVC)

| Camada | Classe / Arquivo | Papel |
|---|---|---|
| Model | `Produto` | Entidade JPA com os dados do produto |
| View | `cadastrar.html`, `lista.html` | Templates Thymeleaf |
| Controller | `ProdutoController` | Recebe requisições HTTP e escolhe a View |
| Service | `ProdutoService` | Regras de negócio, intermedia Controller e Repository |
| Repository | `ProdutoRepository` | Acesso ao banco via Spring Data JPA |

## Como executar

```bash
./mvnw spring-boot:run        # Linux/Mac
mvnw.cmd spring-boot:run      # Windows
```

Acesse `http://localhost:8080`. O console do banco fica em `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:produtosdb`, user `sa`, senha vazia).

> O banco é em memória: os dados são perdidos ao reiniciar a aplicação.

## Rotas

| Método | URL | Ação |
|---|---|---|
| GET | `/` | Redireciona para `/cadastrar` |
| GET | `/cadastrar` | Exibe o formulário vazio |
| POST | `/cadastrar` | Valida e salva o produto, redireciona para `/lista` |
| GET | `/lista?busca=` | Lista produtos (com filtro opcional) |
| POST | `/remover/{id}` | Remove o produto e redireciona para `/lista` |

## Como a página Thymeleaf funciona

O Thymeleaf é um motor de templates que roda **no servidor**. O HTML é processado antes de ser enviado: as diretivas `th:*` são resolvidas com os dados do Model e o navegador recebe HTML puro já preenchido.

### Como recupera dados (do servidor para a página)

1. O navegador faz `GET /lista`.
2. O `ProdutoController` chama `ProdutoService.listar()`, que consulta o banco via `ProdutoRepository`.
3. O Controller coloca o resultado no `Model`:
   ```java
   model.addAttribute("produtos", service.listar(busca));
   return "lista";
   ```
4. O Spring renderiza `lista.html`. O Thymeleaf lê a variável `produtos` com `${produtos}` e gera o HTML:
   - `th:each="p : ${produtos}"` repete a linha `<tr>` para cada produto
   - `th:text="${p.nome}"` chama `getNome()` e insere o valor na tag
   - `th:if` / `th:unless` mostram ou escondem blocos conforme a condição
5. O HTML final é devolvido ao navegador.

### Como insere dados (da página para o servidor)

1. Em `GET /cadastrar`, o Controller envia um `new Produto()` vazio ao Model.
2. No formulário, `th:object="${produto}"` vincula o form a esse objeto e `th:field="*{nome}"` gera `id`, `name` e `value` de cada campo.
3. Ao enviar, o navegador faz `POST /cadastrar` com os campos do formulário.
4. `@ModelAttribute Produto produto` faz o Spring criar o objeto e preencher os atributos pelos setters.
5. `@Valid` + `BindingResult` verificam as regras de validação:
   - se houver erro, a página `cadastrar` é renderizada de novo e `th:errors` mostra as mensagens
   - se estiver tudo certo, `service.salvar(produto)` grava no banco
6. O Controller responde `redirect:/lista` (padrão **Post/Redirect/Get**), evitando reenvio do formulário ao recarregar a página.

### Diretivas utilizadas

| Diretiva | Função |
|---|---|
| `th:action` | URL de destino do formulário |
| `th:object` | Vincula o formulário a um objeto do Model |
| `th:field` | Gera `id`, `name` e `value` do input |
| `th:text` | Substitui o conteúdo da tag pelo valor da expressão |
| `th:each` | Repete o elemento para cada item da lista |
| `th:if` / `th:unless` | Exibe o elemento conforme a condição |
| `th:errors` | Mostra mensagens de validação do campo |
| `th:value` | Define o atributo `value` |
| `@{/rota}` | Gera URLs relativas à raiz da aplicação |

## Exercícios implementados

- **Nível 1:** campos `estoque` e `descricao`, CSS básico, total de produtos abaixo da tabela
- **Nível 2:** categoria em `select`, validação de preço negativo com mensagem de erro, coluna "Ações" com botão Remover
- **Nível 3:** `@Service` separado do Controller, busca por nome/categoria, banco H2 com Spring Data JPA
