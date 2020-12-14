package view;
import view.ConsoleGUI;
import control.Controller;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Panel;
import java.awt.ScrollPane;
import javax.swing.JTextField;

//import com.sun.glass.ui.View;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.awt.event.ActionEvent;
import javax.swing.JSplitPane;
import java.awt.Window.Type;
import javax.swing.JPasswordField;
import javax.swing.JOptionPane.*;

public class ConnexionGUI  extends JFrame  {
	private static Controller control;
	private static ConsoleGUI console;
	
	private JTextField uti;
	private JPasswordField password;
	public ConnexionGUI() throws ParseException {
		
		
	
		
		setType(Type.POPUP);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		
		uti = new JTextField();
		uti.setBounds(239, 168, 175, 32);
		getContentPane().add(uti);
		uti.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Utilisateur");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(157, 171, 72, 23);
		getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Mot de passe");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(137, 228, 92, 23);
		getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setIcon(new ImageIcon("D:\\cours\\SLAM4\\VinciThermoGreen\\img\\Logo_Vinci_Concessions.gif"));
		lblNewLabel_2.setBounds(21, 26, 254, 77);
		getContentPane().add(lblNewLabel_2);
		
		/**
		 * Ce bouton gère la connexion en envoyant au controlleur le pseudo et mot de passe saisie par l'utilisateur 
		 */
		JButton btnNewButton = new JButton("Connexion");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
				boolean b = false;
				try {
			
					control = new Controller();
				
					b = control.ConnectUTI(uti.getText(), password.getText());
				if(b == true) {
				
					console = new ConsoleGUI();
				
					console.test();   
					control.setPseudo(uti.getText());
					suppAff();
				
				}
				else {
					System.out.println(control.hash(password.getText()));
					System.out.println("mauvais login");
					 JOptionPane.showMessageDialog(null,"Mauvais login");
				}
					
				} catch (Exception e) {
					 JOptionPane.showMessageDialog(null,"La BDD n'est pas branché");
				}
				
				
				
		
				
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnNewButton.setBounds(302, 266, 112, 32);
		getContentPane().add(btnNewButton);

		password = new JPasswordField();
		password.setBounds(239, 221, 175, 32);
		getContentPane().add(password);
	}
	
	
	/**
	 * Cette methode permet de fermer la fenetre de connexion
	 */
	public void suppAff() {
		this.setVisible(false);
	}
	

}
