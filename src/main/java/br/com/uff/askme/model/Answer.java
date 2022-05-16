package br.com.uff.askme.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerId;

    @ManyToOne(fetch = FetchType.LAZY)
    private User author;
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    private Topic topic;
    private LocalDateTime creationDate = LocalDateTime.now();

    private Boolean solution = false;

    public Answer(String message, Topic topic, User author) {
        this.message = message;
        this.topic = topic;
        this.author = author;
    }

}
