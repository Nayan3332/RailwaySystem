package com.railway.db;
import java.sql.*;
public class DatabaseSetup {
	public static void main(String[] args) {
		try {
			//Connect to xampp
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "");
			Statement stmt = con.createStatement();
			//Create and Enter Database
			stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS railway_system");
			stmt.executeUpdate("USE railway_system");
			//Create users Table
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (" + "user_id INT AUTO_INCREMENT PRIMARY KEY, "
					+ "username VARCHAR(50) UNIQUE, " + "password VARCHAR(50), " + "fullname VARCHAR(100), "
					+ "role VARCHAR(10) DEFAULT 'user')");
			//Create trains Table
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS trains (" + "train_no INT PRIMARY KEY, "
					+ "train_name VARCHAR(100), " + "source VARCHAR(50), " + "destination VARCHAR(50), "
					+ "price DECIMAL(10,2), " + "seats INT)");
			//Create bookings Table
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS bookings (" + "pnr INT AUTO_INCREMENT PRIMARY KEY, "
					+ "user_id INT, " + "train_no INT, " + "passenger_name VARCHAR(100), " + "booking_date DATE)");
			//Add  Admin 
			stmt.executeUpdate("INSERT IGNORE INTO users (username, password, fullname, role) "
					+ "VALUES ('admin', 'admin123', 'System Administrator', 'admin')");
			System.out.println(".☆*: .｡. o(≧▽≦)o .｡.:*☆.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}