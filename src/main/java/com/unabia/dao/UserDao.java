package com.unabia.dao;

import com.unabia.model.User;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Optional;

/**
 * All SQL queries for the users table. Every query uses PreparedStatement.
 * Passwords are never stored as plain text: they are salted and hashed (PBKDF2).
 */
public class UserDao {

    public static final String STATUS_PENDING = "PENDING";

    public boolean usernameExists(String username) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /** Saves a new account with the chosen role and the default status PENDING. */
    public void register(String username, String password, String fullName, String role) throws SQLException {
        String sql = "INSERT INTO users (username, password_hash, full_name, role, status, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, hashPassword(password));
            ps.setString(3, fullName);
            ps.setString(4, role);
            ps.setString(5, STATUS_PENDING);
            ps.setString(6, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            ps.executeUpdate();
        }
    }

    /** Returns the user if the username exists and the password matches. */
    public Optional<User> login(String username, String password) throws SQLException {
        String sql = "SELECT user_id, username, password_hash, full_name, role, status, created_at FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password_hash");
                    if (verifyPassword(password, storedPassword)) {
                        return Optional.of(new User(
                                rs.getInt("user_id"),
                                rs.getString("username"),
                                storedPassword,
                                rs.getString("full_name"),
                                rs.getString("role"),
                                rs.getString("status"),
                                rs.getString("created_at")));
                    }
                }
            }
        }
        return Optional.empty();
    }

    // ---------- password hashing ----------

    private static String hashPassword(String password) {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        byte[] hash = pbkdf2(password, salt);
        return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
    }

    private static boolean verifyPassword(String password, String stored) {
        String[] parts = stored.split(":");
        if (parts.length != 2) {
            return false;
        }
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] expected = Base64.getDecoder().decode(parts[1]);
        return MessageDigest.isEqual(expected, pbkdf2(password, salt));
    }

    private static byte[] pbkdf2(String password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Password hashing failed", e);
        }
    }
}
