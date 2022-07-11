package br.com.uff.askme.unit.repository;

import br.com.uff.askme.model.Topic;
import br.com.uff.askme.repository.TopicRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class TopicRepositoryTest extends RepositorySystemTest {

    @Autowired
    private TopicRepository topicRepository;

    @Test
    public void deve_retornar_topico_buscando_por_curso() {
        Page<Topic> topics = topicRepository.findByCourse_Name("Engenharia de Software I", Pageable.unpaged());
        Topic topic = topics.getContent().get(0);
        assertEquals(1, topics.getSize());
        assertEquals("UML", topic.getTitle());
    }

    @Test
    public void nao_deve_retornar_topico_buscando_por_curso_inexistente() {
        Page<Topic> topics = topicRepository.findByCourse_Name("Estrutura de Dados", Pageable.unpaged());
        assertEquals(0, topics.getSize());
        assertTrue(topics.isEmpty());
    }

    @Test
    public void deve_retornar_topico_buscando_por_autor() {
        Page<Topic> topics = topicRepository.findByUser_Name("Paula", Pageable.unpaged());
        Topic topic = topics.getContent().get(0);
        assertEquals(1, topics.getSize());
        assertEquals("UML", topic.getTitle());
    }

    @Test
    public void nao_deve_retornar_topico_buscando_por_autor_inexistente() {
        Page<Topic> topics = topicRepository.findByUser_Name("João", Pageable.unpaged());
        assertEquals(0, topics.getSize());
        assertTrue(topics.isEmpty());
    }

}
