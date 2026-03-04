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
        setTitle("Railway System - Register");
        
        int width = 400;
        int height = 480;
        
        setBounds(100, 100, width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel lblTitle = new JLabel("CREATE NEW ACCOUNT");
        lblTitle.setForeground(Color.BLACK); 
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setBounds(85, 30, 250, 30);
        contentPane.add(lblTitle);

        // --- UI Setup ---
        JLabel lblUser = new JLabel("Username:"); 
        lblUser.setForeground(Color.BLACK);
        lblUser.setBounds(50, 90, 100, 25);
        contentPane.add(lblUser);
        
        txtUser = new JTextField(); 
        txtUser.setBounds(160, 90, 150, 25);
        contentPane.add(txtUser);

        JLabel lblFull = new JLabel("Full Name:"); 
        lblFull.setForeground(Color.BLACK);
        lblFull.setBounds(50, 140, 100, 25);
        contentPane.add(lblFull);
        
        txtFullname = new JTextField(); 
        txtFullname.setBounds(160, 140, 150, 25);
        contentPane.add(txtFullname);

        JLabel lblPass = new JLabel("Password:"); 
        lblPass.setForeground(Color.BLACK);
        lblPass.setBounds(50, 190, 100, 25);
        contentPane.add(lblPass);
        
        txtPass = new JPasswordField(); 
        txtPass.setBounds(160, 190, 150, 25);
        contentPane.add(txtPass);

        JLabel lblConf = new JLabel("Confirm:"); 
        lblConf.setForeground(Color.BLACK);
        lblConf.setBounds(50, 240, 100, 25);
        contentPane.add(lblConf);
        
        txtConfirm = new JPasswordField(); 
        txtConfirm.setBounds(160, 240, 150, 25);
        contentPane.add(txtConfirm);

        //REGISTER BUTTON
        JButton btnReg = new JButton("REGISTER");
        btnReg.setBounds(130, 300, 120, 35);
        btnReg.setBackground(new Color(60, 179, 113));
        btnReg.setForeground(Color.WHITE);
        btnReg.setFocusPainted(false);
        btnReg.addActionListener(e -> registerUserLogic());
        contentPane.add(btnReg);

        //LOGIN FRAME
        JButton btnBack = new JButton("Already have an account? Login");
        btnBack.setBounds(85, 350, 220, 25);
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setForeground(Color.BLUE); // Brighter color for visibility
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        contentPane.add(btnBack);

        //BACKGROUND IMAGE 
        JLabel lblBackground = new JLabel("");
        ImageIcon rawIcon = new ImageIcon("src/assets/1.jpg");
        Image scaledImg = rawIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        lblBackground.setIcon(new ImageIcon(scaledImg));
        lblBackground.setBounds(0, 0, width, height);
        contentPane.add(lblBackground);
        contentPane.setComponentZOrder(lblBackground, contentPane.getComponentCount() - 1);
    }
    private void registerUserLogic() {
        String u = txtUser.getText().trim();
        String full = txtFullname.getText().trim();
        String p = new String(txtPass.getPassword());
        String cp = new String(txtConfirm.getPassword());

        if(u.isEmpty() || full.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fields cannot be empty!");
        } else if(!p.equals(cp)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!");
        } else {
            User newUser = new User();
            newUser.setUsername(u);
            newUser.setFullname(full);
            newUser.setPassword(p);
            newUser.setRole("user");

            if(new User_DAO().registerUser(newUser)) {
                JOptionPane.showMessageDialog(this, "Account Created! Please Login.");
                new LoginFrame().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Username already taken.");
            }
        }
    }
}