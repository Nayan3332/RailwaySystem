package com.railway.dao;
import java.sql.*;
import com.railway.db.DBConnection;
import javax.swing.table.DefaultTableModel;
public class Booking_DAO {
	// Method to book a ticket 3 thing are send by user.
	public boolean bookTicket(int userId, int trainNo, String passengerName) {
		String insertSql = "INSERT INTO bookings(user_id, train_no, passenger_name, booking_date, status) VALUES(?,?,?,CURDATE(), 'Confirmed')";
		String updateSql = "UPDATE trains SET seats = seats - 1 WHERE train_no = ? AND seats > 0";
		try (Connection con = DBConnection.getConnection()) {
			con.setAutoCommit(false);
			// Insert Booking
			PreparedStatement psInsert = con.prepareStatement(insertSql);
			psInsert.setInt(1, userId);
			psInsert.setInt(2, trainNo);
			psInsert.setString(3, passengerName);
			int rowsInserted = psInsert.executeUpdate();
			// Reduce Seat count
			PreparedStatement psUpdate = con.prepareStatement(updateSql);
			psUpdate.setInt(1, trainNo);
			int rowsUpdated = psUpdate.executeUpdate();
			if (rowsInserted > 0 && rowsUpdated > 0) {
				con.commit(); 
				return true;
			} else {
				con.rollback();
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	// Method to load history
	public void loadHistory(String name, DefaultTableModel model) {
		String sql = "SELECT pnr, train_no, booking_date, status FROM bookings WHERE passenger_name = ?";
		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			model.setRowCount(0);
			while (rs.next()) {
				model.addRow(new Object[] { rs.getInt("pnr"), rs.getInt("train_no"), rs.getString("booking_date"),
						rs.getString("status") });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// find,cancel,addseat
	public boolean cancelTicket(int pnr) {
		String findTrainSql = "SELECT train_no FROM bookings WHERE pnr = ? AND status = 'Confirmed'";
		String cancelSql = "UPDATE bookings SET status = 'Cancelled' WHERE pnr = ?";
		String addSeatSql = "UPDATE trains SET seats = seats + 1 WHERE train_no = ?";
		try (Connection con = DBConnection.getConnection()) {
			con.setAutoCommit(false);
			PreparedStatement psFind = con.prepareStatement(findTrainSql);
			psFind.setInt(1, pnr);
			ResultSet rs = psFind.executeQuery();
			if (rs.next()) {
				int trainNo = rs.getInt("train_no");
				PreparedStatement psCancel = con.prepareStatement(cancelSql);
				psCancel.setInt(1, pnr);
				psCancel.executeUpdate();
				PreparedStatement psAddSeat = con.prepareStatement(addSeatSql);
				psAddSeat.setInt(1, trainNo);
				psAddSeat.executeUpdate();
				con.commit();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public Object[] checkPNRStatus(int pnr) {
		String sql = "SELECT b.pnr, b.train_no, t.train_name, b.booking_date, b.status "
				+ "FROM bookings b JOIN trains t ON b.train_no = t.train_no WHERE b.pnr = ?";
		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, pnr);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return new Object[] { rs.getInt("pnr"), rs.getInt("train_no"), rs.getString("train_name"),
						rs.getString("booking_date"), rs.getString("status") };
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}