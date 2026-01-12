package com.kopoomta.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GptService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * GPT를 사용하여 퀴즈 문제 텍스트 생성
     */
    public String generateQuizQuestion(String language, String topic, String correctAnswer, List<String> wrongAnswers) {
        String prompt = String.format(
                "당신은 코딩 교육 전문가입니다. %s 언어의 '%s' 개념에 대한 객관식 문제를 만들어주세요.\n\n" +
                        "정답: %s\n" +
                        "오답 선택지: %s\n\n" +
                        "요구사항:\n" +
                        "1. 문제는 명확하고 간결하게 작성\n" +
                        "2. 정답과 오답을 자연스럽게 섞어 질문 작성\n" +
                        "3. 문제만 출력하고, 선택지나 정답은 포함하지 말 것\n" +
                        "4. 한국어로 작성\n" +
                        "5. 50자 이내로 작성",
                language, topic, correctAnswer, String.join(", ", wrongAnswers)
        );

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-3.5-turbo");
            requestBody.put("messages", Arrays.asList(
                    Map.of("role", "system", "content", "당신은 프로그래밍 교육 전문가입니다."),
                    Map.of("role", "user", "content", prompt)
            ));
            requestBody.put("max_tokens", 200);
            requestBody.put("temperature", 0.7);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://api.openai.com/v1/chat/completions",
                    request,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), Map.class);
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");

                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    String generatedQuestion = (String) message.get("content");
                    return generatedQuestion.trim();
                }
            }

            return "다음 중 " + topic + "에 대한 올바른 설명은?";

        } catch (Exception e) {
            System.err.println("GPT API 호출 실패: " + e.getMessage());
            e.printStackTrace();
            return "다음 중 " + topic + "에 대한 올바른 설명은?";
        }
    }
}