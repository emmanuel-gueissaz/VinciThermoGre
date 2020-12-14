/**
 * @author Jérôme Valenti
 */
package view;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import com.mysql.fabric.xmlrpc.base.Array;
import com.sun.xml.internal.ws.org.objectweb.asm.Label;

import Base.BDD;
import control.Controller;
import view.CreationUTI_GUI;
import model.Mesure;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.Panel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

/**
 * <p>ConsoleGUI : IHM de l'application de consultation des températures</p>
 * <p>Projet Vinci Thermo Green</p>
 * @author Jérôme Valenti
 * @version 2.0.0
 * @see control.Controller
 * @see model.Mesure
 */
public class ConsoleGUI extends JFrame {
	
	private static Controller control;
	/**
	 * <p>Container intermédiaire JPanel</p>
	 * <p>Contient les critères de filtrage des données de la table</p>
	 * @see JPanel 
	 */
	JPanel pnlCriteria = new JPanel();

	/**
	 * <p>Bouton radio pour le choix de conversion</p>
	 */
	private static JRadioButton rdbtnCelsius = new JRadioButton("Celsius");
	JRadioButton rdbtnFahrenheit = new JRadioButton("Fahrenheit");

	/**
	 * <p>Liste de choix d'une zone</>
	 * @see JComboBox
	 */
	JComboBox<String> choixZone = new JComboBox<String>();
	
	/**
	 * <p>Saisie de la date de début de période</p>
	 * @see JTextField
	 */	
	private JTextField dateDebut;

	/**
	 * <p>Saisie de la date de fin de période</p>
	 * @see JTextField
	 */	
	private JTextField dateFin;
	
	private JButton btnFiltrer = new JButton("Filtrer");
	
	/**
	 * <p>Container intermédiaire JPanel</p>
	 * <p>Contient l'affichage graphique des données de la Table</p>
	 * @see JPanel 
	 */
	JPanel pnlParam = new JPanel();
	JPanel pnlGraph = new JPanel();

	/**
	 * <p>Affiche la température minimale sur la période</p>
	 * @see JTextField
	 */	
	private static JTextField tempMin;
	
	/**
	 * <p>Affiche la température moyenne sur la période</p>
	 * @see JTextField
	 */
	private static JTextField tempMoy;
	
	/**
	 * <p>Affiche la température maximale sur la période</p>
	 * @see JTextField
	 */
	private static JTextField tempMax;
	
	

	/**
	 * <p>Pour recevoir les données collectées</p>
	 * @see JTable 
	 */
	private static JTable laTable;
	
	/**
	 * <p>Un objet de la classe Mesure</p>
	 * @see model.Mesure
	 */
	private static Mesure uneMesure;
	
	/**
	 * <p>Pour recevoir les données collectées</p>
	 * @see ArrayList
	 * @see model.Mesure
	 */
	private static ArrayList<Mesure> lesMesures;

	/**
	 * <p>Pour recevoir le JTable qui contient les mesures selectionnées</p>
	 */

	private static ArrayList Stades;
	

	private static JScrollPane scrollPane = new JScrollPane();
	
	/**
	 * <p>Tableau d'objet pour alimenter la JTable</p>
	 */
	private static Object[][] data;
	
	private boolean admin;

	/**
	 * <p>Container intermédiaire JPanel</p>
	 * <p>Contient les bornes des valeurs nominales</p>
	 * @see JPanel 
	 */
	JPanel pnlBounds = new JPanel();
	
	private JLabel lbAlerte = new JLabel();
	


	public boolean isAdmin() {
		return admin;
	}


	public void setAdmin(boolean admin) {
		this.admin = admin;
	}


