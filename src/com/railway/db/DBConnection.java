package com.railway.db;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
	//Database info
	private static final String URL  = "jdbc:mysql://localhost:3306/railway_system";
	private static final String USER = "root";
	private static final String PASSWORD = ""; 
	//Method to get the connection
	public static Connection getConnection() {
		Connection con = null;//due to error i put this here to fix.
		try {
			// Load Driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			// Connect
			con = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}
	// Test
    public static void main(String[] args) {
        Connection conn = getConnection();
        
        if (conn != null) {
            System.out.println("Connected to the Database.");
        } else {
            System.out.println("Connection is fail.");
        }
    }
}