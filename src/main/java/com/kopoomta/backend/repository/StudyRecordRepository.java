package com.kopoomta.backend.repository;

import com.kopoomta.backend.model.StudyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyRecordRepository extends JpaRepository<StudyRecord, Long> {

    // Oracle 11g 호환 쿼리로 변경
    @Query(value = "SELECT * FROM (SELECT * FROM study_records ORDER BY created_at DESC) WHERE ROWNUM <= :limit", nativeQuery = true)
    List<StudyRecord> findRecentRecords(@Param("limit") int limit);

    // 총 학습 시간 조회
    @Query("SELECT SUM(s.studyTimeSeconds) FROM StudyRecord s")
    Long sumStudyTimeSeconds();
}