	public ConsoleGUI() throws ParseException {
		//Appelle le constructeur de la classe mère
		super();
		
		//control = new Controller();
		setIconImage(Toolkit.getDefaultToolkit().getImage("img\\vinci_ico.jpg"));
		setTitle("Vinci Thermo Green");
		setSize(712, 510);
		setResizable(false);
		setFont(new Font("Consolas", Font.PLAIN, 12));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		//Pane pointe sur le container racine
		Container pane = getContentPane();
		//Fixe le Layout de la racine à Absolute
		getContentPane().setLayout(null);
		
		//Définit le JPanel des critères
		pnlCriteria.setBounds(10, 96, 325, 145);
		pnlCriteria.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Filtrage", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		pnlCriteria.setBackground(UIManager.getColor("Label.background"));
		pnlCriteria.setLayout(null);
		pane.add(pnlCriteria);
		
		//Ajoute deux boutons radio au JPanel pnlCriteria
		rdbtnCelsius.setFont(new Font("Consolas", Font.PLAIN, 12));
		rdbtnCelsius.setBounds(15, 20, 100, 23);
		pnlCriteria.add(rdbtnCelsius);
		
		//Sélectionne la convertion celsius par défaut
		rdbtnCelsius.setSelected(true);
		
		rdbtnFahrenheit.setFont(new Font("Consolas", Font.PLAIN, 12));
		rdbtnFahrenheit.setBounds(115, 20, 100, 23);
		pnlCriteria.add(rdbtnFahrenheit);
		
	    //Groupe les boutons radio.
	    ButtonGroup group = new ButtonGroup();
	    group.add(rdbtnCelsius);
	    group.add(rdbtnFahrenheit);
		
		choixZone.setBounds(115, 50, 100, 20);
		pnlCriteria.add(choixZone);
		
		//un bouchon "Quick & Dirty" pour peupler la liste déroulante
		//TODO peupler la liste avec un équivalent à SELECT DISTINCT
		//TODO implémenter la classe métier Zone pour peupler une JComboBox<Zone>
		
		// a faire 	apres le choix du stade 
	
		
		JLabel lblZone = new JLabel("Zone");
		lblZone.setFont(new Font("Consolas", Font.PLAIN, 12));
		lblZone.setBounds(15, 54, 99, 14);
		pnlCriteria.add(lblZone);
		
		JLabel lblDebut = new JLabel("D\u00E9but");
		lblDebut.setFont(new Font("Consolas", Font.PLAIN, 12));
		lblDebut.setBounds(15, 83, 46, 14);
		pnlCriteria.add(lblDebut);
		
		dateDebut = new JTextField();
		dateDebut.setBounds(115, 79, 100, 20);
		pnlCriteria.add(dateDebut);
		dateDebut.setColumns(10);
		
		JLabel lblFin = new JLabel("Fin");
		lblFin.setFont(new Font("Consolas", Font.PLAIN, 12));
		lblFin.setBounds(15, 114, 46, 14);
		pnlCriteria.add(lblFin);
		
		dateFin = new JTextField();
		dateFin.setColumns(10);
		dateFin.setBounds(115, 110, 100, 20);
		pnlCriteria.add(dateFin);
		
		btnFiltrer.setBounds(225, 109, 89, 23);
		pnlCriteria.add(btnFiltrer);
		btnFiltrer.addActionListener(new filtrerData());
		
		JLabel lblLogoVinci = new JLabel();
		lblLogoVinci.setIcon(new ImageIcon("img\\s_vinci.png"));
		lblLogoVinci.setBounds(221, 11, 95, 35);
		pnlCriteria.add(lblLogoVinci);

		//Définit le JScrollPane qui reçoit la JTable
		scrollPane.setBounds(10, 252, 325, 218);
		pane.add(scrollPane);
		
		//Définit le JPanel des paramètres du graphique
		pnlParam.setBounds(345, 73, 355, 287);
		pnlParam.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Graphique des temp\u00E9ratures", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(128, 128, 128)));
		pnlParam.setBackground(UIManager.getColor("Label.background"));
		pnlParam.setLayout(null);
		pane.add(pnlParam);
		
		JCheckBox chckbxDistinctZone = new JCheckBox("Distinguer les zones");
		chckbxDistinctZone.setFont(new Font("Consolas", Font.PLAIN, 12));
		chckbxDistinctZone.setBounds(15, 20, 165, 23);
		pnlParam.add(chckbxDistinctZone);
		
		JLabel lblTypeDeGraphique = new JLabel("Type de graphique");
		lblTypeDeGraphique.setFont(new Font("Consolas", Font.PLAIN, 12));
		lblTypeDeGraphique.setBounds(15, 50, 120, 14);
		pnlParam.add(lblTypeDeGraphique);
		
		JComboBox choixGraphique = new JComboBox();
		choixGraphique.setBounds(152, 47, 190, 20);
		pnlParam.add(choixGraphique);
		
		JButton btnActualiser = new JButton("Actualiser");
		btnActualiser.setBounds(222, 19, 120, 23);
		pnlParam.add(btnActualiser);
		
		JLabel lblMin = new JLabel("Min");
		lblMin.setFont(new Font("Consolas", Font.PLAIN, 12));
		lblMin.setBounds(15, 306, 30, 14);
		pnlParam.add(lblMin);
		
		tempMin = new JTextField();
		tempMin.setEditable(false);
		tempMin.setBounds(55, 302, 50, 20);
		pnlParam.add(tempMin);
		tempMin.setColumns(10);
		
		JLabel lblMoy = new JLabel("Moy");
		lblMoy.setFont(new Font("Consolas", Font.PLAIN, 12));
		lblMoy.setBounds(137, 304, 30, 14);
		pnlParam.add(lblMoy);
		
		tempMoy = new JTextField();
		tempMoy.setEditable(false);
		tempMoy.setColumns(10);
		tempMoy.setBounds(177, 300, 50, 20);
		pnlParam.add(tempMoy);
		
		JLabel lblMax = new JLabel("Max");
		lblMax.setFont(new Font("Consolas", Font.PLAIN, 12));
		lblMax.setBounds(252, 304, 30, 14);
		pnlParam.add(lblMax);
		
		tempMax = new JTextField();
		tempMax.setEditable(false);
		tempMax.setColumns(10);
		tempMax.setBounds(292, 300, 50, 20);
		pnlParam.add(tempMax);
		
		//Définit le JPanel qui recoit le graphique
		pnlGraph.setBorder(new TitledBorder(null, "Graphique", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlGraph.setBackground(UIManager.getColor("Label.background"));
		pnlGraph.setBounds(15, 75, 330, 215);
		
		//pose le pnlGraph dans le pnlParam
		pnlParam.add(pnlGraph);
		pnlGraph.setLayout(null);
		
		//Définit le JPanel des bornes nominales
		pnlBounds.setBounds(340, 346, 355, 124);
		pnlBounds.setBorder(new TitledBorder(null, "D\u00E9bord des valeurs nominales", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		pnlBounds.setBackground(UIManager.getColor("Label.background"));
		pnlBounds.setLayout(null);
		pane.add(pnlBounds);
	
		//
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(15, 45, 35, 14);
		pnlBounds.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setBounds(15, 95, 35, 14);
		pnlBounds.add(lblNewLabel_1);
		
		// 
		
		JSlider slider = new JSlider();
		lblNewLabel.setText(Integer.toString(slider.getValue()));
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				 lblNewLabel.setText(Integer.toString(slider.getValue()));
			}
		});
		slider.setBounds(48, 40, 208, 25);
		pnlBounds.add(slider);
		
	
		
		JSlider slider_1 = new JSlider();
		lblNewLabel_1.setText(Integer.toString(slider_1.getValue()));
		slider_1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				lblNewLabel_1.setText(Integer.toString(slider_1.getValue()));
			}
		});
		slider_1.setBounds(48, 88, 207, 25);
		pnlBounds.add(slider_1);
		
		
	
		
		JLabel lblDebordMin = new JLabel("Minimum");
		lblDebordMin.setBounds(15, 20, 60, 14);
		pnlBounds.add(lblDebordMin);
		
		JLabel lblDebordMaximum = new JLabel("Maximum");
		lblDebordMaximum.setBounds(15, 70, 60, 14);
		pnlBounds.add(lblDebordMaximum);
		
	
		lbAlerte.setIcon(new ImageIcon("img\\s_green_button.png"));
		lbAlerte.setBounds(275, 26, 70, 59);
		pnlBounds.add(lbAlerte);
		

		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Stade", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 10, 316, 56);
		getContentPane().add(panel);
		
		JComboBox ChoixStade = new JComboBox();
		panel.add(ChoixStade);
		
		control = new Controller();
		Stades = control.LesStades();
		
		boolean temperature = control.lireBDD(Stades.get(0).toString());
		if(temperature==true) {
			changeRedIcon();
		}
		
		for (int i = 0; i < Stades.size(); i++) {
			ChoixStade.addItem(Stades.get(i));
		}
		
		JButton btnNewButton = new JButton("save");
		btnNewButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				
			int min = slider.getValue();
			int max = slider_1.getValue();
			if(min < max) {
			String stade_sel =  ChoixStade.getSelectedItem().toString();
			boolean bool = control.SaveMinMaxTemp(stade_sel, min, max);
			if( bool == true) {
				 JOptionPane.showMessageDialog(null,"Température sauvegardé");
			}
			else JOptionPane.showMessageDialog(null,"Problème lors de la sauvegarde de température");
			}
			else {
				JOptionPane.showMessageDialog(null,"la température min doit être supérieur au la température max");
			}}
		});
		btnNewButton.setBounds(275, 90, 70, 25);
		pnlBounds.add(btnNewButton);
		
		int a[]=control.GetMinMaxTemp(ChoixStade.getSelectedItem().toString());
		slider.setValue(a[0]);
		slider.setValue(a[1]);
		JButton ChoixStadeValide = new JButton("Valider");

		ChoixStadeValide.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// clear les zones 
				String nomStade = ChoixStade.getSelectedItem().toString();
				int a[]=control.GetMinMaxTemp(nomStade);
				slider.setValue(a[0]);
				slider_1.setValue(a[1]);
		
				boolean temperature = control.lireBDD(nomStade);
				if(temperature==true) {
					changeRedIcon();
					control.envoiSMS(nomStade);
				
				}
				else {
					changegreenIcon();
				}
				int nbZone = control.nb_zone(nomStade);
				
				choixZone.removeAllItems();
				choixZone.addItem("*");
				for (int i = 1; i < nbZone+1; i++) {
					String t =   Integer.toString(i);
					choixZone.addItem(t);
					
				}
		
				lesMesures = control.filtrerLesMesure(choixZone.getSelectedItem().toString());
		
				//System.out.println("test " + lesMesures.toString());

	        
				
				laTable = setTable(lesMesures);
	    		scrollPane.setViewportView(laTable);

			
	
			}
		});

		panel.add(ChoixStadeValide);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(345, 10, 351, 56);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		/**
		 * Bouton de creation d'un utiliseur permettant de faire afficher une nouvelle frame
		 */
		JButton create_uti = new JButton("creer utilisateur");
		System.out.println(this.isAdmin());
	
			create_uti.setBounds(35, 16, 154, 29);
			panel_1.add(create_uti);
	
			JButton deconnexion = new JButton("deconnexion");
			deconnexion.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					suppAff();
					try {
					
						control.main(null);
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				
				}
			});
			deconnexion.setHorizontalAlignment(SwingConstants.RIGHT);
			deconnexion.setBounds(221, 16, 115, 29);
			panel_1.add(deconnexion);
		//}
		create_uti.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					creatuti();
				} catch (ParseException e1) {
					
					
				}
			}
		});
		
		

	}
	
	public void controllerC () throws ParseException {
	control = new Controller();
	}


	public static void creatuti() throws ParseException {
		CreationUTI_GUI uti = new CreationUTI_GUI();
		uti.setLocation(400,400);
		uti.setSize(400, 400);
		uti.setVisible(true);
		
	}
	
	
	
	public void changeRedIcon() {
		lbAlerte.setIcon(new ImageIcon("img\\s_red_button.png"));
			
	}
	
	public void changegreenIcon() {
		lbAlerte.setIcon(new ImageIcon("img\\s_green_button.png"));
			
	}
	
	/**
	 * cette methode permet de creer la frame de consoleGUI
	 * @throws ParseException
	 */
	public static void test() throws ParseException {
		//Construit et affiche l'IHM
		
		
		
				ConsoleGUI monIHM = new ConsoleGUI();
				monIHM.setAdmin(true);
					
				
				
				monIHM.setLocation(100,100);
				
				monIHM.setSize(730, 530);
				//Instancie un contrôleur pour prendre en charge l'IHM
				//control = new Controller();
				
				

				//Demande l'acquisition des data
//				uneMesure = new Mesure();
				lesMesures = control.getLesMesures();
						
				//Construit le tableau d'objet
				laTable = setTable(lesMesures);
				
				
				//Definit le JScrollPane qui va recevoir la JTable
				scrollPane.setViewportView(laTable);
				
				System.out.println("Before set chart in main()");
				//affiche le graphique
				monIHM.setChart();
				System.out.println("After set chart in main()");
				
				
				monIHM.setVisible(true);


	}
	
	

	
	/**
	 * <p>Transfert les données de la collection vers un tableau d'objets</p>
	 * <p>La température est en degré Fahrenheit</p>
	 * 
	 * @param ArrayList<Mesure>
	 * @return Object[][]
	 */
	private static JTable setTable(ArrayList<Mesure> mesures) {
		
		float min = 0;
		float max = 0;
		float moy = 0;
		DecimalFormat round = new DecimalFormat("0.##");
		Object[][] dataTable = new Object[mesures.size()][3];

		if (rdbtnCelsius.isSelected()) {

			System.out.println("Celsius : " + rdbtnCelsius.isSelected() + " | " + mesures.size());

			// Initialisation de min et max
			min = mesures.get(0).getCelsius();
			max = mesures.get(0).getCelsius();
					for (int i = 0; i < mesures.size(); i++) {

				uneMesure = lesMesures.get(i);
				dataTable[i][0] = uneMesure.getNumZone();
				dataTable[i][1] = uneMesure.getHoroDate();
				dataTable[i][2] = round.format(uneMesure.getCelsius());
			
		
				// Min, max et moy
				moy = moy + uneMesure.getCelsius();

				if (uneMesure.getCelsius() < min) {
					min = uneMesure.getCelsius();
				}
				if (uneMesure.getCelsius() > max) {
					max = uneMesure.getCelsius();
				}
			}
		} else {

			System.out.println("Celsius : " + rdbtnCelsius.isSelected() + " | " + mesures.size());

			// Initialisation de min et max
			min = mesures.get(0).getFahrenheit();
			max = mesures.get(0).getFahrenheit();

			for (int i = 0; i < mesures.size(); i++) {
				uneMesure = lesMesures.get(i);
				dataTable[i][0] = uneMesure.getNumZone();
				dataTable[i][1] = uneMesure.getHoroDate();
				dataTable[i][2] = round.format(uneMesure.getFahrenheit());
			

				// Min, max et moy
				moy = moy + uneMesure.getFahrenheit();

				if (uneMesure.getFahrenheit() < min) {
					min = uneMesure.getFahrenheit();
				}
				if (uneMesure.getCelsius() > max) {
					max = uneMesure.getFahrenheit();
				}
			}
		}

		String[] titreColonnes = { "Zone", "Date-heure", "T°" };
		JTable uneTable = new JTable(dataTable, titreColonnes);
		// Les données de la JTable ne sont pas modifiables
		uneTable.setEnabled(false);
		

		// Arroundi et affecte les zones texte min, max et moy
		tempMin.setText(round.format(min));
		tempMax.setText(round.format(max));
		moy = moy / mesures.size();
		tempMoy.setText(round.format(moy));

		return uneTable;
	}

	//TODO factoriser le code avec setTable
	//TODO gérer le choix du graphique
	/**
	 * <p>Impl&eacute;mente la biblioth&egrave;que JFreeChart :</p>
	 * <ol>
	 * <li>d&eacute;finit le type de container de donn&eacute;es -&gt; DefaultCategoryDataset</li>
	 * <li>alimente le container des donn&eacute;es</li>
	 * <li>Fabrique un graphique lin&eacute;aire -&gt; ChartFactory.createLineChart</li>
	 * <li>Englobe le graphique dans un panel sp&eacute;cifique -&gt; new ChartPanel(chart)</li>
	 * <li>Englobe ce panel dans un JPanle de l'IHM -&gt; pnlGraph.add(chartPanel)<br /></li>
	 * </ol>
	 * @author Jérôme Valenti
	 * @see JFreeChart
	 */
	public void setChart (){
		
		int i1 = 0,i2 = 0,i3 = 0,i4 = 0;
		DefaultCategoryDataset dataChart = new DefaultCategoryDataset();
		
		// Set data ((Number)temp,zone,dateHeure)
		for (int i = 0; i < lesMesures.size(); i++) {

			uneMesure = lesMesures.get(i);

			switch (uneMesure.getNumZone()) {
			case "1":
				dataChart.addValue((Number)uneMesure.getCelsius(),uneMesure.getNumZone(),i1);
				i1++;
				break;
			case "2":
				dataChart.addValue((Number)uneMesure.getCelsius(),uneMesure.getNumZone(),i2);
				i2++;
				break;
			case "3":
				dataChart.addValue((Number)uneMesure.getCelsius(),uneMesure.getNumZone(),i3);
				i3++;
				break;
			case "4":
				dataChart.addValue((Number)uneMesure.getCelsius(),uneMesure.getNumZone(),i4);
				i4++;
				break;
			default:
				break;
			}
		}

		//un bouchon pour tester
//		 Set data ((Number)temp,zone,dateHeure)
//        dataChart.addValue((Number)1.0, "01", 1);
//        dataChart.addValue((Number)5.0, "02", 1);
//        dataChart.addValue((Number)4.0, "01", 2);
//        dataChart.addValue((Number)7.0, "02", 2);
//        dataChart.addValue((Number)3.0, "01", 3);
//        dataChart.addValue((Number)6.0, "02", 3);
//        dataChart.addValue((Number)5.0, "01", 4);
//        dataChart.addValue((Number)8.0, "02", 4);
//        dataChart.addValue((Number)5.0, "01", 5);
//        dataChart.addValue((Number)4.0, "02", 5);
//        dataChart.addValue((Number)7.0, "01", 6);
//        dataChart.addValue((Number)4.0, "02", 6);
//        dataChart.addValue((Number)7.0, "01", 7);
//        dataChart.addValue((Number)2.0, "02", 7);
//        dataChart.addValue((Number)8.0, "01", 8);
//        dataChart.addValue((Number)1.0, "02", 8);
	System.out.println(dataChart.getRowCount() + " lignes " + dataChart.getColumnCount() + " colonnes");
//		
		JFreeChart chart = ChartFactory.createLineChart(
                null,       				// chart title
                "Heure",					// domain axis label
                "Températures",				// range axis label
                dataChart,					// data
                PlotOrientation.VERTICAL,	// orientation
                true,						// include legend
                true,						// tooltips
                false						// urls
            );
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBounds(5, 20, 320, 190);
        chartPanel.setVisible(true);
        pnlGraph.add(chartPanel);
		System.out.println("chartPanel added to pnlGraph");
	}
	/**
	 * <p>Classe interne qui gère le clique sur le bouton filtrer
	 * @author Jérôme Valenti
	 */
	class filtrerData implements ActionListener {
        public void actionPerformed(ActionEvent e){
        	
        	ArrayList<Mesure> selction = new ArrayList();
     

        	lesMesures = control.filtrerLesMesure(choixZone.getSelectedItem().toString());
        	
        	System.out.println("Filtrer Celsius : " + rdbtnCelsius.isSelected() + 
        				" Fahrenheit : " + rdbtnFahrenheit.isSelected() + 
        				" choix : " + choixZone.getSelectedItem() + 
        				" début : " + dateDebut.getText() );
        	displayLesMesures(lesMesures);
    
        	//Construit le tableau d'objet
        	
    		laTable = setTable(lesMesures);
    		
    		//Definit le JScrollPane qui va recevoir la JTable
    		scrollPane.setViewportView(laTable);
    		
    		System.out.println("Before setChart in filtrerData()");
    		//affiche le graphique
    		setChart();
    		System.out.println("After setChart in filtrerData()");
        }
	}
	
	/**
	 * Cette methode permet de fermer la fenetre de consoleGUI
	 */
	public void suppAff() {
		this.setVisible(false);
	}
	
	private void displayLesMesures(ArrayList<Mesure> uneCollection){

		for (int i = 0; i < uneCollection.size(); i++) {
        	System.out.println(i + " " + uneCollection.get(i).getNumZone() + " | " 
        					+ uneCollection.get(i).getHoroDate() + " | " 
        					+ uneCollection.get(i).getCelsius() );
		} 
	}
}