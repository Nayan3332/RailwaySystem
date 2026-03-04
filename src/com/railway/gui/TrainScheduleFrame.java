package com.railway.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import com.railway.dao.Train_DAO;
import com.railway.model.Train;
import java.util.List;

public class TrainScheduleFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel model;
	private String currentUserName;

	public TrainScheduleFrame(String userName) {
		this.currentUserName = userName;

		setTitle("Full Train Schedule");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 850, 500);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBackground(new Color(245, 245, 245));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Back
		JButton btnBack = new JButton("BACK");
		btnBack.setBounds(20, 20, 80, 30);
		btnBack.addActionListener(e -> {
			new UserDashboard(currentUserName).setVisible(true);
			dispose();
		});
		contentPane.add(btnBack);

		JLabel lblTitle = new JLabel("ALL AVAILABLE TRAINS");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
		lblTitle.setBounds(300, 20, 300, 40);
		contentPane.add(lblTitle);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(40, 80, 750, 330);
		contentPane.add(scrollPane);

		model = new DefaultTableModel(new String[] { "Train No", "Name", "Source", "Dest", "Price", "Seats" }, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(model);
		scrollPane.setViewportView(table);

		// Load Data 
		Train_DAO dao = new Train_DAO();
		List<Train> list = dao.getAllTrains();
		for (Train t : list) {
			model.addRow(new Object[] { t.getTrainNo(), t.getTrainName(), t.getSource(), t.getDestination(),
					t.getPrice(), t.getSeats() });
		}
	}
}