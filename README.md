# Aeterna Biz — Gestão de Categorias de Resíduos e Rastreabilidade

Aplicação desktop em Java (JDBC + Swing) integrada ao **PostgreSQL** com extensão **PostGIS**, desenvolvida para o módulo **Aeterna Biz** (logística reversa, conformidade ambiental e economia circular).

## Arquitetura e Tecnologias

* **Linguagem e Runtime:** Java (JDK 25)
* **Acesso a Dados:** JDBC (`Connection`, `PreparedStatement`, `ResultSet`) com padrão DAO e `ConexaoFactory`
* **Interface Gráfica:** Java Swing (`AeternaTela`) com validação de campos, tabela de listagem e tradução de erros SQL (`SQLException`)
* **Banco de Dados:** PostgreSQL com extensão `postgis` (`geography(Point, 4326)`), tipo `ENUM` (`StatusConformidade`) e chaves primárias/estrangeiras em `BIGSERIAL` / `BIGINT`

## Estrutura do Projeto

* `sql/schema_aeterna_biz.sql` — Script DDL para criação das tabelas, restrições (`UNIQUE`, `CHECK`, `FK`), tipo `StatusConformidade` e índices (`BTREE` e `GIST`).
* `sql/seed_aeterna_biz.sql` — Script DML de carga inicial de dados para testes e demonstração.
* `src/main/java/br/com/aeterna/conexao/ConexaoFactory.java` — Gerenciamento da conexão com o PostgreSQL.
* `src/main/java/br/com/aeterna/modelo/` — Classes de domínio/modelo (ex.: `CategoriaResiduo`).
* `src/main/java/br/com/aeterna/dao/` — Camada de persistência com operações CRUD (ex.: `CategoriaResiduoDAO`).
* `src/main/java/br/com/aeterna/app/` — Ponto de entrada (`Main`) e interface gráfica (`AeternaTela`).

## Como Executar

1. **Preparar o banco de dados:**
   * Crie o banco no PostgreSQL e habilite a extensão `postgis`.
   * Execute `sql/schema_aeterna_biz.sql`.
   * Execute `sql/seed_aeterna_biz.sql` para popular os dados iniciais.
2. **Configurar a conexão:**
   * Verifique host, porta, nome do banco, usuário e senha em `ConexaoFactory.java`.
3. **Executar a aplicação:**
   * Execute `br.com.aeterna.app.AeternaTela` (interface CRUD) ou `br.com.aeterna.app.Main` (demonstração via console).

## Funcionalidades Implementadas (CRUD)

* **Cadastrar (`INSERT`):** Valida campos obrigatórios e limites de tamanho, insere no PostgreSQL e retorna o `ID` sequencial gerado (`BIGSERIAL`).
* **Listar (`SELECT`):** Carrega os registros ordenados por `id` na tabela da interface.
* **Atualizar (`UPDATE`):** Permite selecionar um registro, editar os dados e atualizar `atualizado_em`.
* **Excluir (`DELETE`):** Solicita confirmação antes de remover o registro pelo `ID`.
* **Tratamento de Exceções:** Exibe mensagens compreensíveis para violação de unicidade (`23505`), vínculo com registros dependentes (`23503`) e falhas de conexão/autenticação.
