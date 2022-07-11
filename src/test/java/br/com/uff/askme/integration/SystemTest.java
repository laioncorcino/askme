package br.com.uff.askme.integration;

import br.com.uff.askme.dto.TopicRequest;
import br.com.uff.askme.dto.TopicResponse;
import br.com.uff.askme.dto.UpdateTopicRequest;
import br.com.uff.askme.model.Course;
import br.com.uff.askme.model.User;
import br.com.uff.askme.wrapper.PageableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class SystemTest {

    private static final String TOPIC_API = "/api/v1/topic";

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @BeforeEach
    public void buildCenary() {
        Course dataBase = new Course("Banco de Dados I", "Banco de Dados");
        Course softwareEngineering = new Course("Engenharia de Software I", "Engenharia de Software");

        testEntityManager.persist(dataBase);
        testEntityManager.persist(softwareEngineering);

        User laion = new User("Laion", "laion@email.com", "123456");
        User paula = new User("Paula", "paula@email.com", "123456");

        testEntityManager.persist(laion);
        testEntityManager.persist(paula);
    }

    protected ResponseEntity<PageableResponse<TopicResponse>> doGetPage(String url) {
        return testRestTemplate.exchange(
                TOPIC_API + url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
    }

    protected ResponseEntity<TopicResponse> doGet(String resource) {
        return testRestTemplate.getForEntity(
                resource,
                TopicResponse.class
        );
    }

    protected ResponseEntity<String> doPost(TopicRequest topicRequest) {
        return testRestTemplate.postForEntity(
                TOPIC_API,
                topicRequest,
                String.class
        );
    }

    protected ResponseEntity<String> doPut(String resource, UpdateTopicRequest updateTopicRequest) {
        return testRestTemplate.exchange(
                resource,
                HttpMethod.PUT,
                new HttpEntity<>(updateTopicRequest, createJsonHeader()),
                String.class
        );
    }

    protected ResponseEntity<String> doDelete(String resource) {
        return testRestTemplate.exchange(
                resource,
                HttpMethod.DELETE,
                null,
                String.class
        );
    }

    protected void saveTwoTopicsInDatabase() {
        doPost(createTopicDatabase());
        doPost(createTopicSoftEngineer());
    }

    protected TopicRequest createTopicSoftEngineer() {
        TopicRequest topicRequest = new TopicRequest();
        topicRequest.setTitle("Levantamento de requisitos");
        topicRequest.setMessage("Alguem teria um script de procedimentos para levantamento de requisitos");
        topicRequest.setCourse("Engenharia de Software I");
        return topicRequest;
    }

    protected TopicRequest createTopicDatabase() {
        TopicRequest topicRequest = new TopicRequest();
        topicRequest.setTitle("Modelo lógico");
        topicRequest.setMessage("Ao juntar duas entidades n x n como fica em tabelas ?");
        topicRequest.setCourse("Banco de Dados I");
        return topicRequest;
    }

    protected String extractUrlContext(ResponseEntity<String> postResponse) {
        return Objects.requireNonNull(postResponse.getHeaders().getLocation()).getPath();
    }

    protected static HttpHeaders createJsonHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }

}
