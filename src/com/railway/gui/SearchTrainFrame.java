package com.railway.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import com.railway.dao.Booking_DAO;
import com.railway.dao.Train_DAO;
import com.railway.dao.User_DAO;
import com.railway.model.Train;

public class SearchTrainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtSource, txtDestination;
	private JTable table;
	private DefaultTableModel model;
	private String loggedInUser;
	private JComboBox<String> comboCat;
	private JLabel statusLabel;

	public SearchTrainFrame(String userName) {
		this.loggedInUser = userName;

		setTitle("Railway System - Search & Book Journeys");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 950, 650);
		setLocationRelativeTo(null);

		contentPane = new JPanel();

		contentPane.setBackground(new Color(240, 248, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnBack = new JButton("Dashboard");
		btnBack.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnBack.setBounds(20, 20, 110, 30);
		btnBack.addActionListener(e -> {
			new UserDashboard(loggedInUser).setVisible(true);
			dispose();
		});
		contentPane.add(btnBack);

		JLabel lblTitle = new JLabel("TRAIN SCHEDULE & BOOKING");
		lblTitle.setForeground(new Color(25, 25, 112));
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(250, 15, 450, 40);
		contentPane.add(lblTitle);

		//SEARCH PANEL
		JPanel searchPanel = new JPanel();
		searchPanel.setBorder(BorderFactory.createTitledBorder("Search Filters"));
		searchPanel.setBackground(Color.WHITE);
		searchPanel.setBounds(60, 70, 810, 80);
		searchPanel.setLayout(null);
		contentPane.add(searchPanel);

		JLabel lblSrc = new JLabel("From:");
		lblSrc.setBounds(50, 30, 50, 25);
		searchPanel.add(lblSrc);

		txtSource = new JTextField();
		txtSource.setBounds(100, 30, 150, 25);
		searchPanel.add(txtSource);

		JLabel lblDest = new JLabel("To:");
		lblDest.setBounds(280, 30, 50, 25);
		searchPanel.add(lblDest);

		txtDestination = new JTextField();
		txtDestination.setBounds(310, 30, 150, 25);
		searchPanel.add(txtDestination);

		JButton btnSearch = new JButton("FIND TRAINS");
		btnSearch.setBackground(new Color(70, 130, 180)); 
		btnSearch.setForeground(Color.WHITE);
		btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnSearch.setBounds(480, 27, 140, 30);
		searchPanel.add(btnSearch);

		JButton btnShowAll = new JButton("CLEAR");
		btnShowAll.setBounds(630, 27, 110, 30);
		searchPanel.add(btnShowAll);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(60, 170, 810, 300);
		contentPane.add(scrollPane);

		model = new DefaultTableModel(new Object[][] {},
				new String[] { "Train No", "Train Name", "Source", "Destination", "Price (₹)", "Seats Available" }) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(model);
		table.setRowHeight(30);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(table);

		//CATEGORY & STATUS
		JLabel lblCat = new JLabel("Fare Category:");
		lblCat.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblCat.setBounds(60, 490, 120, 25);
		contentPane.add(lblCat);

		String[] categories = { "General", "Senior Citizen (40% Off)", "Student (50% Off)" };
		comboCat = new JComboBox<>(categories);
		comboCat.setBounds(180, 490, 220, 25);
		contentPane.add(comboCat);

		statusLabel = new JLabel("Ready to search.");
		statusLabel.setForeground(Color.GRAY);
		statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
		statusLabel.setBounds(60, 570, 400, 25);
		contentPane.add(statusLabel);

		JButton btnBookSelected = new JButton("CONFIRM & SELECT SEAT");
		btnBookSelected.setFont(new Font("Segoe UI", Font.BOLD, 16));
		btnBookSelected.setBackground(new Color(34, 139, 34));
		btnBookSelected.setForeground(Color.WHITE);
		btnBookSelected.setFocusPainted(false);
		btnBookSelected.setBounds(500, 490, 300, 50);
		contentPane.add(btnBookSelected);

		loadAllTrains();

		btnSearch.addActionListener(e -> filterTrains());
		btnShowAll.addActionListener(e -> loadAllTrains());
		btnBookSelected.addActionListener(e -> handleBooking());
	}

	private void loadAllTrains() {
		List<Train> list = new Train_DAO().getAllTrains();
		updateTable(list);
		statusLabel.setText("System: All active schedules loaded.");
		statusLabel.setForeground(Color.GRAY);
	}

	private void filterTrains() {
		String src = txtSource.getText().trim();
		String dest = txtDestination.getText().trim();
		if (src.isEmpty() || dest.isEmpty()) {
			statusLabel.setText("Error: Source and Destination required.");
			statusLabel.setForeground(Color.RED);
			return;
		}
		List<Train> list = new Train_DAO().searchTrains(src, dest);
		updateTable(list);
		statusLabel.setText("System: " + list.size() + " trains found for " + src + " to " + dest);
		statusLabel.setForeground(new Color(34, 139, 34));
	}

	private void updateTable(List<Train> list) {
		model.setRowCount(0);
		for (Train t : list) {
			model.addRow(new Object[] { t.getTrainNo(), t.getTrainName(), t.getSource(), t.getDestination(),
					t.getPrice(), t.getSeats() });
		}
	}

	private void handleBooking() {
		int row = table.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "Please select a train from the schedule!");
			return;
		}

		int trainNo = Integer.parseInt(table.getValueAt(row, 0).toString());
		double basePrice = Double.parseDouble(table.getValueAt(row, 4).toString());
		int totalSeats = Integer.parseInt(table.getValueAt(row, 5).toString());

		if (totalSeats <= 0) {
			JOptionPane.showMessageDialog(this, "No seats available on this train.");
			return;
		}

		String category = comboCat.getSelectedItem().toString();
		double tempPrice = basePrice;
		if (category.contains("Senior Citizen"))
			tempPrice = basePrice * 0.6;
		else if (category.contains("Student"))
			tempPrice = basePrice * 0.5;

		final double finalPrice = tempPrice;

		int confirm = JOptionPane.showConfirmDialog(this,
				"Confirm Booking for Train " + trainNo + "?\nFinal Price: ₹" + String.format("%.2f", finalPrice),
				"Booking Confirmation", JOptionPane.YES_NO_OPTION);

		if (confirm == JOptionPane.YES_OPTION) {
			new SeatSelectionFrame(trainNo, totalSeats, (seatNo) -> {
				com.railway.model.User userObj = new User_DAO().getUserByUsername(loggedInUser);
				if (userObj != null) {
					new PaymentFrame(finalPrice, () -> {
						Booking_DAO bDao = new Booking_DAO();
						if (bDao.bookTicket(userObj.getUserId(), trainNo, loggedInUser, seatNo)) {
							JOptionPane.showMessageDialog(this, "Success! PNR Generated for Seat #" + seatNo);
							loadAllTrains();
						} else {
							JOptionPane.showMessageDialog(this, "Booking Failed: Seat may have been taken.");
						}
					}).setVisible(true);
				}
			}).setVisible(true);
		}
	}
}