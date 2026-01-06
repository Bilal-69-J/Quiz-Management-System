package com.quiz.model;

public class Quiz {
    private int quizId;
    private String title;
    private String category;
    private int timeLimit;
    private int passingMarks;
    private String status;

    public Quiz() {}

    public Quiz(int quizId, String title, String category, int timeLimit, int passingMarks, String status) {
        this.quizId = quizId;
        this.title = title;
        this.category = category;
        this.timeLimit = timeLimit;
        this.passingMarks = passingMarks;
        this.status = status;
    }

    // ... (Keep all your existing Getters and Setters here) ...
    public int getQuizId() { return quizId; }
    public void setQuizId(int quizId) { this.quizId = quizId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getTimeLimit() { return timeLimit; }
    public void setTimeLimit(int timeLimit) { this.timeLimit = timeLimit; }
    public int getPassingMarks() { return passingMarks; }
    public void setPassingMarks(int passingMarks) { this.passingMarks = passingMarks; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // --- ADD THIS METHOD AT THE END ---
    @Override
    public String toString() {
        return this.title; // This makes the dropdown show the Title!
    }
}