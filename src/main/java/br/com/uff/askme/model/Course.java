package br.com.uff.askme.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String category;

    @OneToMany(mappedBy = "course")
    private List<Topic> topics = new ArrayList<>();

    public Course(String name, String category) {
        this.name = name;
        this.category = category;
    }
}
