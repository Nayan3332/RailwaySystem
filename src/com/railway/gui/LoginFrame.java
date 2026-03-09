package com.railway.gui;

import java.awt.*;
import javax.swing.*;
import com.railway.dao.User_DAO;
import com.railway.model.User;
import com.railway.db.DatabaseSetup; // Import your setup logic

public class LoginFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsername;
	private JPasswordField txtPassword;

	public static void main(String[] args) {
		UIManager.put("Button.focus", new Color(0, 0, 0, 0));

		try {
			DatabaseSetup.main(null);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"CRITICAL ERROR: Could not connect to MySQL Server.\n" + "Please ensure XAMPP/WAMP is running.",
					"Database Connection Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		EventQueue.invokeLater(() -> {
			try {
				LoginFrame frame = new LoginFrame();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public LoginFrame() {
		setTitle("Railway System - Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		int frameWidth = 800;
		int frameHeight = 500;

		setBounds(100, 100, frameWidth, frameHeight);
		setLocationRelativeTo(null);
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
		glassPanel.setBounds(225, 50, 350, 360);
		glassPanel.setLayout(null);
		glassPanel.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
		contentPane.add(glassPanel);

		JLabel lblTitle = new JLabel(" LOGIN");
		lblTitle.setForeground(new Color(25, 25, 112));
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(0, 30, 350, 40);
		glassPanel.add(lblTitle);

		JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblUsername.setBounds(50, 100, 100, 25);
		glassPanel.add(lblUsername);

		txtUsername = new JTextField();
		txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtUsername.setBounds(50, 130, 250, 35);
		glassPanel.add(txtUsername);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblPassword.setBounds(50, 180, 100, 25);
		glassPanel.add(lblPassword);

		txtPassword = new JPasswordField();
		txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtPassword.setBounds(50, 210, 250, 35);
		glassPanel.add(txtPassword);

		JButton btnLogin = new JButton("LOGIN TO PORTAL");
		btnLogin.setBackground(new Color(70, 130, 180)); 
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnLogin.setFocusPainted(false);
		btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnLogin.setBounds(50, 270, 250, 45);

		btnLogin.addActionListener(e -> {
			String user = txtUsername.getText().trim();
			String pass = new String(txtPassword.getPassword()).trim();

			if (user.isEmpty() || pass.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please enter both credentials.");
				return;
			}

			User_DAO dao = new User_DAO();
			User loggedInUser = dao.loginUser(user, pass);

			if (loggedInUser != null) {
				JOptionPane.showMessageDialog(this, "Welcome " + loggedInUser.getFullname());
				if (loggedInUser.getRole().equalsIgnoreCase("admin")) {
					new AdminDashboard(loggedInUser.getUsername()).setVisible(true);
				} else {
					new UserDashboard(loggedInUser.getUsername()).setVisible(true);
				}
				dispose();
			} else {
				JOptionPane.showMessageDialog(this, "Access Denied: Invalid Credentials", "Login Error",
						JOptionPane.ERROR_MESSAGE);
			}
		});
		glassPanel.add(btnLogin);

		JButton btnSignUp = new JButton("New User? Register Your Account");
		btnSignUp.setBounds(50, 320, 250, 25);
		btnSignUp.setBorderPainted(false);
		btnSignUp.setContentAreaFilled(false);
		btnSignUp.setForeground(new Color(25, 25, 112));
		btnSignUp.setFont(new Font("Segoe UI", Font.ITALIC, 12));
		btnSignUp.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnSignUp.addActionListener(e -> {
			new RegistrationFrame().setVisible(true);
			dispose();
		});
		glassPanel.add(btnSignUp);

		//Background Styling 
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
}