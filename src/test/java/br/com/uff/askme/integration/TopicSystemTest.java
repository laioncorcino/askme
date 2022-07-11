package br.com.uff.askme.integration;

import br.com.uff.askme.dto.TopicRequest;
import br.com.uff.askme.dto.TopicResponse;
import br.com.uff.askme.dto.UpdateTopicRequest;
import br.com.uff.askme.repository.TopicRepository;
import br.com.uff.askme.wrapper.PageableResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TopicSystemTest extends SystemTest {

    @Autowired
    private TopicRepository topicRepository;

    @AfterEach
    public void cleanerDatabase() {
        topicRepository.deleteAll();
    }

    @Test
    @DisplayName("should_return_paginated_list_of_topics")
    public void listTopicPaginated() {
        ResponseEntity<PageableResponse<TopicResponse>> getResponse = doGetPage("/");

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        PageableResponse<TopicResponse> pageTopics = getResponse.getBody();

        assertThat(pageTopics).isNotNull();
        assertThat(pageTopics.getNumberOfElements()).isEqualTo(2);
        assertThat(pageTopics.getContent().size()).isEqualTo(2);
        assertThat(pageTopics.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(pageTopics.getPageable().getPageSize()).isEqualTo(10);

        List<TopicResponse> content = pageTopics.getContent();

        assertTrue(content.stream().anyMatch(topic -> topic.getTitle().equals("Levantamento de requisitos")));
        assertTrue(content.stream().anyMatch(topic -> topic.getTitle().equals("Modelo lógico")));
    }

    @Test
    @DisplayName("should_return_paginated_list_of_topics_find_by_course")
    public void listTopicByCourse() {
        ResponseEntity<PageableResponse<TopicResponse>> getResponse = doGetPage("?courseName=Engenharia de Software I");

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        PageableResponse<TopicResponse> pageTopics = getResponse.getBody();

        assertThat(pageTopics).isNotNull();
        assertThat(pageTopics.getNumberOfElements()).isEqualTo(1);
        assertThat(pageTopics.getContent().size()).isEqualTo(1);
        assertThat(pageTopics.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(pageTopics.getPageable().getPageSize()).isEqualTo(10);

        List<TopicResponse> content = pageTopics.getContent();

        assertTrue(content.stream().anyMatch(topic -> topic.getTitle().equals("Levantamento de requisitos")));
        assertTrue(content.stream().noneMatch(topic -> topic.getTitle().equals("Modelo lógico")));
    }

    @Test
    @DisplayName("should_return_empty_list_topics_find_by_course_nonexistent")
    public void emptyTopicListByCourseNonexistent() {
        ResponseEntity<PageableResponse<TopicResponse>> getResponse = doGetPage("?courseName=not_found");

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        PageableResponse<TopicResponse> pageTopics = getResponse.getBody();

        assertThat(pageTopics).isNotNull();
        assertThat(pageTopics.getNumberOfElements()).isEqualTo(0);
        assertThat(pageTopics.getContent().size()).isEqualTo(0);
        assertThat(pageTopics.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(pageTopics.getPageable().getPageSize()).isEqualTo(10);

        List<TopicResponse> content = pageTopics.getContent();

        assertTrue(content.stream().noneMatch(topic -> topic.getTitle().equals("Levantamento de requisitos")));
        assertTrue(content.stream().noneMatch(topic -> topic.getTitle().equals("Modelo lógico")));
    }

    @Test
    @DisplayName("should_return_topic_find_by_id")
    public void getTopicById() {
        doPost(createTopicSoftEngineer());
        ResponseEntity<String> postResponse = doPost(createTopicDatabase());

        String uri = extractUrlContext(postResponse);
        ResponseEntity<TopicResponse> getResponse = doGet(uri);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().getTitle()).isEqualTo("Modelo lógico");
        assertThat(getResponse.getBody().getCourse()).isEqualTo("Banco de Dados I");
    }

    @Test
    @DisplayName("should_return_not_found_search_topic_by_id_nonexistent")
    public void getTopicByIdNotFound() {
        ResponseEntity<TopicResponse> getResponse = doGet("/api/v1/topic/1000000");

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(getResponse.getBody()).isNotNull();
        assertTrue(getResponse.getBody().toString().contains("Tópico não encontrado"));
    }

    @Test
    @DisplayName("should_create_a_topic_successfully")
    public void createTopic() {
        ResponseEntity<String> postResponse = doPost(createTopicSoftEngineer());

        assertThat(postResponse).isNotNull();
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(postResponse.getHeaders().getLocation()).isNotNull();

        String uri = extractUrlContext(postResponse);
        ResponseEntity<TopicResponse> getResponse = doGet(uri);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().getTitle()).isEqualTo("Levantamento de requisitos");
        assertThat(getResponse.getBody().getCourse()).isEqualTo("Engenharia de Software I");
    }

    @Test
    @DisplayName("should_throw_a_validation_error_when_there_is_not_enough_data_to_create_the_topic")
    public void validateEmptyRequest() {
        ResponseEntity<String> postResponse = doPost(new TopicRequest());

        assertThat(postResponse).isNotNull();
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        assertThat(postResponse.getBody()).isNotNull();
        assertTrue(postResponse.getBody().contains("Title is mandatory"));
        assertTrue(postResponse.getBody().contains("Message is mandatory"));
        assertTrue(postResponse.getBody().contains("Course is mandatory"));
    }

    @Test
    @DisplayName("should_update_topic_successfully")
    public void updateTopic() {
        doPost(createTopicSoftEngineer());
        ResponseEntity<String> postResponse = doPost(createTopicDatabase());
        String postResource = extractUrlContext(postResponse);

        UpdateTopicRequest updateRequest = new UpdateTopicRequest();
        updateRequest.setTitle("Modelo lógico - ATUALIZADO");

        ResponseEntity<String> putResponse = doPut(extractUrlContext(postResponse), updateRequest);
        String putResource = extractUrlContext(putResponse);

        assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(postResource).isEqualTo(putResource);

        ResponseEntity<TopicResponse> getResponse = doGet(putResource);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().getTitle()).isEqualTo("Modelo lógico - ATUALIZADO");
    }

    @Test
    @DisplayName("should_not_update_topic_when_user_is_not_author")
    public void updateTopicUserNotAuthor() {
        doPost(createTopicDatabase());
        ResponseEntity<String> postResponse = doPost(createTopicSoftEngineer());

        UpdateTopicRequest topicRequest = new UpdateTopicRequest();
        topicRequest.setTitle("Titulo atualizado");

        ResponseEntity<String> putResponse = doPut(extractUrlContext(postResponse), topicRequest);

        assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(putResponse.getBody()).isNotNull();
        assertTrue(putResponse.getBody().contains("Usuário não pode atualizar este topico"));
    }

    @Test
    @DisplayName("should_not_update_topic_with_topic_id_not_found")
    public void updateTopicWithTopicIdNotFound() {
        UpdateTopicRequest topicRequest = new UpdateTopicRequest();
        topicRequest.setTitle("titulo atualizado");

        ResponseEntity<String> putResponse = doPut("/api/v1/topic/1000000", topicRequest);

        assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(putResponse.getBody()).isNotNull();
        assertTrue(putResponse.getBody().contains("Tópico não encontrado"));
    }

    @Test
    @DisplayName("should_delete_topic_successfully")
    public void deleteTopic() {
        doPost(createTopicDatabase());
        ResponseEntity<String> postResponse = doPost(createTopicSoftEngineer());
        String postResource = extractUrlContext(postResponse);

        ResponseEntity<String> deleteResponse = doDelete(postResource);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<TopicResponse> getResponse = doGet(postResource);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(getResponse.getBody()).isNotNull();
        assertTrue(getResponse.getBody().toString().contains("Tópico não encontrado"));

        ResponseEntity<PageableResponse<TopicResponse>> getPage = doGetPage("/");

        assertThat(getPage).isNotNull();

        PageableResponse<TopicResponse> pageTopics = getPage.getBody();

        assertThat(pageTopics).isNotNull();
        assertThat(pageTopics.getNumberOfElements()).isEqualTo(1);
        assertThat(pageTopics.getContent().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("should_return_not_found_on_delete_with_id_nonexistent")
    public void deleteTopicWithTopicIdNotFound() {
        ResponseEntity<String> deleteResponse = doDelete("/api/v1/topic/1000000");

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(deleteResponse.getBody()).isNotNull();
        assertTrue(deleteResponse.getBody().contains("Tópico não encontrado"));
    }

}
