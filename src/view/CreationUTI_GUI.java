package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;

import control.Controller;

import javax.swing.JPasswordField;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.awt.event.ActionEvent;

public class CreationUTI_GUI extends JFrame {
	private JTextField textField;
	private JPasswordField passwordField;
	private static Controller control;
	public CreationUTI_GUI() throws ParseException {
		control = new Controller();
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(90, 34, 251, 214);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Pseudo");
		lblNewLabel.setBounds(20, 40, 46, 14);
		panel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Password");
		lblNewLabel_1.setBounds(20, 90, 56, 14);
		panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("role");
		lblNewLabel_2.setBounds(37, 135, 29, 14);
		panel.add(lblNewLabel_2);
		
		textField = new JTextField();
		textField.setBounds(86, 37, 133, 20);
		panel.add(textField);
		textField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(86, 87, 133, 20);
		panel.add(passwordField);
		
		
		JButton btnNewButton_1 = new JButton("Retour");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				suppAff();
			}
		});
		btnNewButton_1.setBounds(20, 175, 74, 23);
		panel.add(btnNewButton_1);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(86, 132, 133, 20);
		comboBox.addItem("membre");
		comboBox.addItem("admin");
		panel.add(comboBox);
		
		/**
		 * Ce boutton permet d'envoyer les informations nécessaire au controleur pour la création d'un utilisateur
		 */
		JButton btnNewButton = new JButton("Valider");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String uti = textField.getText();
				String mdp = passwordField.getText();
				Boolean role = false;
				Boolean test = false;
				if(comboBox.getSelectedItem().toString().compareTo("admin")==0) {
					role = true;
					
				}
				try {
					if(test =control.createUti(uti, mdp, role)) {
					 JOptionPane.showMessageDialog(null,"l'utilisateur " + uti + "à étais créer");
					}
				} catch (SQLException e1) {
				
					
					
				}
			
			}
		});
		btnNewButton.setBounds(147, 175, 89, 23);
		panel.add(btnNewButton);
		
		JLabel lblNewLabel_3 = new JLabel("nouveau utilisateur");
		lblNewLabel_3.setBounds(0, 0, 161, 20);
		panel.add(lblNewLabel_3);
		
		
		
	}
	
	/**
	 * Cette methode permet de fermer la fenetre de creation d'utilisateur
	 */
	public void suppAff() {
		this.setVisible(false);
	}
}
