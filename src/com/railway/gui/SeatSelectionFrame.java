package com.railway.gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import com.railway.dao.Booking_DAO;

public class SeatSelectionFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private int selectedSeat = -1;

	public SeatSelectionFrame(int trainNo, int totalSeats, java.util.function.Consumer<Integer> onSeatSelected) {
		setTitle("Railway System - Seat Selection");
		setBounds(100, 100, 550, 650);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.setBackground(new Color(240, 248, 255));
		setContentPane(contentPane);

		JPanel headerPanel = new JPanel(new GridLayout(2, 1));
		headerPanel.setBackground(new Color(240, 248, 255));

		JLabel lblTitle = new JLabel("SELECT YOUR SEAT (Train #" + trainNo + ")");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
		lblTitle.setForeground(new Color(25, 25, 112)); 
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		headerPanel.add(lblTitle);

		JLabel lblLegend = new JLabel("RED = Already Booked | GREEN = Available for You");
		lblLegend.setFont(new Font("Segoe UI", Font.ITALIC, 14));
		lblLegend.setHorizontalAlignment(SwingConstants.CENTER);
		lblLegend.setForeground(Color.DARK_GRAY);
		headerPanel.add(lblLegend);

		contentPane.add(headerPanel, BorderLayout.NORTH);

		int rows = (int) Math.ceil(totalSeats / 4.0);
		JPanel gridPanel = new JPanel(new GridLayout(rows, 4, 15, 15));
		gridPanel.setBackground(Color.WHITE);
		gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

		// Fetching booked seats from DAO
		List<Integer> bookedSeats = new Booking_DAO().getBookedSeats(trainNo);

		for (int i = 1; i <= totalSeats; i++) {
			JButton btnSeat = new JButton(String.valueOf(i));
			btnSeat.setFont(new Font("Segoe UI", Font.BOLD, 14));
			btnSeat.setFocusPainted(false);

			if (bookedSeats.contains(i)) {
				btnSeat.setBackground(new Color(220, 20, 60)); // Crimson Red
				btnSeat.setForeground(Color.WHITE);
				btnSeat.setEnabled(false);
				btnSeat.setToolTipText("Seat already taken");
			} else {
				btnSeat.setBackground(new Color(34, 139, 34)); // Forest Green
				btnSeat.setForeground(Color.WHITE);
				int finalI = i;
				btnSeat.addActionListener(e -> {
					selectedSeat = finalI;
					int confirm = JOptionPane.showConfirmDialog(this,
							"Confirm selection of Seat #" + selectedSeat + "?", "Seat Confirmation",
							JOptionPane.YES_NO_OPTION);

					if (confirm == JOptionPane.YES_OPTION) {
						onSeatSelected.accept(selectedSeat);
						dispose();
					}
				});
			}
			gridPanel.add(btnSeat);
		}

		JScrollPane scrollPane = new JScrollPane(gridPanel);
		scrollPane.setBorder(null);
		contentPane.add(scrollPane, BorderLayout.CENTER);

		JPanel footerPanel = new JPanel();
		footerPanel.setBackground(new Color(240, 248, 255));
		JButton btnCancel = new JButton("CANCEL SELECTION");
		btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnCancel.setBackground(Color.GRAY);
		btnCancel.setForeground(Color.WHITE);
		btnCancel.addActionListener(e -> dispose());
		footerPanel.add(btnCancel);

		contentPane.add(footerPanel, BorderLayout.SOUTH);
	}
}