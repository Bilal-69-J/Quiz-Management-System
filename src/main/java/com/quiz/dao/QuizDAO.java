package com.quiz.dao;

import com.quiz.model.Quiz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * QuizDAO - CRUD operations for quiz table
 */
public class QuizDAO {

    public int insert(Quiz q) throws Exception {
        String sql = "INSERT INTO quiz (title, category, time_limit, passing_marks, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, q.getTitle());
            ps.setString(2, q.getCategory());
            ps.setInt(3, q.getTimeLimit());
            ps.setInt(4, q.getPassingMarks());
            ps.setString(5, q.getStatus());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public void update(Quiz q) throws Exception {
        String sql = "UPDATE quiz SET title=?, category=?, time_limit=?, passing_marks=?, status=? WHERE quiz_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, q.getTitle());
            ps.setString(2, q.getCategory());
            ps.setInt(3, q.getTimeLimit());
            ps.setInt(4, q.getPassingMarks());
            ps.setString(5, q.getStatus());
            ps.setInt(6, q.getQuizId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws Exception {
        String sql = "DELETE FROM quiz WHERE quiz_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Quiz> getAll() throws Exception {
        String sql = "SELECT quiz_id, title, category, time_limit, passing_marks, status FROM quiz";
        List<Quiz> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Quiz q = new Quiz(rs.getInt("quiz_id"), rs.getString("title"), rs.getString("category"),
                        rs.getInt("time_limit"), rs.getInt("passing_marks"), rs.getString("status"));
                list.add(q);
            }
        }
        return list;
    }

    public Quiz getById(int id) throws Exception {
        String sql = "SELECT quiz_id, title, category, time_limit, passing_marks, status FROM quiz WHERE quiz_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Quiz(rs.getInt("quiz_id"), rs.getString("title"), rs.getString("category"),
                            rs.getInt("time_limit"), rs.getInt("passing_marks"), rs.getString("status"));
                }
            }
        }
        return null;
    }

    public List<Quiz> getActiveQuizzes() throws Exception {
        String sql = "SELECT quiz_id, title, category, time_limit, passing_marks, status FROM quiz WHERE status = 'ACTIVE'";
        List<Quiz> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Quiz q = new Quiz(rs.getInt("quiz_id"), rs.getString("title"), rs.getString("category"),
                        rs.getInt("time_limit"), rs.getInt("passing_marks"), rs.getString("status"));
                list.add(q);
            }
        }
        return list;
    }
}