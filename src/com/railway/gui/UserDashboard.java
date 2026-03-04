package com.railway.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.railway.dao.Booking_DAO;

public class UserDashboard extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private String userName;

    
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UserDashboard frame = new UserDashboard("Guest User");
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public UserDashboard(String userName) {
        this.userName = userName;
        initComponents();
    }

    private void initComponents() {
        setTitle("Railway System - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int width = 950, height = 650;
        setBounds(100, 100, width, height);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);

        //Container for UI black Glass
        JPanel glassPanel = new JPanel();
        glassPanel.setBackground(new Color(0, 0, 0, 160)); 
        glassPanel.setBounds(150, 50, 650, 520);
        glassPanel.setLayout(null);
        contentPane.add(glassPanel);

        JLabel lblTitle = new JLabel("RAILWAY PASSENGER PORTAL");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(0, 30, 650, 40);
        glassPanel.add(lblTitle);

        JLabel lblWelcome = new JLabel("Welcome, " + userName);
        lblWelcome.setForeground(new Color(220, 220, 220));
        lblWelcome.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
        lblWelcome.setBounds(0, 70, 650, 25);
        glassPanel.add(lblWelcome);
        addButton(glassPanel, "BOOK TICKET", 70, 150, 240, 50, new Color(72, 52, 117), e -> {
            new SearchTrainFrame(userName).setVisible(true);
            dispose();
        });

        addButton(glassPanel, "MY PROFILE", 340, 150, 240, 50, new Color(52, 101, 117), e -> {
            new ProfileFrame(userName).setVisible(true);
            dispose();
        });

        //Bookings & PNR
        addButton(glassPanel, "MY BOOKINGS", 70, 220, 240, 50, new Color(52, 73, 117), e -> {
            new BookingHistoryFrame(userName).setVisible(true);
            dispose();
        });

        addButton(glassPanel, "CHECK PNR", 340, 220, 240, 50, new Color(117, 85, 52), e -> {
            handlePNRCheck();
        });

        // CANCEL TICKET
        addButton(glassPanel, "CANCEL TICKET", 205, 290, 240, 50, new Color(150, 40, 40), e -> {
            handleCancellation();
        });

        //LOGOUT
        addButton(glassPanel, "LOGOUT", 225, 420, 200, 40, Color.DARK_GRAY, e -> {
            
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to Logout?", 
                                                        "Logout Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new LoginFrame().setVisible(true);
                dispose();
            }
        });

        //BACKGROUND IMAGE
        JLabel lblBackground = new JLabel("");
        ImageIcon icon = new ImageIcon("src/assets/dash.png");
        Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        lblBackground.setIcon(new ImageIcon(scaled));
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
        btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        btn.addActionListener(al);
        panel.add(btn);
    }

    private void handlePNRCheck() {
        String pnrInput = JOptionPane.showInputDialog(this, "Enter PNR Number:");
        if (pnrInput != null && !pnrInput.trim().isEmpty()) {
            try {
                int pnr = Integer.parseInt(pnrInput.trim());
                new PNRStatusFrame(userName, pnr).setVisible(true);
                dispose();
            } catch (Exception e) { JOptionPane.showMessageDialog(this, "Invalid PNR."); }
        }
    }

    private void handleCancellation() {
        String pnrInput = JOptionPane.showInputDialog(this, "Enter PNR to Cancel:");
        if (pnrInput != null && !pnrInput.trim().isEmpty()) {
            try {
                if (new Booking_DAO().cancelTicket(Integer.parseInt(pnrInput.trim()))) {
                    JOptionPane.showMessageDialog(this, "Ticket Cancelled Successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid PNR or already cancelled.");
                }
            } catch (Exception e) { JOptionPane.showMessageDialog(this, "Error in cancellation."); }
        }
    }
}