package com.railway.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import com.railway.dao.Train_DAO;
import com.railway.model.Train;

public class AddTrainFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtNo, txtName, txtSrc, txtDest, txtPrice, txtSeats;
    private AdminDashboard parent; 

    public AddTrainFrame(AdminDashboard parent, String adminName) {
        this.parent = parent;
        setTitle("Admin - Add New Train");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 550);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel();
        contentPane.setBackground(new Color(245, 245, 245));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitle = new JLabel("ENTER TRAIN DETAILS");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(100, 20, 250, 30);
        contentPane.add(lblTitle);

        // UI Label and Field setup
        String[] labels = {"Train Number:", "Train Name:", "Source:", "Destination:", "Ticket Price:", "Total Seats:"};
        JTextField[] fields = {
            txtNo = new JTextField(), txtName = new JTextField(), 
            txtSrc = new JTextField(), txtDest = new JTextField(), 
            txtPrice = new JTextField(), txtSeats = new JTextField()
        };

        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lbl.setBounds(50, 70 + (i * 50), 120, 25);
            contentPane.add(lbl);
            
            fields[i].setBounds(180, 70 + (i * 50), 200, 30);
            fields[i].setFont(new Font("Segoe UI", Font.PLAIN, 14));
            contentPane.add(fields[i]);
        }

        JButton btnSave = new JButton("SAVE TRAIN");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(new Color(34, 139, 34)); 
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setBounds(135, 420, 180, 45);
        
        btnSave.addActionListener(e -> {
            saveTrainLogic();
        });
        contentPane.add(btnSave);
    }

    private void saveTrainLogic() {
        try {
            // Validation check for empty fields
            if (txtNo.getText().isEmpty() || txtName.getText().isEmpty() || txtSrc.getText().isEmpty() || 
                txtDest.getText().isEmpty() || txtPrice.getText().isEmpty() || txtSeats.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!");
                return;
            }

            // Create Train Object
            Train t = new Train(
                Integer.parseInt(txtNo.getText().trim()),
                txtName.getText().trim(),
                txtSrc.getText().trim(),
                txtDest.getText().trim(),
                Double.parseDouble(txtPrice.getText().trim()),
                Integer.parseInt(txtSeats.getText().trim())
            );

            if (new Train_DAO().addTrain(t)) {
                JOptionPane.showMessageDialog(this, "Train added successfully!");
                parent.loadTrains(); // Refresh the AdminDashboard table immediately
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error: Check if Train Number already exists.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter numeric values for Number, Price, and Seats.");
        }
    }
}