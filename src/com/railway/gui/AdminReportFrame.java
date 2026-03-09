package com.railway.gui;

import java.awt.*;
import javax.swing.*;
import com.railway.dao.Booking_DAO;

public class AdminReportFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	public AdminReportFrame() {
		setTitle("Admin - System Analytics & Business Reports");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		int width = 500;
		int height = 480;
		setBounds(100, 100, width, height);
		setLocationRelativeTo(null);
		setResizable(false);

		JPanel contentPane = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				// Load and scale background image
				ImageIcon icon = new ImageIcon("C:\\RailwaySystem\\src\\assets\\admin.jpg");
				if (icon.getIconWidth() > 0) {
					g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
				} else {
					g.setColor(new Color(240, 248, 255));
					g.fillRect(0, 0, getWidth(), getHeight());
				}
				g.setColor(new Color(255, 255, 255, 120));
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		contentPane.setLayout(null);
		setContentPane(contentPane);

		// Header Section
		JLabel lblTitle = new JLabel("SYSTEM ANALYTICS");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setForeground(new Color(25, 25, 112));
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
		lblTitle.setBounds(50, 25, 380, 40);
		contentPane.add(lblTitle);

		// Fetch Data
		Booking_DAO dao = new Booking_DAO();
		double revenue = dao.getTotalRevenue();
		int bookings = dao.getTotalBookings();

		JPanel pnlRevenue = createGlassCard(new Color(34, 139, 34));
		pnlRevenue.setBounds(50, 90, 380, 100);
		contentPane.add(pnlRevenue);

		JLabel lblRevTitle = new JLabel("Total Revenue Generated");
		lblRevTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblRevTitle.setForeground(new Color(60, 60, 60));
		lblRevTitle.setBounds(20, 15, 200, 20);
		pnlRevenue.add(lblRevTitle);

		JLabel lblRev = new JLabel("₹ " + String.format("%.2f", revenue));
		lblRev.setFont(new Font("Segoe UI", Font.BOLD, 22));
		lblRev.setForeground(new Color(34, 139, 34));
		lblRev.setBounds(20, 45, 340, 35);
		pnlRevenue.add(lblRev);

		JPanel pnlBookings = createGlassCard(new Color(70, 130, 180));
		pnlBookings.setBounds(50, 210, 380, 100);
		contentPane.add(pnlBookings);

		JLabel lblCountTitle = new JLabel("Confirmed Transactions");
		lblCountTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblCountTitle.setForeground(new Color(60, 60, 60));
		lblCountTitle.setBounds(20, 15, 200, 20);
		pnlBookings.add(lblCountTitle);

		JLabel lblCount = new JLabel(bookings + " Active Bookings");
		lblCount.setFont(new Font("Segoe UI", Font.BOLD, 22));
		lblCount.setForeground(new Color(70, 130, 180));
		lblCount.setBounds(20, 45, 340, 35);
		pnlBookings.add(lblCount);

		JButton btnClose = new JButton("CLOSE REPORT");
		btnClose.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnClose.setBackground(new Color(220, 20, 60));
		btnClose.setForeground(Color.WHITE);
		btnClose.setFocusPainted(false);
		btnClose.setBounds(160, 350, 160, 45);
		btnClose.addActionListener(e -> dispose());
		contentPane.add(btnClose);
	}

	private JPanel createGlassCard(Color accentColor) {
		JPanel card = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(new Color(255, 255, 255, 200));
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
				g2.dispose();
			}
		};
		card.setOpaque(false);
		card.setLayout(null);
		card.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, accentColor));
		return card;
	}
}