package com.railway.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import com.railway.db.DBConnection;
import com.railway.model.Train;

public class Train_DAO {
	// Method that is use for admin to add a new train
	public boolean addTrain(Train train) {
		boolean success = false;
		String sql = "INSERT INTO trains (train_no, train_name, source, destination, price, seats) VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
			pst.setInt(1, train.getTrainNo());
			pst.setString(2, train.getTrainName());
			pst.setString(3, train.getSource());
			pst.setString(4, train.getDestination());
			pst.setDouble(5, train.getPrice());
			pst.setInt(6, train.getSeats());
			success = pst.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}

	// Method for search the trains
	public List<Train> searchTrains(String src, String dest) {
		List<Train> list = new ArrayList<>();
		String sql = "SELECT * FROM trains WHERE source = ? AND destination = ?";
		try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
			pst.setString(1, src);
			pst.setString(2, dest);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Train t = new Train();
				t.setTrainNo(rs.getInt("train_no"));
				t.setTrainName(rs.getString("train_name"));
				t.setSource(rs.getString("source"));
				t.setDestination(rs.getString("destination"));
				t.setPrice(rs.getDouble("price"));
				t.setSeats(rs.getInt("seats"));
				list.add(t);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Train> getAllTrains() {
		List<Train> list = new ArrayList<>();
		String sql = "SELECT * FROM trains";
		try (Connection con = DBConnection.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				Train t = new Train();
				t.setTrainNo(rs.getInt("train_no"));
				t.setTrainName(rs.getString("train_name"));
				t.setSource(rs.getString("source"));
				t.setDestination(rs.getString("destination"));
				t.setPrice(rs.getDouble("price"));
				t.setSeats(rs.getInt("seats"));
				list.add(t);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public boolean deleteTrain(int trainNo) {
		String sql = "DELETE FROM trains WHERE train_no = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

			pst.setInt(1, trainNo);
			return pst.executeUpdate() > 0;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean updateTrain(Train train) {
		String sql = "UPDATE trains SET train_name=?, source=?, destination=?, price=?, seats=? WHERE train_no=?";
		try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
			pst.setString(1, train.getTrainName());
			pst.setString(2, train.getSource());
			pst.setString(3, train.getDestination());
			pst.setDouble(4, train.getPrice());
			pst.setInt(5, train.getSeats());
			pst.setInt(6, train.getTrainNo());
			return pst.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// Method to update train tracking
	public boolean updateTrainTracking(int trainNo, String currentLocation, String status) {
		String sql = "INSERT INTO train_tracking (train_no, current_location, status) VALUES (?, ?, ?) "
				+ "ON DUPLICATE KEY UPDATE current_location = VALUES(current_location), status = VALUES(status)";
		try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
			pst.setInt(1, trainNo);
			pst.setString(2, currentLocation);
			pst.setString(3, status);
			return pst.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// Method to fetch tracking information
	public List<LinkedHashMap<String, Object>> getTrainTracking(int trainNo) {
		String sql = "SELECT * FROM train_tracking WHERE train_no = ?";
		List<LinkedHashMap<String, Object>> results = new ArrayList<>();
		try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {
			pst.setInt(1, trainNo);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				LinkedHashMap<String, Object> row = new LinkedHashMap<>();
				row.put("train_no", rs.getInt("train_no"));
				row.put("current_location", rs.getString("current_location"));
				row.put("status", rs.getString("status"));
				row.put("last_updated", rs.getTimestamp("last_updated"));
				results.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}

	public List<Object[]> getTrainTrackingList(int trainNo) {
		List<Object[]> trackingData = new ArrayList<>();

		String sql = "SELECT * FROM train_tracking WHERE train_no = ? ORDER BY last_updated DESC LIMIT 1";

		try (Connection con = DBConnection.getConnection(); PreparedStatement pst = con.prepareStatement(sql)) {

			pst.setInt(1, trainNo);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					trackingData.add(new Object[] { rs.getInt("train_no"), rs.getString("current_location"),
							rs.getString("status"), rs.getTimestamp("last_updated") });
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return trackingData;
	}
}