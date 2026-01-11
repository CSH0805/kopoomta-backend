package com.kopoomta.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;

@Service
public class GptService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generateQuizQuestion(String language, String topic,
                                       String correctAnswer, List<String> wrongAnswers) {

        String prompt = String.format(
                "아래 정답과 오답을 사용해서 %s %s 개념에 대한 객관식 문제 1개를 만들어라. " +
                        "정답은 하나만 존재해야 한다.\n\n" +
                        "정답: %s\n" +
                        "오답: %s\n\n" +
                        "문제만 출력하고 답은 포함하지 마라.",
                language, topic, correctAnswer, String.join(", ", wrongAnswers)
        );

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-3.5-turbo");
            requestBody.put("messages", Arrays.asList(
                    Map.of("role", "user", "content", prompt)
            ));
            requestBody.put("max_tokens", 200);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://api.openai.com/v1/chat/completions",
                    request,
                    String.class
            );

            Map<String, Object> responseMap = objectMapper.readValue(
                    response.getBody(), Map.class
            );

            List<Map<String, Object>> choices =
                    (List<Map<String, Object>>) responseMap.get("choices");
            Map<String, Object> message =
                    (Map<String, Object>) choices.get(0).get("message");

            return (String) message.get("content");

        } catch (Exception e) {
            e.printStackTrace();
            return "문제 생성 실패";
        }
    }
}