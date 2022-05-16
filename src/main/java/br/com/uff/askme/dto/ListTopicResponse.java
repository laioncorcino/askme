package br.com.uff.askme.dto;

import br.com.uff.askme.model.Topic;
import br.com.uff.askme.util.DateUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@NoArgsConstructor
@Getter
public class ListTopicResponse {

    private Long topicId;
    private String author;
    private String title;
    private String message;
    private String course;
    private String creationDate;
    private String status;

    public ListTopicResponse(Topic topic) {
        this.topicId = topic.getTopicId();
        this.author = topic.getAuthor() != null ? topic.getAuthor().getName() : "";
        this.title = topic.getTitle();
        this.message = topic.getMessage();
        this.course = topic.getCourse().getName();
        this.creationDate = DateUtil.formatLocalDateTime(topic.getCreationDate());
        this.status = topic.getStatus().toString();
    }

    public static Page<ListTopicResponse> convertList(Page<Topic> topics) {
        return topics.map(ListTopicResponse::new);
    }

}
