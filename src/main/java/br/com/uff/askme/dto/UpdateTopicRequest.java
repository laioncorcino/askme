package br.com.uff.askme.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class UpdateTopicRequest {

    @Length(min = 5)
    private String title;
    @Length(min = 5)
    private String message;
    private String course;

}
