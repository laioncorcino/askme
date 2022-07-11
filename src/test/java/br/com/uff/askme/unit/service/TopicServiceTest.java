package br.com.uff.askme.unit.service;

import br.com.uff.askme.dto.ListTopicResponse;
import br.com.uff.askme.dto.TopicRequest;
import br.com.uff.askme.dto.UpdateTopicRequest;
import br.com.uff.askme.error.exception.NotFoundException;
import br.com.uff.askme.model.Course;
import br.com.uff.askme.model.Topic;
import br.com.uff.askme.model.User;
import br.com.uff.askme.repository.TopicRepository;
import br.com.uff.askme.repository.UserRepository;
import br.com.uff.askme.service.CourseService;
import br.com.uff.askme.service.TopicService;
import br.com.uff.askme.service.UserService;
import br.com.uff.askme.util.TopicCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class TopicServiceTest {

    @InjectMocks
    private TopicService topicService;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private UserService userService;

    @Mock
    private CourseService courseService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("should_return_paginated_list_of_topics")
    public void listTopicPaginated() {
        List<Topic> topicList = Arrays.asList(TopicCreator.topicDataStructureWithTwoAnswer(), TopicCreator.topicDataStructureWithTwoAnswer());
        PageImpl<Topic> topicListPaginated = new PageImpl<>(topicList, buildPageable(), 2);

        Mockito.when(topicRepository.findAll(any(PageRequest.class))).thenReturn(topicListPaginated);
        Page<ListTopicResponse> listTopicResponses = topicService.listTopics(null, buildPageable());

        assertFalse(listTopicResponses.isEmpty());
        assertThat(listTopicResponses.getTotalElements()).isEqualTo(2);
    }

    @Test
    @DisplayName("should_return_topic_list_find_by_course")
    public void listTopicByCourse() {
        PageImpl<Topic> oneTopicPage = new PageImpl<>(List.of(TopicCreator.topicDataStructureWithTwoAnswer()), buildPageable(), 1);

        Mockito.when(topicRepository.findByCourse_Name(eq("Estrutura de Dados I"), any(PageRequest.class))).thenReturn(oneTopicPage);
        Page<ListTopicResponse> topics = topicService.listTopics("Estrutura de Dados I", buildPageable());

        assertFalse(topics.isEmpty());
        assertThat(topics.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("should_return_empty_list_topics_find_by_course_nonexistent")
    public void emptyTopicListByAuthorNonexistent() {
        PageImpl<Topic> oneTopicPage = new PageImpl<>(Collections.emptyList(), buildPageable(), 0);

        Mockito.when(topicRepository.findByCourse_Name(eq("xxxxx"), any(PageRequest.class))).thenReturn(oneTopicPage);
        Page<ListTopicResponse> topics = topicService.listTopics("xxxxx", buildPageable());

        assertTrue(topics.isEmpty());
        assertThat(topics.getTotalElements()).isEqualTo(0);
    }

    @Test
    @DisplayName("should_return_topic_find_by_id")
    public void getTopicById() {
        Mockito.when(topicRepository.findById(1L)).thenReturn(Optional.ofNullable(TopicCreator.topicDataStructureWithTwoAnswer()));
        Topic topic = topicService.getTopicById(1L);

        assertThat(topic).isNotNull();
        assertThat(topic.getTitle()).isEqualTo("Usar algoritmo de ordenação");
    }

    @Test
    @DisplayName("should_return_not_found_search_topics_by_id_nonexistent")
    public void getTopicByIdNotFound() {
        Mockito.when(topicRepository.findById(10L)).thenThrow(new NotFoundException("Topic not found"));

        Throwable exception = catchThrowable(() -> topicService.getTopicById(10L));

        assertThat(exception)
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Topic not found");
    }

    @Test
    @DisplayName("should_save_topic_successfully")
    public void createTopic() throws Exception {
        Mockito.when(userService.getUserById(anyLong())).thenReturn(new User("Nilson", "nilson@email.com", "1234"));
        Mockito.when(courseService.getCourseByName(anyString())).thenReturn(new Course("Banco de Dados I", "Dados"));
        Mockito.when(topicRepository.save(any(Topic.class))).thenReturn(TopicCreator.topicDataBaseWithoutAnswer());

        TopicRequest topicRequest = new TopicRequest();
        topicRequest.setTitle("Alterar atributo da tabela");
        topicRequest.setMessage("Preciso alterar o campo cpf para string");
        topicRequest.setCourse("Banco de Dados I");

        Topic topic = topicService.createTopic(topicRequest, 3L);

        assertThat(topic).isNotNull();
        assertThat(topic.getTopicId()).isNotNull();
        assertThat(topic.getTitle()).isEqualTo("Alterar atributo da tabela");
    }


    @Test
    @DisplayName("should_not_update_topic_with_title_duplicated")
    public void updateTopicTitleDuplicated() {
        Mockito.when(topicRepository.findById(5L)).thenReturn(Optional.ofNullable(TopicCreator.topicDataBaseWithoutAnswer()));
        Mockito.when(userRepository.getById(8L)).thenReturn(new User("Usuario", "usuario@email.com", "1234"));

        UpdateTopicRequest updateTopicRequest = new UpdateTopicRequest();
        updateTopicRequest.setTitle("Topico atualizado");

        Throwable exception = catchThrowable(() -> topicService.updateTopic(5L, updateTopicRequest, 8L));

        assertThat(exception)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Usuário não pode atualizar este topico");
    }

    @Test
    @DisplayName("should_delete_topic_successfully")
    public void deleteTopic() {
        Mockito.when(topicRepository.findById(1L)).thenReturn(Optional.ofNullable(TopicCreator.topicDataStructureWithTwoAnswer()));
        Mockito.doNothing().when(topicRepository).deleteById(eq(1L));

        topicService.deleteTopic(1L);
    }

    @Test
    @DisplayName("should_return_not_found_on_delete_with_id_nonexistent")
    public void deleteTopicWithTopicIdNotFound() {
        Mockito.when(topicRepository.findById(1L)).thenThrow(new NotFoundException("Tópico não encontrado"));

        Throwable exception = catchThrowable(() -> topicService.deleteTopic(1L));

        assertThat(exception)
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Tópico não encontrado");
    }

    private Pageable buildPageable() {
        return PageRequest.of(0, 10, Sort.Direction.ASC, "topicId");
    }


}
