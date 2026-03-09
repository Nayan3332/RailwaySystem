package com.railway.dao;

import java.sql.*;
import com.railway.db.DBConnection;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class Booking_DAO {

	// Booking with Seat
	public boolean bookTicket(int userId, int trainNo, String username, int seatNo) {
		long pnr = (long) (Math.random() * 9000000000L) + 1000000000L;
		String insertSQL = "INSERT INTO bookings (pnr, user_id, train_no, seat_no, status) VALUES (?, ?, ?, ?, 'Confirmed')";
		String updateSQL = "UPDATE trains SET seats = seats - 1 WHERE train_no = ? AND seats > 0";

		try (Connection con = DBConnection.getConnection()) {
			con.setAutoCommit(false);
			try (PreparedStatement pst1 = con.prepareStatement(insertSQL);
					PreparedStatement pst2 = con.prepareStatement(updateSQL)) {

				pst1.setLong(1, pnr);
				pst1.setInt(2, userId);
				pst1.setInt(3, trainNo);
				pst1.setInt(4, seatNo);

				int rows1 = pst1.executeUpdate();
				pst2.setInt(1, trainNo);
				int rows2 = pst2.executeUpdate();

				if (rows1 > 0 && rows2 > 0) {
					con.commit();
					return true;
				} else {
					con.rollback();
					return false;
				}
			} catch (SQLException e) {
				con.rollback();
				e.printStackTrace();
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// History
	public void loadHistory(int userId, DefaultTableModel model) {
		String sql = "SELECT pnr, train_no, seat_no, booking_date, status FROM bookings WHERE user_id = ?";
		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();
			model.setRowCount(0);
			while (rs.next()) {
				model.addRow(new Object[] { rs.getLong("pnr"), rs.getInt("train_no"), rs.getInt("seat_no"),
						rs.getString("booking_date"), rs.getString("status") });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Cancel Ticket
	public boolean cancelTicket(long pnr) {
		String findTrainSql = "SELECT train_no FROM bookings WHERE pnr = ? AND status = 'Confirmed'";
		String cancelSql = "UPDATE bookings SET status = 'Cancelled' WHERE pnr = ?";
		String addSeatSql = "UPDATE trains SET seats = seats + 1 WHERE train_no = ?";
		try (Connection con = DBConnection.getConnection()) {
			con.setAutoCommit(false);
			try (PreparedStatement psFind = con.prepareStatement(findTrainSql)) {
				psFind.setLong(1, pnr);
				ResultSet rs = psFind.executeQuery();
				if (rs.next()) {
					int trainNo = rs.getInt("train_no");
					try (PreparedStatement psCancel = con.prepareStatement(cancelSql)) {
						psCancel.setLong(1, pnr);
						psCancel.executeUpdate();
					}
					try (PreparedStatement psAddSeat = con.prepareStatement(addSeatSql)) {
						psAddSeat.setInt(1, trainNo);
						psAddSeat.executeUpdate();
					}
					con.commit();
					return true;
				}
			} catch (SQLException e) {
				con.rollback();
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// PNR Check
	public Object[] checkPNRStatus(long pnr) {
		String sql = "SELECT b.pnr, b.train_no, b.seat_no, t.train_name, b.booking_date, b.status "
				+ "FROM bookings b JOIN trains t ON b.train_no = t.train_no WHERE b.pnr = ?";
		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, pnr);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return new Object[] { rs.getLong("pnr"), rs.getInt("train_no"), rs.getInt("seat_no"),
						rs.getString("train_name"), rs.getString("booking_date"), rs.getString("status") };
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void generateTicketFile(long pnr) {

		Object[] details = checkPNRStatus(pnr);

		if (details == null) {
			javax.swing.JOptionPane.showMessageDialog(null, "Error: PNR details not found.");
			return;
		}

		String fileName = "Ticket_PNR_" + pnr + ".txt";

		try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(fileName))) {
			writer.println("****************************************");
			writer.println("         RAILWAY SYSTEM RECEIPT         ");
			writer.println("****************************************");
			writer.println(" PNR NUMBER:    " + details[0]);
			writer.println(" TRAIN NO:      " + details[1]);
			writer.println(" SEAT NUMBER:   " + details[2]);
			writer.println(" TRAIN NAME:    " + details[3]);
			writer.println(" JOURNEY DATE:  " + details[4]);
			writer.println(" STATUS:        " + details[5]);
			writer.println("****************************************");
			writer.println(" Generated on:  " + new java.util.Date());
			writer.println("****************************************");
			writer.println("   Please carry a valid ID during travel. ");
			writer.println("****************************************");

			javax.swing.JOptionPane.showMessageDialog(null, "Ticket successfully saved as: " + fileName);
		} catch (java.io.IOException e) {
			javax.swing.JOptionPane.showMessageDialog(null, "File Error: Could not save ticket.");
			e.printStackTrace();
		}
	}

	public double getTotalRevenue() {
		String sql = "SELECT SUM(t.price) FROM bookings b JOIN trains t ON b.train_no = t.train_no WHERE b.status = 'Confirmed'";
		try (Connection con = DBConnection.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			if (rs.next())
				return rs.getDouble(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0.0;
	}

	public int getTotalBookings() {
		String sql = "SELECT COUNT(*) FROM bookings WHERE status = 'Confirmed'";
		try (Connection con = DBConnection.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			if (rs.next())
				return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<Integer> getBookedSeats(int trainNo) {
		List<Integer> bookedSeats = new ArrayList<>();
		String sql = "SELECT seat_no FROM bookings WHERE train_no = ? AND status = 'Confirmed'";
		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, trainNo);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				bookedSeats.add(rs.getInt("seat_no"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bookedSeats;
	}
}