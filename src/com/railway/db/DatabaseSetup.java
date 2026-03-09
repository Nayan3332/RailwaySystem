package com.railway.db;

import java.sql.*;
import java.security.MessageDigest;
import java.util.Base64;

public class DatabaseSetup {

	private static String hashPassword(String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(password.getBytes("UTF-8"));
			return Base64.getEncoder().encodeToString(hash);
		} catch (Exception e) {
			return password;
		}
	}

	public static void main(String[] args) {
		try {
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "");
			Statement stmt = con.createStatement();

			stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS railway_system");
			stmt.executeUpdate("USE railway_system");

			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (" + "user_id INT AUTO_INCREMENT PRIMARY KEY, "
					+ "username VARCHAR(50) UNIQUE, " + "password VARCHAR(255), " + "fullname VARCHAR(100), "
					+ "role VARCHAR(10) DEFAULT 'user')");

			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS trains (" + "train_no INT PRIMARY KEY, "
					+ "train_name VARCHAR(100), " + "source VARCHAR(50), " + "destination VARCHAR(50), "
					+ "price DECIMAL(10,2) CHECK (price > 0), " + "seats INT CHECK (seats >= 0), "
					+ "is_active TINYINT(1) DEFAULT 1)");

			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS bookings (" + "pnr BIGINT PRIMARY KEY, " + "user_id INT, "
					+ "train_no INT, " + "seat_no INT, " + "booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
					+ "status VARCHAR(20) DEFAULT 'Confirmed', "
					+ "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE, "
					+ "FOREIGN KEY (train_no) REFERENCES trains(train_no))");

			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS train_tracking (" + "train_no INT, "
					+ "current_location VARCHAR(100), " + "status VARCHAR(50), "
					+ "last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, "
					+ "FOREIGN KEY (train_no) REFERENCES trains(train_no) ON DELETE CASCADE)");

			// DUMMY DATA

			String adminHash = hashPassword("admin123");
			String userHash = hashPassword("g");

			String userInsert = "INSERT IGNORE INTO users (username, password, fullname, role) VALUES " + "('admin', '"
					+ adminHash + "', 'System Administrator', 'admin'), " + "('g', '" + userHash
					+ "', 'Nayan Kumar', 'user')";
			stmt.executeUpdate(userInsert);

			stmt.executeUpdate(
					"INSERT IGNORE INTO trains (train_no, train_name, source, destination, price, seats) VALUES "
							+ "(12001, 'Shatabdi Express', 'Bhopal', 'Delhi', 1200.00, 50), "
							+ "(12431, 'Rajdhani Express', 'Mumbai', 'Delhi', 2500.00, 40), "
							+ "(11011, 'Intercity Exp', 'Pune', 'Solapur', 350.00, 100)");

			stmt.executeUpdate("INSERT IGNORE INTO train_tracking (train_no, current_location, status) VALUES "
					+ "(12001, 'Bhopal Junction', 'On Time'), " + "(12431, 'Indore', 'Delayed by 15 mins'), "
					+ "(11011, 'Pune', 'On Time')");

			System.out.println("(¬‿¬)");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}