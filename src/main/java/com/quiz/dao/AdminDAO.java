package com.quiz.dao;

import com.quiz.model.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * AdminDAO - simple DAO for Admin table.
 * All SQL uses PreparedStatement.
 */
public class AdminDAO {

    public boolean validate(String username, String password) throws Exception {
        String sql = "SELECT admin_id FROM admin WHERE username = ? AND password = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public int insert(Admin admin) throws Exception {
        String sql = "INSERT INTO admin (username, password) VALUES (?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, admin.getUsername());
            ps.setString(2, admin.getPassword());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public void update(Admin admin) throws Exception {
        String sql = "UPDATE admin SET username = ?, password = ? WHERE admin_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, admin.getUsername());
            ps.setString(2, admin.getPassword());
            ps.setInt(3, admin.getAdminId());
            ps.executeUpdate();
        }
    }

    public void delete(int adminId) throws Exception {
        String sql = "DELETE FROM admin WHERE admin_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, adminId);
            ps.executeUpdate();
        }
    }

    public List<Admin> getAll() throws Exception {
        String sql = "SELECT admin_id, username, password FROM admin";
        List<Admin> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Admin a = new Admin(rs.getInt("admin_id"), rs.getString("username"), rs.getString("password"));
                list.add(a);
            }
        }
        return list;
    }

    public Admin getById(int adminId) throws Exception {
        String sql = "SELECT admin_id, username, password FROM admin WHERE admin_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, adminId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Admin(rs.getInt("admin_id"), rs.getString("username"), rs.getString("password"));
                }
            }
        }
        return null;
    }
}