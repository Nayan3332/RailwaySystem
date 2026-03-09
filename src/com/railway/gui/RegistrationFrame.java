package com.railway.gui;

import javax.swing.*;
import java.awt.*;
import com.railway.dao.User_DAO;
import com.railway.model.User;

public class RegistrationFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField txtUser, txtFullname;
	private JPasswordField txtPass, txtConfirm;
	private JPanel contentPane;

	public RegistrationFrame() {
		setTitle("Railway System - Account Registration");

		int frameWidth = 776;
		int frameHeight = 590;

		setBounds(100, 100, frameWidth, frameHeight);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		contentPane = new JPanel();
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JPanel glassPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(new Color(255, 255, 255, 90));
				g2.fillRect(0, 0, getWidth(), getHeight());
				g2.dispose();
				super.paintComponent(g);
			}
		};
		glassPanel.setOpaque(false); 
		glassPanel.setBounds(217, 41, 350, 440);
		glassPanel.setLayout(null);
		glassPanel.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
		contentPane.add(glassPanel);

		JLabel lblTitle = new JLabel("CREATE NEW ACCOUNT");
		lblTitle.setForeground(new Color(25, 25, 112));
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(0, 20, 350, 30);
		glassPanel.add(lblTitle);

		// Username
		JLabel lblUser = new JLabel("Username");
		lblUser.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblUser.setBounds(40, 70, 100, 25);
		glassPanel.add(lblUser);
		txtUser = new JTextField();
		txtUser.setBounds(40, 95, 270, 35);
		glassPanel.add(txtUser);

		// Full Name
		JLabel lblFull = new JLabel("Full Name");
		lblFull.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblFull.setBounds(40, 140, 100, 25);
		glassPanel.add(lblFull);
		txtFullname = new JTextField();
		txtFullname.setBounds(40, 165, 270, 35);
		glassPanel.add(txtFullname);

		// Password
		JLabel lblPass = new JLabel("Password");
		lblPass.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblPass.setBounds(40, 210, 100, 25);
		glassPanel.add(lblPass);
		txtPass = new JPasswordField();
		txtPass.setBounds(40, 235, 270, 35);
		glassPanel.add(txtPass);

		// Confirm
		JLabel lblConf = new JLabel("Confirm Password");
		lblConf.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblConf.setBounds(40, 280, 150, 25);
		glassPanel.add(lblConf);
		txtConfirm = new JPasswordField();
		txtConfirm.setBounds(40, 305, 270, 35);
		glassPanel.add(txtConfirm);

		// Register Button
		JButton btnReg = new JButton("REGISTER NOW");
		btnReg.setBounds(40, 360, 270, 45);
		btnReg.setBackground(new Color(34, 139, 34));
		btnReg.setForeground(Color.WHITE);
		btnReg.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnReg.addActionListener(e -> registerUserLogic());
		glassPanel.add(btnReg);

		JButton btnBack = new JButton("Already have an account? Login");
		btnBack.setBounds(40, 410, 270, 25);
		btnBack.setBorderPainted(false);
		btnBack.setContentAreaFilled(false);
		btnBack.setForeground(new Color(25, 25, 112));
		btnBack.setFont(new Font("Segoe UI", Font.ITALIC, 12));
		btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnBack.addActionListener(e -> {
			new LoginFrame().setVisible(true);
			dispose();
		});
		glassPanel.add(btnBack);

		JLabel lblBackground = new JLabel("");
		ImageIcon rawIcon = new ImageIcon("src/assets/1.jpg");
		if (rawIcon.getIconWidth() > 0) {
			Image scaledImg = rawIcon.getImage().getScaledInstance(frameWidth, frameHeight, Image.SCALE_SMOOTH);
			lblBackground.setIcon(new ImageIcon(scaledImg));
		} else {
			contentPane.setBackground(new Color(240, 248, 255));
		}
		lblBackground.setBounds(0, 0, frameWidth, frameHeight);
		contentPane.add(lblBackground);

		contentPane.setComponentZOrder(lblBackground, contentPane.getComponentCount() - 1);
	}

	private void registerUserLogic() {
		String u = txtUser.getText().trim();
		String full = txtFullname.getText().trim();
		String p = new String(txtPass.getPassword());
		String cp = new String(txtConfirm.getPassword());

		if (u.isEmpty() || full.isEmpty() || p.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Validation Error: All fields are required.");
		} else if (!p.equals(cp)) {
			JOptionPane.showMessageDialog(this, "Validation Error: Passwords do not match.");
		} else {
			User newUser = new User();
			newUser.setUsername(u);
			newUser.setFullname(full);
			newUser.setPassword(p);
			newUser.setRole("user");

			if (new User_DAO().registerUser(newUser)) {
				JOptionPane.showMessageDialog(this, "Success: Account created!");
				new LoginFrame().setVisible(true);
				dispose();
			} else {
				JOptionPane.showMessageDialog(this, "Error: Username already taken.");
			}
		}
	}
}