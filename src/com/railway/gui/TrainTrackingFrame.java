package com.railway.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import com.railway.dao.Train_DAO;
import com.railway.dao.User_DAO; 
import com.railway.model.User;    

public class TrainTrackingFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField trainNoField;
    private JLabel statusLabel;
    private DefaultTableModel tableModel;
    private String loggedInUser;

    public TrainTrackingFrame(String userName) {
        this.loggedInUser = userName;
        
        setTitle("Railway System -  Train Tracking Portal");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false); 
        
        JPanel contentPane = new JPanel(null);
        contentPane.setBackground(new Color(240, 248, 255)); 
        setContentPane(contentPane);

        JButton btnBack = new JButton("Dashboard");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBack.setBounds(20, 20, 110, 30);
        btnBack.addActionListener(e -> {
            // 1. Fetch user details to determine where to go
            User u = new User_DAO().getUserByUsername(loggedInUser);
            
            if (u != null && "admin".equalsIgnoreCase(u.getRole())) {
                new AdminDashboard(loggedInUser).setVisible(true);
            } else {
                new UserDashboard(loggedInUser).setVisible(true);
            }
            
            TrainTrackingFrame.this.setVisible(false);
            TrainTrackingFrame.this.dispose();       
        });
        contentPane.add(btnBack);

        JLabel lblHeader = new JLabel("TRAIN STATUS TRACKER");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblHeader.setForeground(new Color(25, 25, 112)); 
        lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
        lblHeader.setBounds(150, 15, 500, 40);
        contentPane.add(lblHeader);

        //Search/Track Panel
        JPanel searchPanel = new JPanel(null);
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createTitledBorder("Tracking Filters")); 
        searchPanel.setBounds(50, 70, 680, 85);
        contentPane.add(searchPanel);

        JLabel lblTrainNo = new JLabel("Train Number:");
        lblTrainNo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTrainNo.setBounds(65, 33, 160, 25);
        searchPanel.add(lblTrainNo);

        trainNoField = new JTextField();
        trainNoField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        trainNoField.setBounds(190, 30, 150, 30);
        searchPanel.add(trainNoField);

        JButton trackButton = new JButton("LOCATE TRAIN NOW");
        trackButton.setBackground(new Color(70, 130, 180)); 
        trackButton.setForeground(Color.WHITE);
        trackButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        trackButton.setFocusPainted(false);
        trackButton.setBounds(360, 28, 220, 35);
        trackButton.addActionListener(e -> trackTrain());
        searchPanel.add(trackButton);

        //Result Table
        tableModel = new DefaultTableModel(new String[]{"Train No", "Current Location", "Journey Status", "Last Updated"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; } 
        };
        JTable trackingTable = new JTable(tableModel);
        trackingTable.setRowHeight(35); 
        trackingTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        trackingTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(trackingTable);
        scrollPane.setBounds(50, 175, 680, 270);
        contentPane.add(scrollPane);

        statusLabel = new JLabel("System Status: Ready to locate live train data.");
        statusLabel.setForeground(Color.GRAY);
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        statusLabel.setBounds(50, 465, 500, 25);
        contentPane.add(statusLabel);
    }

    private void trackTrain() {
        String trainNoStr = trainNoField.getText().trim();
        if (trainNoStr.isEmpty()) {
            statusLabel.setText("Warning: Please enter a numeric train number.");
            statusLabel.setForeground(Color.RED);
            return;
        }

        tableModel.setRowCount(0); 

        try {
            int trainNo = Integer.parseInt(trainNoStr);
            Train_DAO dao = new Train_DAO();
            
            //Fetch only the latest record
            List<Object[]> results = dao.getTrainTrackingList(trainNo);

            if (!results.isEmpty()) {
                for (Object[] row : results) {
                    tableModel.addRow(row); 
                }
                statusLabel.setText("Success: Tracking data retrieved successfully.");
                statusLabel.setForeground(new Color(34, 139, 34)); 
            } else {
                statusLabel.setText("Notice: No live tracking records found for Train #" + trainNo);
                statusLabel.setForeground(Color.RED);
            }
        } catch (NumberFormatException ex) {
            statusLabel.setText("Error: Train number must contain only numeric digits.");
            statusLabel.setForeground(Color.RED);
        } catch (Exception ex) {
            ex.printStackTrace();
            statusLabel.setText("Critical Error: Database communication failure.");
            statusLabel.setForeground(Color.RED);
        }
    }
}