package br.com.uff.askme.unit.repository;

import br.com.uff.askme.model.Course;
import br.com.uff.askme.repository.CourseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class CourseRepositoryTest extends RepositorySystemTest {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    @DisplayName("deve_carregar_um_curso_ao_buscar_pelo_seu_nome")
    public void deve_carregar_um_curso_ao_buscar_pelo_seu_nome() {
        String courseName = "Engenharia de Software I";
        Optional<Course> course = Optional.ofNullable(courseRepository.findByName(courseName));

        assertNotNull(course);
        assertTrue(course.isPresent());
        assertEquals(courseName, course.get().getName());
    }

    @Test
    @DisplayName("nao_deve_trazer_curso_inexistente")
    public void nao_deve_trazer_curso_inexistente() {
        String courseName = "JPA";
        Optional<Course> course = Optional.ofNullable(courseRepository.findByName(courseName));
        assertEquals(course, Optional.empty());
    }

}
