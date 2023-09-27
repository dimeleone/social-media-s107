# Social Media S107

## Description

Esse é um projeto de uma rede social desenvolvido para a disciplina S07 com o intuito de exectar uma pipeline CI/CD usando GitHub Actions.

## Sobre o projeto

O projto foi desenvolvido com o framework [Spring Boot](https://spring.io/projects/spring-boot).

Endpoints:

- GET /api/user/{id} (retorna um usuário pelo id)
- POST /api/user (cria um usuário)
- GET /api/user/{id}/posts (retorna os posts de um usuário)
- GET /api/post/{id} (retorna um post pelo id)
- POST /api/post (cria um post)
- GET /api/post (retorna todos os posts)
- GET /api/post/{id}/comment (retorna os comentários de um post)
- POST /api/post/{id}/comment (cria um comentário em um post)

## Sobre a pipeline CI/CD

A pipeline CI/CD foi desenvolvida usando o GitHub Actions e está dividida em 3 jobs:

- Test: executa os testes da aplicação e armazena o relatório de testes no GitHub Actions
- Package: empacota a aplicação em um arquivo .jar e armazena o arquivo no GitHub Actions
- Notification: Envia um email dizendo que a pipeline foi executada com sucesso