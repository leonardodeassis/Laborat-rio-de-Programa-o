# Formulário de Cadastro de Pessoa - JavaFX

Projeto desenvolvido de acordo com os requisitos do trabalho.

## Requisitos
- Apache NetBeans
- JDK 26
- Maven (o NetBeans já possui suporte integrado)
- Internet na primeira execução para baixar as dependências JavaFX

## Como abrir no Apache NetBeans

1. Extraia o arquivo ZIP.
2. Abra o Apache NetBeans.
3. Vá em `File > Open Project`.
4. Selecione a pasta `FormularioCadastroJavaFX`.
5. Aguarde o Maven baixar as dependências.
6. Clique com o botão direito no projeto e escolha `Run`.

## Estrutura

- `FormularioCadastro.java`: classe principal que estende `Application`, cria `Stage` e `Scene`.
- `FormularioCadastroController.java`: tratamento dos componentes e evento do botão usando lambda.
- `FormularioCadastro.fxml`: interface construída com `GridPane`.
- `estilo.css`: estilização da interface.
- `pom.xml`: configuração do Maven e dependências JavaFX.

## Execução pelo terminal

Dentro da pasta do projeto:

`mvn javafx:run`

O projeto está configurado para JDK 26 e JavaFX 27.
