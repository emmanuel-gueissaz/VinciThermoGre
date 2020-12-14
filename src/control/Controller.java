/**
 * @author Jérôme Valenti 
 */
package control;


import Base.*;

import com.mysql.jdbc.Statement;



import java.io.BufferedReader;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.SQLException;

import model.Mesure;
import view.ConnexionGUI;
import view.ConsoleGUI;



import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
/**
 * <p>
 * Le cont&ocirc;lleur :
 * </p>
 * <ol>
 * <li>lit les mesures de température dans un fichier texte</li>
 * <li>retourne la collection des mesures<br />
 * </li>
 * </ol>
 * 
 * @author Jérôme Valenti
 * @version 2.0.0
 *
 */
public class Controller {

	BDD BDD = new BDD();
	private String pseudo;
	private int min;
	private int max;
	public static final String ACCOUNT_SID = "AC5a619eb1fab0c6557039396ff4768684";
	  public static final String AUTH_TOKEN = "5ba2dd3ed51d90b8245a04f8fc763eb8";
	
	
	
	
	

	/**
	 * <p>
	 * Les mesures lues dans le fichier des relevés de températures
	 * </p>
	 */
	
	
	
	private ArrayList<Mesure> lesMesures = new ArrayList<Mesure>();

	

	public String getPseudo() {
		return pseudo;
	}


	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}


	public Controller() throws ParseException {
	





	//	lireCSV("data\\mesures.csv");
	}
	
	
	/**
	 * permet d'initialer l'application en lançant la page de connexion
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args)  throws ParseException {
	
		ConnexionGUI connexion = new ConnexionGUI();
		
	
		 connexion.setLocation(200,200);
		 connexion.setSize(600, 600);
		 connexion.setVisible(true);
		 
		 
		 
	
	}
	
	
	
	


	/**
	 * Renvoie a la vue la liste des stades
	 * @return ArrayList
	 */
	public ArrayList LesStades() {
		ArrayList requete = new ArrayList();	
		requete = BDD.AFF_All_Stade();
		System.out.println(requete.toString());
		return requete;
		
	}
	

	/**
	 * <p>Lit un fichier de type CSV (Comma Separated Values)</p>
	 * <p>Le fichier contient les mesures de temp&eacute;rature de la pelouse.</p>
	 * 
	 * @author Jérôme Valenti
	 * @return
	 * @throws ParseException
	 * @since 2.0.0
	 */
	
	/**
	 * Met a jour la liste de mesures qui sera affiché dans la vue
	 * 
	 * @param stade
	 */
	public Boolean lireBDD(String stade) {
		boolean bool = false;
		ArrayList<Mesure> requete = new ArrayList();
		requete = BDD.requete_select(stade);
		lesMesures.clear();
		for (int i = 0; i < requete.size(); i++) {
		
		
			if(requete.get(i).getFahrenheit()>max || requete.get(i).getFahrenheit()<min) {
				bool = true;
				requete.get(i).setNotGood(true);
	
			}
			System.out.println(requete.get(i));
			lesMesures.add(requete.get(i));
		}
	
		return bool;
	}

	/**
	 * renvoie a la vue le nombre de zone que possède le stade sélectionné
	 * 
	 * @param stade
	 * @return
	 */
public int nb_zone(String stade) {
	int zone = BDD.NB_Zone_Stade(stade);
	return zone;
}

/**
 * Cette methode permet a l'utilisateur de ce connecter avec un identifiant et un mot de passe crypter
 * @param uti
 * @param mdp
 * @return true si la saisie correspond à un utilisateur en BDD
 */
public boolean ConnectUTI(String uti, String mdp) {

	boolean bool = BDD.connectUTI(uti, mdp);


	return bool;
}




public boolean SaveMinMaxTemp (String stade, int min, int max) {
	boolean bool = BDD.SaveMinMaxTemp(stade, min, max);

	return bool;
	
}

 

public int[] GetMinMaxTemp (String stade) {
	int[] a = BDD.getMaxMinTemp(stade);
	min = a[0];
	max = a[1];

	return a;
	
}


public String GetTelStade(String stade) {
	String tel = BDD.getTel(stade);
	System.out.println("test une forsse merde");
	return tel;
}

public void envoiSMS(String stade) {
	String tel = BDD.getTel(stade);
	System.out.println(tel);
	SMS(tel, stade);
	
}

public void SMS (String tel, String stade) {
	   Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

	    Message message = Message.creator(new PhoneNumber("+33"+tel),
	        new PhoneNumber("+12513366851"), 
	        "Votre stade " + stade + " possède une température problématique").create();

	    System.out.println(message.getSid());
	  
}

//public boolean verifAdmin(String admin) {
//   boolean ok = BDD.verifAdmin(admin);
//   return ok;
//}

/**
 * Cette méthonde permet de hash (crypter) le mot de passe 
 * @param pwd
 * @return String - mot de passe crypter
 */
public String hash(String pwd) {
	
	return BCrypt.hashpw(pwd, BCrypt.gensalt());


}

/**
 * Cette methode permet de creer utilisateur en cryptant le mot de passe saisie par l'administrateur
 * @param uti
 * @param mdp
 * @param role
 * @return true si la creation de l'utilisateur réussi ou false si la création échoue
 * @throws SQLException
 */
public boolean createUti(String uti, String mdp, boolean role) throws SQLException {
	mdp = this.hash(mdp);
	boolean bool  = BDD.createUti(uti, mdp, role);
	
	
	return bool;
}
	


	
	
	

	/**
	 * <p>
	 * Filtre la collection des mesures en fonction des param&egrave;tres :
	 * </p>
	 * <ol>
	 * <li>la zone (null = toutes les zones)</li>
	 * <li>la date de d&eacute;but (null = &agrave; partir de l'origine)</li>
	 * <li>la date de fin (null = jusqu'&agrave; la fin)<br />
	 * </li>
	 * </ol>
	 */
	// public void filtrerLesMesure(String laZone, Date leDebut, Date lafin) {
	public ArrayList<Mesure> filtrerLesMesure(String laZone) {
		// Parcours de la collection
		// Ajout à laSelection des objets qui correspondent aux paramètres
		// Envoi de la collection

		ArrayList<Mesure> laSelection = new ArrayList<Mesure>();
		for (Mesure mesure : lesMesures) {
			if (laZone.compareTo("*") == 0) {
				laSelection.add(mesure);
			} else {
				if (laZone.compareTo(mesure.getNumZone()) == 0) {
				
					laSelection.add(mesure);
					
				}
			}
		}

		return laSelection;

	}

	/**
	 * <p>
	 * Retourne la collection des mesures
	 * </p>
	 * 
	 * @return ArrayList<Mesure>
	 */
	public ArrayList<Mesure> getLesMesures() {

		return lesMesures;
		
	}

	/**
	 * <p>Convertion d'une String en Date</p>
	 * 
	 * @param strDate
	 * @return Date
	 * @throws ParseException
	 */
	private Date strToDate(String strDate) throws ParseException {

		SimpleDateFormat leFormat = null;
		Date laDate = new Date();
		leFormat = new SimpleDateFormat("yy-MM-dd kk:mm");

		laDate = leFormat.parse(strDate);
		return laDate;
	}
}
