package com.kopoomta.backend.repository;

import com.kopoomta.backend.model.QuizMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuizMetaRepository extends JpaRepository<QuizMeta, Long> {

    // 언어, 주제, 난이도로 문제 찾기
    List<QuizMeta> findByLanguageAndTopicAndDifficulty(
            String language, String topic, String difficulty
    );

    // 랜덤으로 1개 가져오기
    @Query(value = "SELECT * FROM quiz_meta WHERE language = ?1 AND topic = ?2 ORDER BY DBMS_RANDOM.VALUE FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
    QuizMeta findRandomByLanguageAndTopic(String language, String topic);
}