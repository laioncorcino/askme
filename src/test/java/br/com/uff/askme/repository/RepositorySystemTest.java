package br.com.uff.askme.repository;

import br.com.uff.askme.model.Answer;
import br.com.uff.askme.model.Course;
import br.com.uff.askme.model.Topic;
import br.com.uff.askme.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public abstract class RepositorySystemTest {

    @Autowired
    private TestEntityManager testEntityManager;

    protected Course bancoDeDados;
    protected Course engenhariaSoftware;
    protected User laion;
    protected User paula;
    protected Topic databaseTopic;
    protected Topic engTopic;
    protected Answer databaseAnswer1;
    protected Answer databaseAnswer2;
    protected Answer engAnswer1;

    @BeforeEach
    public void buildCenary() {
        bancoDeDados = new Course("Banco de Dados I", "Banco de Dados");
        engenhariaSoftware = new Course("Engenharia de Software I", "Engenharia de Software");

        testEntityManager.persist(bancoDeDados);
        testEntityManager.persist(engenhariaSoftware);

        laion = new User("Laion", "laion@email.com", "123456");
        paula = new User("Paula", "paula@email.com", "123456");

        testEntityManager.persist(laion);
        testEntityManager.persist(paula);

        databaseTopic = new Topic("SQL", "Como agrupar por idade", bancoDeDados, laion);
        engTopic = new Topic("UML", "Esqueci como enviar uma mensagem no diagrama de sequência", engenhariaSoftware, paula);

        testEntityManager.persist(databaseTopic);
        testEntityManager.persist(engTopic);

        databaseAnswer1 = new Answer("Usa o group by", databaseTopic, paula);
        databaseAnswer2 = new Answer("Coloca o sum tmb no group by", databaseTopic, paula);
        engAnswer1 = new Answer("Usa seta pontilhada", engTopic, laion);

        testEntityManager.persist(databaseAnswer1);
        testEntityManager.persist(databaseAnswer2);
        testEntityManager.persist(engAnswer1);

        databaseTopic.setAnswers(Arrays.asList(databaseAnswer1, databaseAnswer2));
        engTopic.setAnswers(Collections.singletonList(engAnswer1));

        testEntityManager.persist(databaseTopic);
        testEntityManager.persist(engTopic);
    }
}
