package br.com.uff.askme.dto;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
public class UpdateTopicRequest {

    @NotNull @NotEmpty
    private String author;
    @Length(min = 5)
    private String title;
    @Length(min = 5)
    private String message;
    private String course;

}
