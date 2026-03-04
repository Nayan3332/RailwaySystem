package com.railway.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.util.List; // Added missing import
import com.railway.dao.Booking_DAO;
import com.railway.dao.Train_DAO; // Added missing import
import com.railway.model.Train; // Added missing import

public class SearchTrainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtSource, txtDestination;
	private JTable table;
	private DefaultTableModel model;
	private String loggedInUser;

	public SearchTrainFrame(String userName) {
		this.loggedInUser = userName;

		setTitle("Railway System - Welcome " + userName);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBackground(new Color(245, 245, 245));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JButton btnBack = new JButton("BACK");
		btnBack.setBounds(20, 20, 80, 30);
		btnBack.addActionListener(e -> {
			new UserDashboard(loggedInUser).setVisible(true);
			dispose();
		});
		contentPane.add(btnBack);

		JLabel lblTitle = new JLabel("FIND & BOOK TRAINS");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
		lblTitle.setBounds(300, 20, 300, 40);
		contentPane.add(lblTitle);

		// Input Fields
		txtSource = new JTextField();
		txtSource.setBounds(160, 90, 150, 25);
		contentPane.add(txtSource);

		txtDestination = new JTextField();
		txtDestination.setBounds(460, 90, 150, 25);
		contentPane.add(txtDestination);

		JButton btnSearch = new JButton("SEARCH");
		btnSearch.setBounds(650, 88, 120, 30);
		contentPane.add(btnSearch);

		// Table
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(60, 150, 770, 300);
		contentPane.add(scrollPane);

		model = new DefaultTableModel(new Object[][] {},
				new String[] { "Train No", "Train Name", "Source", "Destination", "Price", "Seats" }) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // This disables editing for all cells
			}
		};
		table = new JTable(model);
		scrollPane.setViewportView(table);

		JButton btnBookSelected = new JButton("BOOK SELECTED TRAIN");
		btnBookSelected.setBackground(new Color(50, 205, 50));
		btnBookSelected.setForeground(Color.WHITE);
		btnBookSelected.setBounds(330, 480, 250, 45);
		contentPane.add(btnBookSelected);

		//Search
		btnSearch.addActionListener(e -> {
			String src = txtSource.getText();
			String dest = txtDestination.getText();
			Train_DAO dao = new Train_DAO();
			List<Train> list = dao.searchTrains(src, dest);
			model.setRowCount(0);
			for (Train t : list) {
				model.addRow(new Object[] { t.getTrainNo(), t.getTrainName(), t.getSource(), t.getDestination(),
						t.getPrice(), t.getSeats() });
			}
		});

		// Booking
		btnBookSelected.addActionListener(e -> {
			int row = table.getSelectedRow();
			if (row == -1) {
				JOptionPane.showMessageDialog(null, "Please select a train first!");
				return;
			}
			int trainNo = Integer.parseInt(table.getValueAt(row, 0).toString());
			int userId = 1;

			Booking_DAO bDao = new Booking_DAO();
			if (bDao.bookTicket(userId, trainNo, loggedInUser)) {
				JOptionPane.showMessageDialog(null, "Booking Successful! PNR Generated.");
				btnSearch.doClick(); // Refresh table
			} else {
				JOptionPane.showMessageDialog(null, "Booking Failed! No seats available.");
			}
		});
	}
}