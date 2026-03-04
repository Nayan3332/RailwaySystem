package com.railway.gui;

import com.railway.dao.User_DAO;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class ProfileFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private String currentUserName; 

    public ProfileFrame(String userName) {
        this.currentUserName = userName;
        
        setTitle("My Profile - " + userName);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel();
        contentPane.setBackground(new Color(245, 245, 245));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitle = new JLabel("USER PROFILE");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setBounds(125, 30, 200, 40);
        contentPane.add(lblTitle);

        JLabel lblUser = new JLabel("Logged in as: " + userName);
        lblUser.setHorizontalAlignment(SwingConstants.CENTER);
        lblUser.setBounds(125, 80, 200, 25);
        contentPane.add(lblUser);

        JButton btnChangePass = new JButton("UPDATE PASSWORD");
        btnChangePass.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnChangePass.setBounds(125, 130, 200, 45);
        btnChangePass.setBackground(new Color(153, 102, 153));
        btnChangePass.setForeground(Color.WHITE);
        
        btnChangePass.addActionListener(e -> {
            String newPass = JOptionPane.showInputDialog("Enter New Password:");
            if (newPass != null && !newPass.isEmpty()) {
                if (new User_DAO().updatePassword(currentUserName, newPass)) {
                    JOptionPane.showMessageDialog(null, "Password Updated Successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "Update Failed. Check Database connection.");
                }
            }
        });
        contentPane.add(btnChangePass);

        JButton btnBack = new JButton("BACK");
        btnBack.setBounds(175, 200, 100, 30);
        btnBack.addActionListener(e -> {
            // Checks if user is admin or regular user to go back to the right dashboard
            // For now, defaulting to UserDashboard
            new UserDashboard(currentUserName).setVisible(true);
            dispose();
        });
        contentPane.add(btnBack);
    }
}