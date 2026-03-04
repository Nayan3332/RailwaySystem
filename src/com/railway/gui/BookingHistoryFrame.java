package com.railway.gui;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.railway.dao.Booking_DAO;
public class BookingHistoryFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private DefaultTableModel model;
    public BookingHistoryFrame(String userName) {
        setTitle("My Bookings - " + userName);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 800, 500);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel();
        contentPane.setBackground(new Color(240, 248, 255)); 
        contentPane.setLayout(null);
        setContentPane(contentPane);

        // BACK
        JButton btnBack = new JButton("BACK");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBack.setBounds(20, 20, 80, 30);
        btnBack.addActionListener(e -> {
            new UserDashboard(userName).setVisible(true); 
            dispose(); 
        });
        contentPane.add(btnBack);

        JLabel lblTitle = new JLabel("MY BOOKING HISTORY");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setBounds(250, 20, 300, 40);
        contentPane.add(lblTitle);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(50, 80, 680, 300);
        contentPane.add(scrollPane);

        model = new DefaultTableModel(new String[] { "PNR", "Train No", "Date", "Status" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);
        scrollPane.setViewportView(table);

        new Booking_DAO().loadHistory(userName, model);
    }
}