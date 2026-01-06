package com.quiz.dao;

import com.quiz.model.AttemptAnswer;
import com.quiz.model.QuizAttempt;

import java.sql.*;
import java.util.List;

/**
 * QuizAttemptDAO - handles saving attempt and answers, plus retrieval for results and leaderboard.
 */
public class QuizAttemptDAO {

    /**
     * Saves a QuizAttempt and its answers. Returns the generated attempt id.
     * Uses transaction to ensure both attempt and answers are saved.
     */
    public int insertAttemptAndAnswers(QuizAttempt attempt, List<AttemptAnswer> answers) throws Exception {
        String attemptSql = "INSERT INTO quiz_attempt (participant_id, quiz_id, score, percentage, status) VALUES (?, ?, ?, ?, ?)";
        String answerSql = "INSERT INTO attempt_answers (attempt_id, question_id, selected_option) VALUES (?, ?, ?)";
        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement ps = c.prepareStatement(attemptSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, attempt.getParticipantId());
                ps.setInt(2, attempt.getQuizId());
                ps.setInt(3, attempt.getScore());
                ps.setDouble(4, attempt.getPercentage());
                ps.setString(5, attempt.getStatus());
                ps.executeUpdate();
                int attemptId = -1;
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) attemptId = rs.getInt(1);
                }
                // Insert answers
                try (PreparedStatement aps = c.prepareStatement(answerSql)) {
                    for (AttemptAnswer a : answers) {
                        aps.setInt(1, attemptId);
                        aps.setInt(2, a.getQuestionId());
                        aps.setString(3, a.getSelectedOption());
                        aps.addBatch();
                    }
                    aps.executeBatch();
                }
                c.commit();
                return attemptId;
            } catch (Exception ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public ResultSet getResultsByQuiz(int quizId) throws Exception {
        Connection c = DBConnection.getConnection();
        String sql = "SELECT qa.attempt_id, p.name AS participant, q.title AS quiz_title, qa.score, qa.percentage, qa.status, qa.attempt_date " +
                "FROM quiz_attempt qa " +
                "JOIN participant p ON p.participant_id = qa.participant_id " +
                "JOIN quiz q ON q.quiz_id = qa.quiz_id " +
                "WHERE qa.quiz_id = ? ORDER BY qa.percentage DESC";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, quizId);
        return ps.executeQuery();
    }

    // For leaderboard: get top results for a quiz
    public ResultSet getLeaderboard(int quizId) throws Exception {
        Connection c = DBConnection.getConnection();
        String sql = "SELECT p.name, qa.percentage, qa.score, qa.attempt_date FROM quiz_attempt qa JOIN participant p ON p.participant_id = qa.participant_id WHERE qa.quiz_id = ? ORDER BY qa.percentage DESC";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, quizId);
        return ps.executeQuery();
    }

    /**
     * Get attempts belonging to a specific participant (student).
     */
    public ResultSet getAttemptsByParticipant(int participantId) throws Exception {
        Connection c = DBConnection.getConnection();
        String sql = "SELECT qa.attempt_id, q.title AS quiz_title, qa.score, qa.percentage, qa.status, qa.attempt_date " +
                "FROM quiz_attempt qa JOIN quiz q ON q.quiz_id = qa.quiz_id WHERE qa.participant_id = ? ORDER BY qa.attempt_date DESC";
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, participantId);
        return ps.executeQuery();
    }
}