package br.com.uff.askme.dto;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class UpdateAnswerRequest {

    @Length(min = 5)
    private String message;
    private Boolean solution;

}
