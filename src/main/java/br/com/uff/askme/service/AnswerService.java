package br.com.uff.askme.service;

import br.com.uff.askme.dto.AnswerRequest;
import br.com.uff.askme.dto.AnswerResponse;
import br.com.uff.askme.dto.UpdateAnswerRequest;
import br.com.uff.askme.model.Answer;
import br.com.uff.askme.model.Topic;
import br.com.uff.askme.model.User;
import br.com.uff.askme.repository.AnswerRepository;
import br.com.uff.askme.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.nio.file.AccessDeniedException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AnswerService {

    private AnswerRepository answerRepository;
    private TopicService topicService;
    private UserService userService;
    private UserRepository userRepository;

    public Page<AnswerResponse> listAnswer(Pageable pageable, Long topicId) {
        Topic topic = getTopic(topicId);
        Page<Answer> answers = answerRepository.findAnswersFromTopicId(topic, pageable);
        return AnswerResponse.convertList(answers);
    }

    public Answer createAnswer(AnswerRequest answerRequest, Long topicId, Long userId) {
        User user = userService.getUserById(userId);
        Topic topic = getTopic(topicId);
        Answer answer = answerRequest.toModel(topic, user);
        return answerRepository.save(answer);
    }

    public Answer getAnswerById(Long topicId, Long answerId) {
        Topic topic = getTopic(topicId);
        Optional<Answer> answer = answerRepository.findAnswerFromTopicId(topic, answerId);
        return answer.orElseThrow(() -> new EntityNotFoundException("Resposta não encontrada"));
    }

    public Answer updateAnswer(Long topicId, Long answerId, UpdateAnswerRequest updateAnswerRequest, Long userId) throws AccessDeniedException {
        Answer answer = getAnswerById(topicId, answerId);
        User user = userRepository.getById(userId);

        if (answer.getAuthor() != null && answer.getAuthor() != user) {
            throw new AccessDeniedException("Usuário não pode atualizar esta resposta");
        }

        if (StringUtils.isNotBlank(updateAnswerRequest.getMessage())) {
            answer.setMessage(updateAnswerRequest.getMessage());
        }
        if (updateAnswerRequest.getSolution() != null) {
            answer.setSolution(updateAnswerRequest.getSolution());
        }

        return answerRepository.save(answer);
    }

    public void deleteAnswer(Long topicId, Long answerId) {
        Answer answer = getAnswerById(topicId, answerId);
        answerRepository.delete(answer);
    }

    private Topic getTopic(Long topicId) {
        return topicService.getTopicById(topicId);
    }

}
