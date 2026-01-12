package com.kopoomta.backend.controller;

import com.kopoomta.backend.model.StudyRecord;
import com.kopoomta.backend.service.StudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/study")
@CrossOrigin(origins = "*")
public class StudyController {

    @Autowired
    private StudyService studyService;

    // 학습 기록 저장
    @PostMapping("/record")
    public Map<String, Object> saveStudyRecord(@RequestBody Map<String, Object> request) {
        String language = (String) request.get("language");
        String topic = (String) request.get("topic");
        String difficulty = (String) request.get("difficulty");
        int studyTimeSeconds = (int) request.get("studyTimeSeconds");
        int quizCorrect = (int) request.get("quizCorrect");
        int quizTotal = (int) request.get("quizTotal");

        return studyService.saveStudyRecord(
                language, topic, difficulty,
                studyTimeSeconds, quizCorrect, quizTotal
        );
    }

    // 최근 학습 기록 조회
    @GetMapping("/recent")
    public List<StudyRecord> getRecentStudyRecords() {
        return studyService.getRecentStudyRecords(10);
    }

    // 총 학습 시간 조회
    @GetMapping("/total-time")
    public Map<String, Object> getTotalStudyTime() {
        return studyService.getTotalStudyTime();
    }
}