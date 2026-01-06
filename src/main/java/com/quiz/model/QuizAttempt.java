package com.quiz.model;

import java.sql.Timestamp;

/**
 * QuizAttempt model
 */
public class QuizAttempt {
    private int attemptId;
    private int participantId;
    private int quizId;
    private int score;
    private double percentage;
    private String status;
    private Timestamp attemptDate;

    public QuizAttempt() {}

    public QuizAttempt(int attemptId, int participantId, int quizId, int score, double percentage, String status, Timestamp attemptDate) {
        this.attemptId = attemptId;
        this.participantId = participantId;
        this.quizId = quizId;
        this.score = score;
        this.percentage = percentage;
        this.status = status;
        this.attemptDate = attemptDate;
    }

    public int getAttemptId() { return attemptId; }
    public void setAttemptId(int attemptId) { this.attemptId = attemptId; }
    public int getParticipantId() { return participantId; }
    public void setParticipantId(int participantId) { this.participantId = participantId; }
    public int getQuizId() { return quizId; }
    public void setQuizId(int quizId) { this.quizId = quizId; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public double getPercentage() { return percentage; }
    public void setPercentage(double percentage) { this.percentage = percentage; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public java.sql.Timestamp getAttemptDate() { return attemptDate; }
    public void setAttemptDate(java.sql.Timestamp attemptDate) { this.attemptDate = attemptDate; }
}