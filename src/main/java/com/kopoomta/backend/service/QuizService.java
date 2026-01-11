package com.kopoomta.backend.service;

import com.kopoomta.backend.model.QuizMeta;
import com.kopoomta.backend.repository.QuizMetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuizService {

    @Autowired
    private QuizMetaRepository quizMetaRepository;

    @Autowired
    private GptService gptService;

    public Map<String, Object> generateQuiz(String language, String topic, String difficulty) {
        try {
            // 1. DB에서 문제 메타데이터 가져오기
            List<QuizMeta> metaList = quizMetaRepository.findByLanguageAndTopicAndDifficulty(
                    language, topic, difficulty
            );

            if (metaList.isEmpty()) {
                return createErrorResponse("해당 조건의 문제가 없습니다.");
            }

            // 랜덤으로 1개 선택
            QuizMeta meta = metaList.get(new Random().nextInt(metaList.size()));

            // 2. 오답 리스트 파싱
            List<String> wrongAnswers = parseWrongAnswers(meta.getWrongAnswers());

            // 3. GPT로 문제 생성 (일단 간단하게)
            String questionText = "다음 중 " + topic + "에 대한 올바른 설명은?";

            // 4. 선택지 섞기
            List<String> options = new ArrayList<>();
            options.add(meta.getCorrectAnswer());
            options.addAll(wrongAnswers);
            Collections.shuffle(options);

            // 5. 정답 인덱스
            int correctIndex = options.indexOf(meta.getCorrectAnswer());

            // 6. 응답 생성
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("question", questionText);
            response.put("options", options);
            response.put("correctIndex", correctIndex);
            response.put("language", language);
            response.put("topic", topic);
            response.put("difficulty", difficulty);

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return createErrorResponse("문제 생성 중 오류 발생: " + e.getMessage());
        }
    }

    private List<String> parseWrongAnswers(String wrongAnswers) {
        if (wrongAnswers == null || wrongAnswers.trim().isEmpty()) {
            return Arrays.asList("오답1", "오답2", "오답3");
        }

        if (wrongAnswers.startsWith("[")) {
            wrongAnswers = wrongAnswers.substring(1, wrongAnswers.length() - 1);
        }

        return Arrays.stream(wrongAnswers.split(","))
                .map(s -> s.trim().replace("\"", ""))
                .toList();
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", message);
        return response;
    }
}