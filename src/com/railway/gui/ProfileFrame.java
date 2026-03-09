package com.railway.gui;

import java.awt.*;
import javax.swing.*;
import com.railway.dao.User_DAO;
import com.railway.model.User;

public class ProfileFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	public ProfileFrame(String userName) {
		setTitle("Railway System - User Profile");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 500);
		setLocationRelativeTo(null);
		setResizable(false);

		JPanel contentPane = new JPanel(null);
		contentPane.setBackground(new Color(240, 248, 255));
		setContentPane(contentPane);

		// Fetch user details
		User user = new User_DAO().getUserByUsername(userName);

		JLabel lblTitle = new JLabel("MY ACCOUNT PROFILE");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
		lblTitle.setForeground(new Color(25, 25, 112));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(75, 25, 300, 30);
		contentPane.add(lblTitle);

		// Info Panel 
		JPanel infoPanel = new JPanel(null);
		infoPanel.setBackground(Color.WHITE);
		infoPanel.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 1));
		infoPanel.setBounds(50, 75, 350, 100);
		contentPane.add(infoPanel);

		JLabel lblName = new JLabel("Full Name: " + (user != null ? user.getFullname() : "N/A"));
		lblName.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblName.setBounds(20, 20, 310, 25);
		infoPanel.add(lblName);

		JLabel lblUser = new JLabel("Username: " + userName);
		lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblUser.setForeground(Color.DARK_GRAY);
		lblUser.setBounds(20, 50, 310, 25);
		infoPanel.add(lblUser);

		JLabel lblSecurity = new JLabel("Security Settings");
		lblSecurity.setFont(new Font("Segoe UI", Font.BOLD, 16));
		lblSecurity.setBounds(50, 200, 200, 25);
		contentPane.add(lblSecurity);

		JLabel lblPass = new JLabel("Enter New Password:");
		lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblPass.setBounds(50, 230, 200, 20);
		contentPane.add(lblPass);

		JPasswordField txtPass = new JPasswordField();
		txtPass.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtPass.setBounds(50, 255, 350, 40);
		contentPane.add(txtPass);

		JButton btnUpdate = new JButton("UPDATE PASSWORD");
		// Theme Sync: Steel Blue for primary actions
		btnUpdate.setBackground(new Color(70, 130, 180));
		btnUpdate.setForeground(Color.WHITE);
		btnUpdate.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnUpdate.setBounds(100, 320, 250, 45);
		btnUpdate.setFocusPainted(false);

		btnUpdate.addActionListener(e -> {
			String newPass = new String(txtPass.getPassword());
			if (!newPass.trim().isEmpty()) {
				if (new User_DAO().updatePassword(user.getUserId(), newPass)) {
					JOptionPane.showMessageDialog(this, "Password updated successfully!");
					txtPass.setText("");
				}
			} else {
				JOptionPane.showMessageDialog(this, "Please enter a valid password.");
			}
		});
		contentPane.add(btnUpdate);

		JButton btnBack = new JButton("BACK TO DASHBOARD");
		btnBack.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnBack.setBackground(new Color(105, 105, 105)); 
		btnBack.setForeground(Color.WHITE);
		btnBack.setBounds(125, 390, 200, 35);
		btnBack.addActionListener(e -> {
			new UserDashboard(userName).setVisible(true);
			dispose();
		});
		contentPane.add(btnBack);
	}
}