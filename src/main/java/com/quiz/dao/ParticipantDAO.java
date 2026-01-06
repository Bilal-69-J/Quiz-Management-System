package com.quiz.dao;

import com.quiz.model.Participant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * ParticipantDAO - CRUD operations for participant table
 */
public class ParticipantDAO {

    public int insert(Participant p) throws Exception {
        String sql = "INSERT INTO participant (name, email, contact, password) VALUES (?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getEmail());
            ps.setString(3, p.getContact());
            ps.setString(4, p.getPassword());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public void update(Participant p) throws Exception {
        String sql = "UPDATE participant SET name = ?, email = ?, contact = ?, password = ? WHERE participant_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getEmail());
            ps.setString(3, p.getContact());
            ps.setString(4, p.getPassword());
            ps.setInt(5, p.getParticipantId());
            ps.executeUpdate();
        }
    }

    public void delete(int participantId) throws Exception {
        String sql = "DELETE FROM participant WHERE participant_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, participantId);
            ps.executeUpdate();
        }
    }

    public List<Participant> getAll() throws Exception {
        String sql = "SELECT participant_id, name, email, contact, password FROM participant";
        List<Participant> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Participant p = new Participant(rs.getInt("participant_id"), rs.getString("name"),
                        rs.getString("email"), rs.getString("contact"), rs.getString("password"));
                list.add(p);
            }
        }
        return list;
    }

    public Participant getById(int id) throws Exception {
        String sql = "SELECT participant_id, name, email, contact, password FROM participant WHERE participant_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Participant(rs.getInt("participant_id"), rs.getString("name"),
                            rs.getString("email"), rs.getString("contact"), rs.getString("password"));
                }
            }
        }
        return null;
    }

    /**
     * Find participant by email. Useful for student login/registration.
     */
    public Participant getByEmail(String email) throws Exception {
        String sql = "SELECT participant_id, name, email, contact, password FROM participant WHERE email = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Participant(rs.getInt("participant_id"), rs.getString("name"),
                            rs.getString("email"), rs.getString("contact"), rs.getString("password"));
                }
            }
        }
        return null;
    }

    /**
     * Validate participant login using email + password (plain-text).
     */
    public Participant validateByEmailAndPassword(String email, String password) throws Exception {
        String sql = "SELECT participant_id, name, email, contact, password FROM participant WHERE email = ? AND password = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Participant(rs.getInt("participant_id"), rs.getString("name"),
                            rs.getString("email"), rs.getString("contact"), rs.getString("password"));
                }
            }
        }
        return null;
    }
}