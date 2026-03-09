package com.railway.gui;

import javax.swing.*;
import java.awt.*;

public class PaymentFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private Timer processingTimer;

	public PaymentFrame(double amount, Runnable onPaymentSuccess) {
		setTitle("IICS Secure Gateway - Unified Payments");
		setBounds(100, 100, 450, 480);
		setLocationRelativeTo(null);
		setResizable(false);

		JPanel mainPanel = new JPanel(null);
		mainPanel.setBackground(new Color(240, 248, 255));
		setContentPane(mainPanel);

		JLabel lblHeader = new JLabel("SECURE PAYMENT GATEWAY");
		lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeader.setForeground(new Color(25, 25, 112));
		lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 22));
		lblHeader.setBounds(50, 20, 350, 30);
		mainPanel.add(lblHeader);

		JLabel lblAmount = new JLabel("Total Payable: ₹" + String.format("%.2f", amount));
		lblAmount.setHorizontalAlignment(SwingConstants.CENTER);
		lblAmount.setForeground(new Color(220, 20, 60)); 
		lblAmount.setFont(new Font("Segoe UI", Font.BOLD, 16));
		lblAmount.setBounds(50, 55, 350, 25);
		mainPanel.add(lblAmount);

		//Payment Section
		JTabbedPane tabs = new JTabbedPane();
		tabs.setFont(new Font("Segoe UI", Font.BOLD, 12));
		tabs.setBounds(25, 100, 385, 220);
		mainPanel.add(tabs);


		// UPI 
		JPanel pnlUPI = new JPanel(null);
		pnlUPI.setBackground(Color.WHITE);

		JLabel lblUPITitle = new JLabel("Unified Payments Interface (UPI)");
		lblUPITitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblUPITitle.setBounds(20, 20, 250, 25);
		pnlUPI.add(lblUPITitle);

		JLabel lblUPIPrompt = new JLabel("Enter Virtual Private Address (VPA):");
		lblUPIPrompt.setForeground(Color.GRAY);
		lblUPIPrompt.setBounds(20, 50, 250, 20);
		pnlUPI.add(lblUPIPrompt);

		JTextField txtUPI = new JTextField("username@upi");
		txtUPI.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtUPI.setBounds(20, 80, 340, 40);
		pnlUPI.add(txtUPI);

		JLabel lblNote = new JLabel("Note: Open your UPI app to authorize the request.");
		lblNote.setFont(new Font("Segoe UI", Font.ITALIC, 11));
		lblNote.setBounds(20, 130, 340, 20);
		pnlUPI.add(lblNote);

		tabs.addTab("UPI / QR CODE", pnlUPI);

		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		progressBar.setBounds(50, 340, 350, 15);
		progressBar.setVisible(false);
		mainPanel.add(progressBar);

		JButton btnPay = new JButton("AUTHORIZE & PAY NOW");
		btnPay.setBackground(new Color(70, 130, 180));
		btnPay.setForeground(Color.WHITE);
		btnPay.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnPay.setFocusPainted(false);
		btnPay.setBounds(100, 370, 250, 50);

		btnPay.addActionListener(e -> {
			if (txtUPI.getText().trim().isEmpty() || txtUPI.getText().equals("username@upi")) {
				JOptionPane.showMessageDialog(this, "Please enter a valid UPI ID.");
				return;
			}

			btnPay.setEnabled(false);
			progressBar.setVisible(true);

			processingTimer = new Timer(2500, event -> {
				processingTimer.stop();
				String refNo = "TXN" + (long) (Math.random() * 1000000000L);
				JOptionPane.showMessageDialog(this, "Payment Successful!\nTransaction ID: " + refNo);
				onPaymentSuccess.run();
				dispose();
			});
			processingTimer.start();
		});
		mainPanel.add(btnPay);
	}
}