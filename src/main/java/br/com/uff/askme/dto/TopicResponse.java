package br.com.uff.askme.dto;

import br.com.uff.askme.model.Topic;
import br.com.uff.askme.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class TopicResponse {

    private final Long id;
    private final String author;
    private final String title;
    private final String message;
    private final String course;
    private final String creationDate;
    private final String status;
    @JsonProperty("answer")
    private final List<AnswerResponse> answerResponses = new ArrayList<>();

    public TopicResponse(Topic topic) {
        this.id = topic.getTopicId();
        this.author = topic.getAuthor() != null ? topic.getAuthor().getName() : "";
        this.title = topic.getTitle();
        this.message = topic.getMessage();
        this.course = topic.getCourse() != null ? topic.getCourse().getName() : "";
        this.creationDate = DateUtil.formatLocalDateTime(topic.getCreationDate());
        this.status = topic.getStatus().toString();
        this.answerResponses.addAll(convertAnswerToResponse(topic));
    }

    public static Page<TopicResponse> convertList(Page<Topic> topics) {
        return topics.map(TopicResponse::new);
    }

    private List<AnswerResponse> convertAnswerToResponse(Topic topic) {
        return topic.getAnswers().stream().map(AnswerResponse::new).collect(Collectors.toList());
    }
}
