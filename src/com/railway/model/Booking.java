package com.railway.model;

import java.sql.Date;

public class Booking {
	private long pnr; 
	private int userId;
	private int trainNo;
	private String passengerName;
	private Date bookingDate;
	private int seatNo; 

	public Booking() {
	}

	public Booking(int userId, int trainNo, String passengerName, Date bookingDate, int seatNo) {
		this.userId = userId;
		this.trainNo = trainNo;
		this.passengerName = passengerName;
		this.bookingDate = bookingDate;
		this.seatNo = seatNo;
	}

	// Getters and Setters
	public long getPnr() {
		return pnr;
	}

	public void setPnr(long pnr) {
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

	public int getSeatNo() {
		return seatNo;
	}

	public void setSeatNo(int seatNo) {
		this.seatNo = seatNo;
	}
}