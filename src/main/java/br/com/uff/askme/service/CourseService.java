package br.com.uff.askme.service;

import br.com.uff.askme.model.Course;
import br.com.uff.askme.repository.CourseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CourseService {

    private CourseRepository courseRepository;

    public Course getCourseById(Long id) {
        Optional<Course> course = courseRepository.findById(id);
        return course.orElseThrow(() -> new RuntimeException("Curso não encontrado"));
    }

    public Course getCourseByName(String nameCourse) {
        Course course = courseRepository.findByName(nameCourse);
        if (course == null) {
            throw new RuntimeException("Curso não encontrado");
        }
        return course;
    }

}
