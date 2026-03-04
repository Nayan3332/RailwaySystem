package com.railway.gui;

import javax.swing.*;
import java.awt.*;
import com.railway.dao.Train_DAO;
import com.railway.model.Train;

public class UpdateTrainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private AdminDashboard parent;

	private JTextField txtName, txtSrc, txtDest, txtPrice, txtSeats;
	private int trainNo;

	public UpdateTrainFrame(AdminDashboard parent, Train t) {
		this.parent = parent;

		this.trainNo = t.getTrainNo();
		setTitle("Admin - Update Train #" + trainNo);
		setBounds(100, 100, 400, 450);
		setLocationRelativeTo(null);

		JPanel panel = new JPanel(null);
		setContentPane(panel);

		JLabel lblTitle = new JLabel("UPDATE TRAIN DETAILS");
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblTitle.setBounds(100, 20, 250, 30);
		panel.add(lblTitle);

		txtName = new JTextField(t.getTrainName());
		txtSrc = new JTextField(t.getSource());
		txtDest = new JTextField(t.getDestination());
		txtPrice = new JTextField(String.valueOf(t.getPrice()));
		txtSeats = new JTextField(String.valueOf(t.getSeats()));

		String[] labels = { "Name:", "Source:", "Dest:", "Price:", "Seats:" };
		JTextField[] fields = { txtName, txtSrc, txtDest, txtPrice, txtSeats };

		for (int i = 0; i < labels.length; i++) {
			JLabel lbl = new JLabel(labels[i]);
			lbl.setBounds(50, 70 + (i * 40), 100, 25);
			panel.add(lbl);
			fields[i].setBounds(150, 70 + (i * 40), 180, 25);
			panel.add(fields[i]);
		}

		JButton btnUpdate = new JButton("UPDATE DATA");
		btnUpdate.setBounds(120, 320, 150, 40);
		btnUpdate.setBackground(new Color(70, 130, 180));
		btnUpdate.setForeground(Color.WHITE);
		btnUpdate.setFocusPainted(false);

		btnUpdate.addActionListener(e -> {
			try {
				Train updated = new Train(trainNo, txtName.getText(), txtSrc.getText(), txtDest.getText(),
						Double.parseDouble(txtPrice.getText()), Integer.parseInt(txtSeats.getText()));

				if (new Train_DAO().updateTrain(updated)) {
					JOptionPane.showMessageDialog(null, "Train Updated Successfully!");

					this.parent.loadTrains();

					dispose();
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Error: Invalid Input.");
			}
		});
		panel.add(btnUpdate);
	}
}