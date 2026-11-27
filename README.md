# Urna Cidadã

Simulador educativo de urna eletrônica desenvolvido em Java para o projeto de extensão do curso de Ciência da Computação.

## Sobre o projeto

A aplicação simula uma votação sequencial para dois cargos:

- Vereador.
- Prefeito.

O sistema possui interface gráfica Swing, teclado numérico, voto válido, voto branco, voto nulo, correção da digitação, exibição dos dados dos candidatos, imagens e som de confirmação.

O projeto também possui classes separadas para modelos, regras de votação, persistência e interface, aplicando conceitos de Programação Orientada a Objetos.

## Tecnologias

- Java 17 ou superior.
- Java Swing.
- Maven.
- JUnit 5.
- Firebase Cloud Firestore, quando configurado.

## Como executar

É necessário ter Java 17 ou superior e Maven instalados.

Para executar os testes:

```bash
mvn clean test
```

Para abrir a interface gráfica:

```bash
mvn exec:java
```

Se o Firebase não estiver configurado, o programa utiliza o repositório local durante a execução.

## Configuração opcional do Firebase

O código utiliza a variável de ambiente `GOOGLE_APPLICATION_CREDENTIALS` para localizar o arquivo JSON da conta de serviço do Firebase.

Windows CMD:

```cmd
set GOOGLE_APPLICATION_CREDENTIALS=C:\caminho\firebase-service-account.json
mvn exec:java
```

Linux/macOS:

```bash
export GOOGLE_APPLICATION_CREDENTIALS=/caminho/firebase-service-account.json
mvn exec:java
```

O arquivo JSON de credenciais não deve ser colocado no GitHub. O `.gitignore` já possui regras para ignorar arquivos de conta de serviço.

## Organização das pastas

```text
src/main/java/br/edu/urnacidada/
├── app/          interface gráfica e inicialização
├── excecao/      exceções do sistema
├── modelo/       candidatos e votos
├── repositorio/  persistência local e Firebase
└── servico/      regras da votação

src/main/resources/
├── imagens/      imagens dos candidatos
└── sons/         som de confirmação

src/test/         testes automatizados
```

## Observação

Este é um simulador educativo. Os candidatos e partidos utilizados são fictícios e não representam uma eleição oficial. Os materiais dos Desafios 1, 2 e 3 são entregues separadamente conforme a orientação da disciplina.
