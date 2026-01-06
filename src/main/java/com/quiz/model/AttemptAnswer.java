package com.quiz.model;

/**
 * AttemptAnswer model
 */
public class AttemptAnswer {
    private int attemptId;
    private int questionId;
    private String selectedOption;

    public AttemptAnswer() {}

    public AttemptAnswer(int attemptId, int questionId, String selectedOption) {
        this.attemptId = attemptId;
        this.questionId = questionId;
        this.selectedOption = selectedOption;
    }

    public int getAttemptId() { return attemptId; }
    public void setAttemptId(int attemptId) { this.attemptId = attemptId; }
    public int getQuestionId() { return questionId; }
    public void setQuestionId(int questionId) { this.questionId = questionId; }
    public String getSelectedOption() { return selectedOption; }
    public void setSelectedOption(String selectedOption) { this.selectedOption = selectedOption; }
}