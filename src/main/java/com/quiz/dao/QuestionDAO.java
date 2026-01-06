package com.quiz.dao;

import com.quiz.model.Question;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO {

    public int insert(Question q) throws Exception {
        String sql = "INSERT INTO question (quiz_id, question_text, option_a, option_b, option_c, option_d, correct_option) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, q.getQuizId());
            ps.setString(2, q.getQuestionText());
            ps.setString(3, q.getOptionA());
            ps.setString(4, q.getOptionB());
            ps.setString(5, q.getOptionC());
            ps.setString(6, q.getOptionD());
            ps.setString(7, q.getCorrectOption());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) return rs.getInt(1); }
        }
        return -1;
    }

    public void update(Question q) throws Exception {
        String sql = "UPDATE question SET question_text=?, option_a=?, option_b=?, option_c=?, option_d=?, correct_option=? WHERE question_id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, q.getQuestionText()); ps.setString(2, q.getOptionA()); ps.setString(3, q.getOptionB()); ps.setString(4, q.getOptionC()); ps.setString(5, q.getOptionD()); ps.setString(6, q.getCorrectOption()); ps.setInt(7, q.getQuestionId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws Exception {
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM question WHERE question_id = ?")) {
            ps.setInt(1, id); ps.executeUpdate();
        }
    }

    public List<Question> getAllByQuiz(int quizId) throws Exception {
        String sql = "SELECT * FROM question WHERE quiz_id = ?";
        List<Question> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, quizId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    // --- NEW METHOD FOR RANDOM QUESTIONS ---
    public List<Question> getRandomQuestions(int quizId, int limit) throws Exception {
        // "ORDER BY RAND()" is for MySQL. Use "RANDOM()" for PostgreSQL/SQLite if needed.
        String sql = "SELECT * FROM question WHERE quiz_id = ? ORDER BY RAND() LIMIT ?";
        List<Question> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, quizId);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }
    // ----------------------------------------

    private Question mapRow(ResultSet rs) throws Exception {
        return new Question(rs.getInt("question_id"), rs.getInt("quiz_id"),
                rs.getString("question_text"), rs.getString("option_a"), rs.getString("option_b"),
                rs.getString("option_c"), rs.getString("option_d"), rs.getString("correct_option"));
    }
}