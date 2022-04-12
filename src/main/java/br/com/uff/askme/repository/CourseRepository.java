package br.com.uff.askme.repository;

import br.com.uff.askme.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Course findByName(String courseName);
}
