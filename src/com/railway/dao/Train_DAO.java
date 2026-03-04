package com.railway.dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
			pst.setString(1, src);// city a
			pst.setString(2, dest);// to the city b.
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
}