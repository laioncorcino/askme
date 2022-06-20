package br.com.uff.askme.repository;

import br.com.uff.askme.model.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    Page<Topic> findByCourse_Name(String courseName, Pageable pageable);

    @Query("SELECT t FROM Topic t WHERE t.author.name = :userName")
    Page<Topic> findByUser_Name(@Param("userName") String userName, Pageable pageable);
}
