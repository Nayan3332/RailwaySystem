package com.railway.gui;

import java.awt.*;
import javax.swing.*;
import com.railway.dao.Booking_DAO;

public class PNRStatusFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    public PNRStatusFrame(String userName, int pnr) {
        setTitle("Check PNR Status");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 400);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(null);
        contentPane.setBackground(new Color(240, 248, 255));
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel("PNR STATUS DETAILS");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setBounds(120, 20, 200, 30);
        contentPane.add(lblTitle);

        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        resultArea.setBounds(40, 70, 350, 180);
        contentPane.add(resultArea);

        // Fetch data from DAO
        Booking_DAO dao = new Booking_DAO();
        Object[] details = dao.checkPNRStatus(pnr);

        if (details != null) {
            resultArea.setText(
                " PNR Number:    " + details[0] + "\n" +
                " Train No:      " + details[1] + "\n" +
                " Train Name:    " + details[2] + "\n" +
                " Date:          " + details[3] + "\n" +
                " Status:        " + details[4]
            );
        } else {
            resultArea.setText("No records found for PNR: " + pnr);
        }

        JButton btnBack = new JButton("BACK TO DASHBOARD");
        btnBack.setBounds(125, 280, 200, 35);
        btnBack.addActionListener(e -> {
            new UserDashboard(userName).setVisible(true);
            dispose();
        });
        contentPane.add(btnBack);
    }
}