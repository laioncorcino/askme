package br.com.uff.askme.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long topicId;

    @ManyToOne(fetch = FetchType.LAZY)
    private User author;
    private String title;
    private String message;

    private LocalDateTime creationDate = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private TopicStatus status = TopicStatus.NAO_RESPONDIDO;

    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
    private List<Answer> answers = new ArrayList<>();

    public Topic(String title, String message, Course course, User author) {
        this.title = title;
        this.message = message;
        this.course = course;
        this.author = author;
    }

}
