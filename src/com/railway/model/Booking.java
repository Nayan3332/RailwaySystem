package com.railway.model;
import java.sql.Date;


public class Booking {
	private int pnr;
	private int userId;
	private int trainNo;
	private String passengerName;
	private Date bookingDate;

	public Booking() {
	}
//field
	public Booking(int userId, int trainNo, String passengerName, Date bookingDate) {
		this.userId = userId;
		this.trainNo = trainNo;
		this.passengerName = passengerName;
		this.bookingDate = bookingDate;
	}
//getter and setter
	public int getPnr() {
		return pnr;
	}

	public void setPnr(int pnr) {
		this.pnr = pnr;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getTrainNo() {
		return trainNo;
	}

	public void setTrainNo(int trainNo) {
		this.trainNo = trainNo;
	}

	public String getPassengerName() {
		return passengerName;
	}

	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}

	public Date getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}
}