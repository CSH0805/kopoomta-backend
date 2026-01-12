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
            // 1. DB에서 메타데이터 가져오기
            List<QuizMeta> metaList = quizMetaRepository.findByLanguageAndTopicAndDifficulty(
                    language, topic, difficulty
            );

            if (metaList.isEmpty()) {
                return createErrorResponse("해당 조건의 문제가 없습니다.");
            }

            // 2. 랜덤으로 하나 선택
            QuizMeta meta = metaList.get(new Random().nextInt(metaList.size()));

            // 3. 오답 파싱
            List<String> wrongAnswers = parseWrongAnswers(meta.getWrongAnswers());

            // 4. GPT로 문제 텍스트 생성 ✨ 핵심!
            String questionText = gptService.generateQuizQuestion(
                    language,
                    topic,
                    meta.getCorrectAnswer(),
                    wrongAnswers
            );

            // 5. 선택지 섞기
            List<String> options = new ArrayList<>();
            options.add(meta.getCorrectAnswer());
            options.addAll(wrongAnswers);
            Collections.shuffle(options);

            // 6. 정답 인덱스 찾기
            int correctIndex = options.indexOf(meta.getCorrectAnswer());

            // 7. 응답 생성
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
            return createErrorResponse("문제 생성 중 오류가 발생했습니다: " + e.getMessage());
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