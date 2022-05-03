# Trabalho de Qualidade e Teste - Fórum Multidisciplinar

## Projeto
O Projeto tem como objetivo implementar uma API de um Fórum para dúvidas e discussões de disciplinas para alunos. Um usário pode criar um Tópico e outros usuários podem interagir respondendo o tópico criado.

## Regras de Negócio
 - RF1: Deve ser possível criar um Tópico
    - RF 1.1: O Tópico deve possuir obrigatoriamente um nome, uma descrição, um curso associado, um Autor, um status e a data de publicação.
    - RF 1.2: Cada tópico deve ser associado a um único Autor e Curso.
    - RF 1.3: Um tópico criado deve possuir Status "Não respondido", por padrão.
    - RF 1.4: Status deve receber um dos atributos: Não Respondido, Respondido, Solucionado e Fechado.

 - RF2: Deve ser possível responder um Tópico
    - RF 2.1: A Resposta deve possuir obrigatoriamente uma descrição, um Autor, um Tópico associado e a data de publicação.
    - RF 2.2: Não deve ser possível responder um tópico com status Fechado.

- RF3: Deve ser possível realizar operações CRUD para Cursos
    - RF 3.1: O Curso deve possuir obrigatoriamente um nome
