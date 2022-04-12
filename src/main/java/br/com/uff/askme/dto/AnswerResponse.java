package br.com.uff.askme.dto;

import br.com.uff.askme.model.Answer;
import br.com.uff.askme.util.DateUtil;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class AnswerResponse {

    private final Long id;
    private final String message;
    private final String creationDate;
    private final String author;
    private final Boolean solution;

    public AnswerResponse(Answer answer) {
        this.id = answer.getId();
        this.message = answer.getMessage();
        this.creationDate = DateUtil.formatLocalDateTimeToBrazilianDatabaseStyle(answer.getCreationDate());
        this.author = answer.getAuthor() != null ? answer.getAuthor() : "";
        this.solution = answer.getSolution();
    }

    public static Page<AnswerResponse> convertList(Page<Answer> answers) {
        return answers.map(AnswerResponse::new);
    }
}
