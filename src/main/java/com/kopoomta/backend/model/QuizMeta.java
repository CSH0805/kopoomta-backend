package com.kopoomta.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "quiz_meta")
public class QuizMeta {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_meta_seq")
    @SequenceGenerator(name = "quiz_meta_seq", sequenceName = "quiz_meta_seq", allocationSize = 1)
    private Long id;

    private String language;
    private String topic;
    private String difficulty;

    @Column(name = "correct_answer", length = 500)
    private String correctAnswer;

    @Column(name = "wrong_answers", length = 2000)
    private String wrongAnswers;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

    public String getWrongAnswers() { return wrongAnswers; }
    public void setWrongAnswers(String wrongAnswers) { this.wrongAnswers = wrongAnswers; }
}