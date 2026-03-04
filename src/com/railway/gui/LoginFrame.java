package com.railway.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image; // Added for scaling
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.railway.dao.User_DAO;

public class LoginFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsername;
	private JPasswordField txtPassword;

	public static void main(String[] args) {
		UIManager.put("Button.focus", new Color(0, 0, 0, 0));
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
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		int width = 724;
		int height = 435;

		setBounds(100, 100, width, height);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// --- UI COMPONENTS ---
		JLabel lblTitle = new JLabel("RAILWAY LOGIN");
		lblTitle.setForeground(Color.BLACK);
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 22));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(267, 20, 190, 46);
		contentPane.add(lblTitle);

		JLabel lblUsername = new JLabel("Username");
		lblUsername.setForeground(Color.BLACK);
		lblUsername.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblUsername.setBounds(267, 80, 77, 24);
		contentPane.add(lblUsername);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setForeground(Color.BLACK);
		lblPassword.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblPassword.setBounds(267, 120, 77, 24);
		contentPane.add(lblPassword);

		txtUsername = new JTextField();
		txtUsername.setBounds(348, 80, 120, 25);
		contentPane.add(txtUsername);

		txtPassword = new JPasswordField();
		txtPassword.setBounds(348, 120, 120, 25);
		contentPane.add(txtPassword);

		JButton btnLogin = new JButton("LOGIN");
		btnLogin.setBackground(new Color(60, 179, 113));
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnLogin.addActionListener(e -> {
			String user = txtUsername.getText().trim();
			String pass = new String(txtPassword.getPassword()).trim();

			if (user.isEmpty() || pass.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Please enter both Username and Password.");
				return;
			}

			User_DAO dao = new User_DAO();
			com.railway.model.User loggedInUser = dao.loginUser(user, pass);

			if (loggedInUser != null) {
				JOptionPane.showMessageDialog(null, "Login Successful! Welcome " + loggedInUser.getFullname());
				if (loggedInUser.getRole().equalsIgnoreCase("admin")) {
					new AdminDashboard(loggedInUser.getUsername()).setVisible(true);
				} else {
					new UserDashboard(loggedInUser.getUsername()).setVisible(true);
				}
				dispose();
			} else {
				JOptionPane.showMessageDialog(null, "Invalid Username or Password!", "Login Error",
						JOptionPane.ERROR_MESSAGE);
			}
		});
		btnLogin.setBounds(308, 175, 100, 35);
		contentPane.add(btnLogin);

		JButton btnSignUp = new JButton("No Account? Register Here");
		btnSignUp.setBounds(255, 230, 210, 25);
		btnSignUp.setBorderPainted(false);
		btnSignUp.setContentAreaFilled(false);
		btnSignUp.setForeground(Color.BLUE);
		btnSignUp.setFocusPainted(false);
		btnSignUp.addActionListener(e -> {
			new RegistrationFrame().setVisible(true);
			dispose();
		});
		contentPane.add(btnSignUp);

		JLabel lblBackground = new JLabel("");
		ImageIcon rawIcon = new ImageIcon("src/assets/1.jpg");
		Image scaledImg = rawIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
		lblBackground.setIcon(new ImageIcon(scaledImg));
		lblBackground.setBounds(0, 0, width, height);
		contentPane.add(lblBackground);

		contentPane.setComponentZOrder(lblBackground, contentPane.getComponentCount() - 1);
	}
}