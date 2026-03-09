package com.railway.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.railway.dao.Booking_DAO;
import com.railway.dao.User_DAO;
import com.railway.model.User;

public class BookingHistoryFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private DefaultTableModel model;
	private String userName;

	public BookingHistoryFrame(String userName) {
		this.userName = userName;

		setTitle("Railway System - Personal Booking History");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 850, 550); 
		setLocationRelativeTo(null);

		JPanel contentPane = new JPanel();
		contentPane.setBackground(new Color(240, 248, 255)); 
		contentPane.setLayout(null);
		setContentPane(contentPane);

		
		JButton btnBack = new JButton("Dashboard");
		btnBack.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnBack.setBounds(20, 20, 110, 30);
		btnBack.addActionListener(e -> {
			new UserDashboard(userName).setVisible(true);
			dispose();
		});
		contentPane.add(btnBack);

		JLabel lblTitle = new JLabel("MY JOURNEY HISTORY");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
		lblTitle.setForeground(new Color(25, 25, 112));
		lblTitle.setBounds(250, 20, 350, 40);
		contentPane.add(lblTitle);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(50, 80, 730, 350);
		contentPane.add(scrollPane);

		model = new DefaultTableModel(new String[] { "PNR", "Train No", "Seat No", "Date", "Status" }, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		JTable table = new JTable(model);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		table.setRowHeight(30);
		table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
		scrollPane.setViewportView(table);

		// Double click 
		table.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if (e.getClickCount() == 2) {
					int row = table.getSelectedRow();
					if (row != -1) {
						String pnrValue = table.getValueAt(row, 0).toString();
						java.awt.datatransfer.StringSelection selection = new java.awt.datatransfer.StringSelection(
								pnrValue);
						java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
						JOptionPane.showMessageDialog(null, "PNR " + pnrValue + " copied to clipboard!");
					}
				}
			}
		});


		loadUserHistory();
	}

	private void loadUserHistory() {
		User userObj = new User_DAO().getUserByUsername(userName);
		if (userObj != null) {
			new Booking_DAO().loadHistory(userObj.getUserId(), model);
		} else {
			JOptionPane.showMessageDialog(this, "Error: User data not found.");
		}
	}
}