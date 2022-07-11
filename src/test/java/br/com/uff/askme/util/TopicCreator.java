package br.com.uff.askme.util;

import br.com.uff.askme.model.*;

import java.time.LocalDateTime;
import java.util.Arrays;

public class TopicCreator {

    public static Topic topicDataStructureWithTwoAnswer() {
        return Topic.builder()
                .topicId(1L)
                .author(createAuthorLaion())
                .title("Usar algoritmo de ordenação")
                .message("Não estou conseguindo implementar nenhum algoritmo de ordenação")
                .creationDate(LocalDateTime.now())
                .status(TopicStatus.RESPONDIDO)
                .course(createCourseDataStructure())
                .answers(Arrays.asList(answerDataStructureBase1(), answerDataStructureBase2()))
                .build();
    }

    public static Topic topicDataBaseWithoutAnswer() {
        return Topic.builder()
                .topicId(2L)
                .author(createAuthorNilson())
                .title("Alterar atributo da tabela")
                .message("Preciso alterar o campo cpf para string")
                .creationDate(LocalDateTime.now())
                .status(TopicStatus.NAO_RESPONDIDO)
                .course(createCourseDataBase())
                .build();
    }

    private static Answer answerDataStructureBase1() {
        return Answer.builder()
                .answerId(1L)
                .author(createAuthorPaula())
                .message("Tenta usar o busca binária")
                .creationDate(LocalDateTime.now())
                .build();
    }

    private static Answer answerDataStructureBase2() {
        return Answer.builder()
                .answerId(1L)
                .author(createAuthorEdna())
                .message("Tenta usar o bubble sort")
                .creationDate(LocalDateTime.now())
                .build();
    }

    private static Course createCourseDataStructure() {
        return Course.builder()
                .id(1L)
                .name("Estrutura de Dados I")
                .category("Prog")
                .build();
    }

    private static Course createCourseDataBase() {
        return Course.builder()
                .id(1L)
                .name("Banco de Dados I")
                .category("Dados")
                .build();
    }

    private static User createAuthorLaion() {
        return User.builder()
                .userId(1L)
                .name("Laion")
                .email("laion@email.com")
                .build();
    }

    private static User createAuthorPaula() {
        return User.builder()
                .userId(2L)
                .name("Paula")
                .email("paula@email.com")
                .build();
    }

    private static User createAuthorEdna() {
        return User.builder()
                .userId(3L)
                .name("Edna")
                .email("edna@email.com")
                .build();
    }

    private static User createAuthorNilson() {
        return User.builder()
                .userId(4L)
                .name("Nilson")
                .email("nilson@email.com")
                .build();
    }

}
