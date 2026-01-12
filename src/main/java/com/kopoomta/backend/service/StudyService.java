package com.kopoomta.backend.service;

import com.kopoomta.backend.model.StudyRecord;
import com.kopoomta.backend.repository.StudyRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudyService {

    @Autowired
    private StudyRecordRepository studyRecordRepository;

    /**
     * 학습 기록 저장
     */
    public Map<String, Object> saveStudyRecord(
            String language, String topic, String difficulty,
            int studyTimeSeconds, int quizCorrect, int quizTotal
    ) {
        try {
            StudyRecord record = new StudyRecord();
            record.setLanguage(language);
            record.setTopic(topic);
            record.setDifficulty(difficulty);
            record.setStudyTimeSeconds(studyTimeSeconds);
            record.setQuizCorrect(quizCorrect);
            record.setQuizTotal(quizTotal);
            record.setCreatedAt(LocalDateTime.now());

            studyRecordRepository.save(record);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "학습 기록이 저장되었습니다");

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            return response;
        }
    }

    /**
     * 최근 학습 기록 조회
     */
    public List<StudyRecord> getRecentStudyRecords(int limit) {
        return studyRecordRepository.findRecentRecords(limit); // 메서드 이름 변경
    }

    /**
     * 총 학습 시간 조회
     */
    public Map<String, Object> getTotalStudyTime() {
        Long totalSeconds = studyRecordRepository.sumStudyTimeSeconds();

        Map<String, Object> response = new HashMap<>();
        response.put("totalSeconds", totalSeconds != null ? totalSeconds : 0);
        response.put("totalMinutes", totalSeconds != null ? totalSeconds / 60 : 0);
        response.put("totalHours", totalSeconds != null ? totalSeconds / 3600 : 0);

        return response;
    }
}