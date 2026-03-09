package com.railway.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
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
		setBounds(100, 100, 950, 730);
		setLocationRelativeTo(null);
		setResizable(false);

		contentPane = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				ImageIcon icon = new ImageIcon("src/assets/admin.jpg");
				if (icon.getIconWidth() > 0) {
					g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
				}
				g.setColor(new Color(240, 248, 255, 180));
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitle = new JLabel("RAILWAY ADMINISTRATION");
		lblTitle.setForeground(new Color(25, 25, 112));
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(200, 20, 550, 45);
		contentPane.add(lblTitle);

		JLabel lblWelcome = new JLabel("Welcome, Administrator " + adminName);
		lblWelcome.setForeground(new Color(70, 130, 180));
		lblWelcome.setFont(new Font("Segoe UI", Font.ITALIC, 18));
		lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcome.setBounds(200, 70, 550, 30);
		contentPane.add(lblWelcome);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(80, 130, 780, 300);
		contentPane.add(scrollPane);

		model = new DefaultTableModel(new String[] { "Train No", "Name", "Source", "Dest", "Price", "Seats" }, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(model);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		table.setRowHeight(30);
		table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
		scrollPane.setViewportView(table);

		addButton("ADD NEW TRAIN", 80, 460, 250, 50, new Color(153, 102, 153), e -> {
			new AddTrainFrame(this, adminName).setVisible(true);
		});

		addButton("UPDATE SELECTED", 345, 460, 250, 50, new Color(70, 130, 180), e -> {
			handleUpdate();
		});

		addButton("DELETE SELECTED", 610, 460, 250, 50, new Color(220, 20, 60), e -> {
			handleDelete();
		});

		addButton("BUSINESS REPORTS", 80, 530, 250, 50, new Color(255, 140, 0), e -> {
			new AdminReportFrame().setVisible(true);
		});

		addButton("TRACK LIVE TRAIN", 345, 530, 250, 50, new Color(0, 128, 128), e -> {
			new TrainTrackingFrame(adminName).setVisible(true);
			dispose(); 
		});

		addButton("MANAGE USERS", 610, 530, 250, 50, new Color(105, 105, 105), e -> {
			new ManageUsersFrame(adminName).setVisible(true);
			dispose(); 
		});

		JButton btnLogout = new JButton("LOGOUT");
		btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnLogout.setBackground(new Color(25, 25, 112));
		btnLogout.setForeground(Color.WHITE);
		btnLogout.setBounds(390, 620, 160, 40);
		btnLogout.addActionListener(e -> {
			new LoginFrame().setVisible(true);
			dispose(); 
		});
		contentPane.add(btnLogout);
	}

	private void addButton(String text, int x, int y, int w, int h, Color bg, java.awt.event.ActionListener al) {
		JButton btn = new JButton(text);
		btn.setBounds(x, y, w, h);
		btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btn.setBackground(bg);
		btn.setForeground(Color.WHITE);
		btn.setFocusPainted(false);
		btn.addActionListener(al);
		contentPane.add(btn);
	}

	private void handleUpdate() {
		int row = table.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "Please select a train from the list first!");
			return;
		}
		Train t = new Train();
		t.setTrainNo((int) table.getValueAt(row, 0));
		t.setTrainName((String) table.getValueAt(row, 1));
		t.setSource((String) table.getValueAt(row, 2));
		t.setDestination((String) table.getValueAt(row, 3));
		t.setPrice(Double.parseDouble(table.getValueAt(row, 4).toString()));
		t.setSeats(Integer.parseInt(table.getValueAt(row, 5).toString()));
		new UpdateTrainFrame(this, t).setVisible(true);
	}

	private void handleDelete() {
		int row = table.getSelectedRow();
		if (row != -1) {
			int trainNo = (int) table.getValueAt(row, 0);
			int confirm = JOptionPane.showConfirmDialog(this, "Permanently delete Train " + trainNo + "?", "Confirm",
					JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION && new Train_DAO().deleteTrain(trainNo)) {
				JOptionPane.showMessageDialog(this, "Train Deleted.");
				loadTrains();
			}
		} else {
			JOptionPane.showMessageDialog(this, "Select a train first.");
		}
	}

	public void loadTrains() {
		List<Train> list = new Train_DAO().getAllTrains();
		model.setRowCount(0);
		for (Train t : list) {
			model.addRow(new Object[] { t.getTrainNo(), t.getTrainName(), t.getSource(), t.getDestination(),
					t.getPrice(), t.getSeats() });
		}
	}
}