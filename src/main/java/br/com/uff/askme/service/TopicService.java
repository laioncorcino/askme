package br.com.uff.askme.service;

import br.com.uff.askme.dto.TopicRequest;
import br.com.uff.askme.dto.TopicResponse;
import br.com.uff.askme.dto.UpdateTopicRequest;
import br.com.uff.askme.model.Course;
import br.com.uff.askme.model.Topic;
import br.com.uff.askme.model.User;
import br.com.uff.askme.repository.TopicRepository;
import br.com.uff.askme.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class TopicService {

    private TopicRepository topicRepository;
    private CourseService courseService;
    private UserRepository userRepository;
    private UserService userService;

    public Page<TopicResponse> listTopics(String courseName, Pageable pageable) {
        Page<Topic> topics;

        if (courseName == null) {
            topics = topicRepository.findAll(pageable);
        } else {
            topics = topicRepository.findByCourse_Name(courseName, pageable);
        }

        return TopicResponse.convertList(topics);
    }

    public Topic getTopicById(Long id) {
        Optional<Topic> topic = topicRepository.findById(id);
        return topic.orElseThrow(() -> new EntityNotFoundException("Tópico não encontrado"));
    }

    public Topic createTopic(TopicRequest topicRequest, Long userId) {
        User user = userService.getUserById(userId);
        Course course = courseService.getCourseByName(topicRequest.getCourse());
        Topic topic = topicRequest.convertTopic(course, user);
        return topicRepository.save(topic);
    }

    public Topic updateTopic(Long id, UpdateTopicRequest updateTopicRequest, Long userId) throws Exception {
        Topic topic = getTopicById(id);
        User user = userRepository.getById(userId);

        if (topic.getAuthor() != user) {
            throw new RuntimeException("Usuário não pode atualizar este topico");
        }

        if (StringUtils.isNotBlank(updateTopicRequest.getMessage())) {
            topic.setMessage(updateTopicRequest.getMessage());
        }

        if (StringUtils.isNotBlank(updateTopicRequest.getTitle())) {
            topic.setTitle(updateTopicRequest.getTitle());
        }

        if (StringUtils.isNotBlank(updateTopicRequest.getCourse())) {
            Course course = courseService.getCourseByName(updateTopicRequest.getCourse());
            topic.setCourse(course);
        }

        log.info("Atualizando topico");
        return saveTopic(topic);
    }

    public void deleteTopic(Long id) {
        getTopicById(id);
        topicRepository.deleteById(id);
    }

    private Topic saveTopic(Topic topic) throws Exception {
        try {
            return topicRepository.save(topic);
        }
        catch (Exception | Error e) {
            log.error("Erro ao salvar topico", e.getCause());
            throw new Exception("Erro ao salvar topico");
        }
    }

}












