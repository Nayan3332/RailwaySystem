package com.railway.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import com.railway.dao.User_DAO;
import com.railway.model.User;

public class ManageUsersFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private DefaultTableModel model;
	private User_DAO userDAO;

	public ManageUsersFrame() {
		this.userDAO = new User_DAO();

		setTitle("Admin - System User Management");
		setBounds(100, 100, 750, 500); 
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel contentPane = new JPanel(null);
		contentPane.setBackground(new Color(240, 248, 255));
		setContentPane(contentPane);

		JLabel lblTitle = new JLabel("SYSTEM USER ACCOUNTS");
		lblTitle.setForeground(new Color(25, 25, 112));
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(175, 20, 400, 35);
		contentPane.add(lblTitle);

		model = new DefaultTableModel(new String[] { "User ID", "Username", "Full Name", "Role" }, 0) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			} 
		};
		JTable table = new JTable(model);
		table.setRowHeight(30);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(50, 80, 650, 280);
		contentPane.add(scrollPane);

		loadUsers();

		JButton btnDelete = new JButton("REMOVE ACCOUNT");
		btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnDelete.setBackground(new Color(220, 20, 60));
		btnDelete.setForeground(Color.WHITE);
		btnDelete.setFocusPainted(false);
		btnDelete.setBounds(275, 385, 200, 45);

		btnDelete.addActionListener(e -> {
			int row = table.getSelectedRow();
			if (row != -1) {
				int id = (int) model.getValueAt(row, 0);

				//check to prevent accidental deletion of the primary admin account
				if (id == 1) {
					JOptionPane.showMessageDialog(this, "Security Alert: Cannot delete System Administrator!");
					return;
				}

				int confirm = JOptionPane.showConfirmDialog(this,
						"Are you sure you want to permanently remove this user?", "Confirm Deletion",
						JOptionPane.YES_NO_OPTION);

				if (confirm == JOptionPane.YES_OPTION) {
					if (userDAO.deleteUser(id)) { 
						JOptionPane.showMessageDialog(this, "User successfully removed.");
						loadUsers();
					}
				}
			} else {
				JOptionPane.showMessageDialog(this, "Please select a user record first.");
			}
		});
		contentPane.add(btnDelete);
	}

	private void loadUsers() {
		List<User> userList = userDAO.getAllUsers();
		model.setRowCount(0); 
		for (User u : userList) {
			model.addRow(new Object[] { u.getUserId(), u.getUsername(), u.getFullname(), u.getRole() });
		}
	}
}