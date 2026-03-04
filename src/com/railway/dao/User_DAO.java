package com.railway.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.railway.db.DBConnection;
import com.railway.model.User;
public class User_DAO {
	// Method to Register a New User
	public boolean registerUser(User user) {
		boolean success = false;
		String sql = "INSERT INTO users (username, password, fullname, role) VALUES (?, ?, ?, ?)";
		//?,? is use for protect against SQL Injection attacks.
		try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
		//https://www.geeksforgeeks.org/java/how-to-use-preparedstatement-in-java/
			//BELOW LINE I USE IT FOR REPLACE ? WITH THE NEW DATA.
			pst.setString(1, user.getUsername());
			pst.setString(2, user.getPassword());
			pst.setString(3, user.getFullname());
			pst.setString(4, user.getRole());
			//ROW AFFECTED IS GREATER THAN 0 THAN MEAN TABLE HAS BEEN UPDATE
			int rowsAffected = pst.executeUpdate();
			if (rowsAffected > 0) {
				success = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}
	// Method to Login a User
	public User loginUser(String username, String password) {
		User user = null;
		String sql = "SELECT * FROM users WHERE username = ? AND password = ?";//security
		try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
			pst.setString(1, username);
			pst.setString(2, password);
			ResultSet rs = pst.executeQuery();
			//find user info 
			if (rs.next()) {
				user = new User();
				//setter method from model/user and getInt fetch data from table
				user.setUserId(rs.getInt("user_id"));
				user.setUsername(rs.getString("username"));
				user.setFullname(rs.getString("fullname"));
				user.setRole(rs.getString("role"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	public boolean updatePassword(String username, String newPassword) {
	    String sql = "UPDATE users SET password = ? WHERE username = ?";
	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, newPassword);
	        ps.setString(2, username);
	        return ps.executeUpdate() > 0;
	    } catch (Exception e) { return false; }
	}
	
}