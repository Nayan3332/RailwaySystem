package com.railway.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import com.railway.dao.Train_DAO;
import com.railway.model.Train;

public class AdminDashboard extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel model;
	private String adminName;

	public AdminDashboard(String adminName) {
		this.adminName = adminName;
		initComponents();
		loadTrains();
	}

	private void initComponents() {
		setTitle("Railway System - Admin Control Panel");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 925, 639); 
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(240, 248, 255)); 
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTitle = new JLabel("RAILWAY ADMINISTRATION ");
		lblTitle.setForeground(new Color(25, 25, 112));
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(200, 20, 525, 45);
		contentPane.add(lblTitle);
		
		JLabel lblWelcome = new JLabel("Welcome, Administrator " + adminName);
		lblWelcome.setForeground(new Color(70, 130, 180)); 
		lblWelcome.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcome.setBounds(200, 70, 525, 30);
		contentPane.add(lblWelcome);
		
		JPanel separator = new JPanel();
		separator.setBackground(new Color(70, 130, 180));
		separator.setBounds(150, 120, 625, 2);
		contentPane.add(separator);

		// DATA TABLE 

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(80, 150, 750, 280);
		contentPane.add(scrollPane);
		model = new DefaultTableModel(
		    new String[] { "Train No", "Name", "Source", "Dest", "Price", "Seats" }, 0
		) {
		    @Override
		    public boolean isCellEditable(int row, int column) {
		        return false; 
		    }
		};
		table = new JTable(model);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		table.setRowHeight(25);
		scrollPane.setViewportView(table);

		JButton btnAdd = new JButton("ADD NEW TRAIN");
		btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnAdd.setBackground(new Color(153, 102, 153)); 
		btnAdd.setForeground(Color.GREEN);
		btnAdd.setFocusPainted(false);
		btnAdd.setBounds(42, 458, 250, 50);
		btnAdd.addActionListener(e -> {
		    new AddTrainFrame(this, adminName).setVisible(true);
		});
		contentPane.add(btnAdd);
		JButton btnUpdate = new JButton("UPDATE SELECTED");
		btnUpdate.setBounds(331, 460, 250, 50); 
		btnUpdate.setFocusPainted(false);
		btnUpdate.addActionListener(e -> {
		    int row = table.getSelectedRow();
		    if (row == -1) {
		        JOptionPane.showMessageDialog(null, "Select a train first!");
		        return;
		    }
		    
		    Train t = new Train();
		    t.setTrainNo((int) table.getValueAt(row, 0));
		    t.setTrainName((String) table.getValueAt(row, 1));
		    t.setSource((String) table.getValueAt(row, 2));
		    t.setDestination((String) table.getValueAt(row, 3));
		    
		    // Using toString() in case of table stores it as a different object type
		    t.setPrice(Double.parseDouble(table.getValueAt(row, 4).toString())); 
		    t.setSeats(Integer.parseInt(table.getValueAt(row, 5).toString())); 
		    
		    new UpdateTrainFrame(this, t).setVisible(true);
		});
		contentPane.add(btnUpdate);

		JButton btnDelete = new JButton("DELETE SELECTED");
		btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnDelete.setBackground(new Color(220, 20, 60)); // Crimson Red
		btnDelete.setForeground(Color.WHITE);
		btnDelete.setFocusPainted(false);
		btnDelete.setBounds(608, 458, 250, 50);
		btnDelete.addActionListener(e -> {
		    int row = table.getSelectedRow();
		    if (row == -1) {
		        JOptionPane.showMessageDialog(null, "Please select a train from the list first!");
		        return;
		    }
		    
		    int trainNo = (int) table.getValueAt(row, 0);
		    int confirm = JOptionPane.showConfirmDialog(null, 
		        "Are you sure you want to delete Train " + trainNo + "?", 
		        "Delete Confirmation", 
		        JOptionPane.YES_NO_OPTION);      
		    if (confirm == JOptionPane.YES_OPTION) {
		        if (new Train_DAO().deleteTrain(trainNo)) {
		            JOptionPane.showMessageDialog(null, "Train deleted successfully!");
		            loadTrains();
		        }
		    }
		});
		contentPane.add(btnDelete);
		JButton btnLogout = new JButton("LOGOUT");
		btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnLogout.setBackground(new Color(105, 105, 105)); 
		btnLogout.setForeground(Color.BLACK);
		btnLogout.setFocusPainted(false);
		btnLogout.setBounds(370, 540, 165, 40);
		btnLogout.addActionListener(e -> {
			new LoginFrame().setVisible(true);
			dispose();
		});
		contentPane.add(btnLogout);
	}

	public void loadTrains() {
	    List<Train> list = new Train_DAO().getAllTrains();
	    model.setRowCount(0);
	    for (Train t : list) {
	        model.addRow(new Object[] {
	            t.getTrainNo(),      
	            t.getTrainName(),    
	            t.getSource(),       
	            t.getDestination(),  
	            t.getPrice(),        
	            t.getSeats()         
	        });
	    }
	}
}