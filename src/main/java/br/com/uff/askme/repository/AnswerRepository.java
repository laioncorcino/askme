package br.com.uff.askme.repository;

import br.com.uff.askme.model.Answer;
import br.com.uff.askme.model.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Query("SELECT a FROM Answer a WHERE a.topic = :topic")
    Page<Answer> findAnswersFromTopicId(@Param("topic") Topic topic, Pageable pageable);

    @Query("SELECT a FROM Answer a WHERE a.topic = :topic AND a.id = :answerId")
    Optional<Answer> findAnswerFromTopicId(@Param("topic") Topic topic, @Param("answerId") Long answerId);
}
