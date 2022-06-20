package br.com.uff.askme.repository;

import br.com.uff.askme.model.Answer;
import br.com.uff.askme.model.Topic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class AnswerRepositoryTest extends RepositorySystemTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @DisplayName("deve_retornar_uma_resposta_de_um_topico")
    public void deve_retornar_uma_resposta_de_um_topico() {
        Page<Answer> answers = answerRepository.findAnswersFromTopicId(engTopic, Pageable.unpaged());
        Answer answer = answers.getContent().get(0);
        assertEquals(1, answers.getSize());
        assertEquals("Usa seta pontilhada", answer.getMessage());
    }

    @Test
    @DisplayName("deve_retornar_uma_resposta_de_um_topico")
    public void deve_retornar_varias_resposta_de_um_topico() {
        Page<Answer> answers = answerRepository.findAnswersFromTopicId(databaseTopic, Pageable.unpaged());
        Answer answer1 = answers.getContent().get(0);
        Answer answer2 = answers.getContent().get(1);
        assertEquals(2, answers.getSize());
        assertEquals("Usa o group by", answer1.getMessage());
        assertEquals("Coloca o sum tmb no group by", answer2.getMessage());
        assertEquals("paula@email.com", answer2.getAuthor().getEmail());
    }

    @Test
    @DisplayName("nao_deve_retornar_uma_resposta_de_um_topico_inexistente")
    public void nao_deve_retornar_uma_resposta_de_um_topico_inexistente() {
        Topic topic = new Topic("Requisitos", "Alguem tem o pdf base de entrevistas", engenhariaSoftware, paula);
        topic.setTopicId(1L);
        Page<Answer> answers = answerRepository.findAnswersFromTopicId(topic, Pageable.unpaged());
        assertEquals(0, answers.getSize());
    }

    @Test
    public void deve_retornar_uma_resposta_encontrando_por_id_do_topico() {
        Answer answer = answerRepository.findAnswerFromTopicId(databaseTopic, databaseAnswer1.getAnswerId()).get();
        assertNotNull(answer);
        assertEquals("Usa o group by", answer.getMessage());
    }

    @Test
    public void nao_deve_retornar_uma_resposta_de_id_do_topico_inexistente() {
        Optional<Answer> answer = answerRepository.findAnswerFromTopicId(databaseTopic, 1L);
        assertEquals(answer, Optional.empty());
    }

}
