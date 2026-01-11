package com.kopoomta.backend.controller;

import com.kopoomta.backend.model.QuizMeta;
import com.kopoomta.backend.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/quiz")
@CrossOrigin(origins = "*")  // Flutter 연결용
public class QuizController {

    @Autowired
    private QuizService quizService;

    // 문제 생성 요청
    @PostMapping("/generate")
    public Map<String, Object> generateQuiz(@RequestBody Map<String, String> request) {
        String language = request.get("language");
        String topic = request.get("topic");
        String difficulty = request.get("difficulty");

        return quizService.generateQuiz(language, topic, difficulty);
    }
}