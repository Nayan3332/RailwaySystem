package com.railway.gui;

import java.awt.*;
import javax.swing.*;
import com.railway.dao.Booking_DAO;

public class PNRStatusFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	public PNRStatusFrame(String userName, long pnr) {
		setTitle("Railway System - PNR Status Inquiry");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 450);
		setLocationRelativeTo(null);

		JPanel contentPane = new JPanel();
		contentPane.setLayout(null);
		contentPane.setBackground(new Color(240, 248, 255)); 
		setContentPane(contentPane);

		JLabel lblTitle = new JLabel("PNR STATUS DETAILS");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblTitle.setForeground(new Color(25, 25, 112));
		lblTitle.setBounds(120, 20, 250, 30);
		contentPane.add(lblTitle);

		//ticket layout
		JTextArea resultArea = new JTextArea();
		resultArea.setEditable(false);
		resultArea.setFont(new Font("Monospaced", Font.BOLD, 14));
		resultArea.setBackground(Color.WHITE);
		resultArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		resultArea.setBounds(40, 70, 350, 220); 
		contentPane.add(resultArea);

		// Fetch  data from DAO
		Booking_DAO dao = new Booking_DAO();
		Object[] details = dao.checkPNRStatus(pnr);

		//Display Logic 
		if (details != null) {
			resultArea.setText("\n  ==============================\n" + "  PNR Number:   " + details[0] + "\n"
					+ "  Train No:     " + details[1] + "\n" + "  SEAT NUMBER:  " + details[2] + "\n" + // Index 2
																										// is now
																										// seat_no
					"  Train Name:   " + details[3] + "\n" + "  Date:         " + details[4] + "\n" + "  Status:       "
					+ details[5] + "\n" + "  ==============================");
		} else {
			resultArea.setText("\n  Error: No records found \n  for PNR: " + pnr);
		}

		JButton btnPrint = new JButton("PRINT TICKET");
		btnPrint.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnPrint.setBackground(new Color(34, 139, 34));
		btnPrint.setForeground(Color.WHITE);
		btnPrint.setBounds(231, 320, 159, 40);
		btnPrint.addActionListener(e -> {
			if (details != null) {
				new Booking_DAO().generateTicketFile(pnr);
			} else {
				JOptionPane.showMessageDialog(this, "No ticket data to print!");
			}
		});
		contentPane.add(btnPrint);

		JButton btnBack = new JButton("DASHBOARD");
		btnBack.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnBack.setBackground(new Color(70, 130, 180));
		btnBack.setForeground(Color.WHITE);
		btnBack.setBounds(40, 320, 159, 40);
		btnBack.addActionListener(e -> {
			new UserDashboard(userName).setVisible(true);
			dispose();
		});
		contentPane.add(btnBack);
	}
}