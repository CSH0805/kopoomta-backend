package com.kopoomta.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "study_records")
public class StudyRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "study_records_seq")
    @SequenceGenerator(name = "study_records_seq", sequenceName = "study_records_seq", allocationSize = 1)
    private Long id;

    private String userId;
    private String language;
    private String topic;
    private String difficulty;

    @Column(name = "study_time_seconds")
    private int studyTimeSeconds;

    @Column(name = "quiz_correct")
    private int quizCorrect;

    @Column(name = "quiz_total")
    private int quizTotal;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public int getStudyTimeSeconds() { return studyTimeSeconds; }
    public void setStudyTimeSeconds(int studyTimeSeconds) { this.studyTimeSeconds = studyTimeSeconds; }

    public int getQuizCorrect() { return quizCorrect; }
    public void setQuizCorrect(int quizCorrect) { this.quizCorrect = quizCorrect; }

    public int getQuizTotal() { return quizTotal; }
    public void setQuizTotal(int quizTotal) { this.quizTotal = quizTotal; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}