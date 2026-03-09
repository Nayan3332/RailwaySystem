package com.railway.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.security.MessageDigest;
import java.util.Base64;
import com.railway.db.DBConnection;
import com.railway.model.User;

public class User_DAO {

	// Hashing Password
	private String hashPassword(String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(password.getBytes("UTF-8"));
			return Base64.getEncoder().encodeToString(hash);
		} catch (Exception e) {
			e.printStackTrace();
			return password;
		}
	}

	// Register User
	public boolean registerUser(User user) {
		String sql = "INSERT INTO users (username, password, fullname, role) VALUES (?, ?, ?, ?)";
		try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

			pst.setString(1, user.getUsername());
			
			pst.setString(2, hashPassword(user.getPassword()));
			pst.setString(3, user.getFullname());
			pst.setString(4, user.getRole());

			return pst.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// Login Logic
	public User loginUser(String username, String password) {
		User user = null;

		String sql = "SELECT * FROM users WHERE BINARY username = ? AND BINARY password = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

			pst.setString(1, username);

			pst.setString(2, hashPassword(password));

			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				user = new User();
				user.setUserId(rs.getInt("user_id"));
				user.setUsername(rs.getString("username"));
				user.setFullname(rs.getString("fullname"));
				user.setRole(rs.getString("role"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public boolean updatePassword(int userId, String newPassword) {
		String sql = "UPDATE users SET password = ? WHERE user_id = ?";
		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, hashPassword(newPassword));
			ps.setInt(2, userId);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<User> getAllUsers() {
		List<User> userList = new ArrayList<>();
		String sql = "SELECT user_id, username, fullname, role FROM users";
		try (Connection con = DBConnection.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				User u = new User();
				u.setUserId(rs.getInt("user_id"));
				u.setUsername(rs.getString("username"));
				u.setFullname(rs.getString("fullname"));
				u.setRole(rs.getString("role"));
				userList.add(u);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userList;
	}

	public boolean deleteUser(int userId) {
		String sql = "DELETE FROM users WHERE user_id = ?";
		try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
			pst.setInt(1, userId);
			return pst.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public User getUserByUsername(String username) {
		User user = null;
		String sql = "SELECT * FROM users WHERE username = ?";
		try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
			pst.setString(1, username);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				user = new User();
				user.setUserId(rs.getInt("user_id"));
				user.setUsername(rs.getString("username"));
				user.setFullname(rs.getString("fullname"));
				user.setRole(rs.getString("role"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}
}