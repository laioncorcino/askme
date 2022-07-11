package br.com.uff.askme.dto;

import br.com.uff.askme.model.Course;
import br.com.uff.askme.model.Topic;
import br.com.uff.askme.model.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TopicRequest {

    @NotNull @NotEmpty
    private String title;
    @NotNull @NotEmpty @Length(min = 5)
    private String message;
    @NotNull @NotEmpty
    private String course;

    public Topic convertTopic(Course course, User author) {
        return new Topic(title, message, course, author);
    }

}
