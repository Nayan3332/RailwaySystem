package com.railway.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.railway.dao.Booking_DAO;

public class UserDashboard extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private String userName;

	public UserDashboard(String userName) {
		this.userName = userName;
		initComponents();
	}

	private void initComponents() {
		setTitle("Railway System - Passenger Portal");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int width = 950, height = 650;
		setBounds(100, 100, width, height);
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
				g2.setColor(new Color(0, 0, 0, 180));
				g2.fillRect(0, 0, getWidth(), getHeight());
				g2.dispose();
				super.paintComponent(g);
			}
		};
		glassPanel.setOpaque(false); 
		glassPanel.setBounds(150, 50, 650, 520);
		glassPanel.setLayout(null);
		glassPanel.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 80), 1));
		contentPane.add(glassPanel);

		JLabel lblTitle = new JLabel("RAILWAY PASSENGER PORTAL");
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28)); 
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(0, 30, 650, 40);
		glassPanel.add(lblTitle);

		JLabel lblWelcome = new JLabel("Welcome, " + userName);
		lblWelcome.setForeground(new Color(200, 200, 200));
		lblWelcome.setFont(new Font("Segoe UI", Font.ITALIC, 16));
		lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcome.setBounds(0, 75, 650, 25);
		glassPanel.add(lblWelcome);

		addButton(glassPanel, "BOOK JOURNEY", 70, 160, 240, 55, new Color(70, 130, 180), e -> {
			new SearchTrainFrame(userName).setVisible(true);
			dispose();
		});

		addButton(glassPanel, "MY PROFILE", 340, 160, 240, 55, new Color(100, 149, 237), e -> {
			new ProfileFrame(userName).setVisible(true);
			dispose();
		});

		addButton(glassPanel, "BOOKING HISTORY", 70, 240, 240, 55, new Color(60, 179, 113), e -> {
			new BookingHistoryFrame(userName).setVisible(true);
			dispose();
		});

		addButton(glassPanel, "PNR ENQUIRY", 340, 240, 240, 55, new Color(255, 140, 0), e -> {
			handlePNRCheck();
		});

		addButton(glassPanel, "TRACK TRAIN", 70, 320, 240, 55, new Color(30, 144, 255), e -> {
			new TrainTrackingFrame(userName).setVisible(true);
			dispose();
		});

		addButton(glassPanel, "CANCEL BOOKING", 340, 320, 240, 55, new Color(220, 20, 60), e -> {
			handleCancellation();
		});

		// Logout
		addButton(glassPanel, "LOGOUT", 225, 430, 200, 45, new Color(105, 105, 105), e -> {
			int confirm = JOptionPane.showConfirmDialog(this, "Log out from the system?", "Confirmation",
					JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				new LoginFrame().setVisible(true);
				dispose();
			}
		});

		//Background Image
		JLabel lblBackground = new JLabel("");
		ImageIcon icon = new ImageIcon("src/assets/dash.png");
		if (icon.getIconWidth() > 0) {
			Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
			lblBackground.setIcon(new ImageIcon(scaled));
		} else {
			contentPane.setBackground(new Color(25, 25, 112)); 
		}
		lblBackground.setBounds(0, 0, width, height);
		contentPane.add(lblBackground);

		contentPane.setComponentZOrder(lblBackground, contentPane.getComponentCount() - 1);
	}

	private void addButton(JPanel panel, String text, int x, int y, int w, int h, Color bg, ActionListener al) {
		JButton btn = new JButton(text);
		btn.setBounds(x, y, w, h);
		btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btn.setBackground(bg);
		btn.setForeground(Color.WHITE);
		btn.setFocusPainted(false);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btn.setBorder(BorderFactory.createEmptyBorder()); 
		btn.addActionListener(al);
		panel.add(btn);
	}

	private void handlePNRCheck() {
		String pnrInput = JOptionPane.showInputDialog(this, "Enter 10-digit PNR Number:");
		if (pnrInput != null && !pnrInput.trim().isEmpty()) {
			try {
				long pnr = Long.parseLong(pnrInput.trim()); //10-digit PNR
				new PNRStatusFrame(userName, pnr).setVisible(true);
				dispose();
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "Error: Invalid PNR format. Numbers only.");
			}
		}
	}

	private void handleCancellation() {
		String pnrInput = JOptionPane.showInputDialog(this, "Enter PNR to Cancel Booking:");
		if (pnrInput != null && !pnrInput.trim().isEmpty()) {
			try {
				long pnr = Long.parseLong(pnrInput.trim());
				if (new Booking_DAO().cancelTicket(pnr)) {
					JOptionPane.showMessageDialog(this, "Success: Ticket has been cancelled.");
				} else {
					JOptionPane.showMessageDialog(this, "Notice: Invalid PNR or already cancelled.");
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "Error: Invalid PNR format.");
			}
		}
	}
}