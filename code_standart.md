# Code Standart

| Pontos       | Descrição                                                                                                                                                                  |
|--------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Situação     | Grupo de programadores desenvolvendo codigos em conjunto, mas cada um com a sua particularidade na hora dedesenvolver, arquitetar, organizar e disponibilizar o seu codigo |
| Problematica | Diferentes padrões de codigo em um mesmo projeto, dificultando a padronização, organização, auxilio nas duvidas, resolução de problemas no codigo fonte                    |
| Solução      | Desenvolvimentod de uma `guideline` com os padrões que serão seguidos para orientar e guiar os desenvolvedors                                                              |

### Code Guideline

- Seguir a estrutura dos packages já existentes
    * Os nome dos `packages` devem ter apenas letras minusculas, sem acentuação ou numero
- O codigo será baseado na **Arquitetura MVC (Model-View-Controller)**
    * O Fluxo padrão será: `Controller` --> `Service` --> `View` --> `Controller` (Opcional)
- Nomenclaturas importantes
    * `DTO` (Data Transfer Object): Objetos simples para fazer a transferencia dos dados sem a lógica de negócio (
      contas, validações, etc)
    * `Model`: Camada de Persistencia dos Dados (Banco de Dados)
    * `View`: Classes que serão retornadas para a visualização (Retorno do Controller)
- Não criar blocos de tratamento de exceção (`try/catch`) vazios, sempre imprimir a exception usando `logger.error("
  Exception: ",e)`
- Dividir metodos grandes em metodos menores para facilitar a leitura e entendimento da lógica/codigo
- Formatar e organizar os imports (Inteliij)
    * `Ctrl+Alt+Shift+L`: Deixar marcado `Whole File`, `Optimize Imports` --> Run

### Commits e Codigo no Repositorio

- É recomendavel utilizar a aba `Commit` presente no menu lateral esquerdo do Intellij
    * Maior organização e facilidade ao escolher os arquivos do `commit`
    * Selecionar dentro de cada arquivo o que irá no `commit`
    * Visualizar facilmente as alterações no codigo
- Commits sem Warming (Intellij)
    * Na aba lateral `Commit`: Clicar no icone de Configuraççoes --> Marcar `Analyze Code`, `Check TODO`, `Cleanup Code`, 
      `Optimize Imports` e `Reformat Code`
    * Antes de enviar o `commit` ao GitHub, verificar se existe algum warming e resolver
- Mensagens/Titulis **não GENERICOS** com até 60 letras, descrevendo resumidamente o que foi feito
