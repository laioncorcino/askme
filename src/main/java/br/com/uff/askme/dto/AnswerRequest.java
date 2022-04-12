package br.com.uff.askme.dto;

import br.com.uff.askme.model.Answer;
import br.com.uff.askme.model.Topic;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
public class AnswerRequest {

    private String author;
    @NotNull @NotEmpty @Length(min = 5)
    private String message;

    public Answer toModel(Topic topic, String author) {
        return new Answer(message, topic, author);
    }
}
