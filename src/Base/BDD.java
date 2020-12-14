package Base;

import model.*;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

import org.mindrot.jbcrypt.BCrypt;

import com.mysql.jdbc.Statement;




public class BDD {
	
	private String BDD ;
	private String url ;
	private String user ;
	private String passwd ;
	private Statement smt;
	private boolean admin ;

	
	public BDD(){
		BDD = "vinci";
		url = "jdbc:mysql://localhost:3306/" + BDD;
        user = "Vinci";
		passwd = "P@ssw0rdsio";
		smt = connexion();
				}
	
	public String getBDD() {
		return BDD;
	}


	public void setBDD(String bDD) {
		BDD = bDD;
	}



	public String getUrl() {
		return url;
	}



	public void setUrl(String url) {
		this.url = url;
	}


	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
	}


	public String getPasswd() {
		return passwd;
	}



	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	
	

	public boolean GetAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	
	

	

/**
 * Cette méthode permet de faire la connexion à la base de données.
 * @return Statement
 */
	public  Statement connexion() {
		Statement smt = null;
                 try {
                 Class.forName("com.mysql.jdbc.Driver");
                 Connection conn = DriverManager.getConnection(url, user, passwd); 
                  smt = (Statement) conn.createStatement();        
                 System.out.println("connexion reussi");
                
                 }
                 catch (Exception e){
                	 System.out.println(e.getMessage());
                	 
                 }
                 return smt;

	}
	

	
	/**
	 * Cette méthode permet de retourner une liste de mesures liés au stade proposé en paramètre 
	 * @param stade
	 * @return ArrayList
	 */
	public ArrayList requete_select(String stade) {
		ArrayList  resultat = new ArrayList();
		
		try {

		ResultSet res = smt.executeQuery("select * from mesure where nom = '" + stade + "';");
		   while (res.next()) {
			String zone = res.getString("numZone");
			Date date =  res.getDate("horaDate") ;
			Float fahrenheit = res.getFloat("fahrenheit") ;
			Mesure laMesure = new Mesure(zone, date, fahrenheit);
			resultat.add(laMesure);
        }
		  // System.out.println(resultat.toString());
		}
		
		catch (Exception e) {
	System.out.println(e.getMessage());
		}
		
	
		return resultat;
		
		
	}
	/**
	 * Cette méthode retourne une liste de tous les stades existants
	 * @return ArrayList
	 */
	public ArrayList AFF_All_Stade() {
		ArrayList lesStades = new ArrayList();
		try {
	
			ResultSet res = smt.executeQuery("SELECT * FROM stade");
			 while (res.next()) {
				 String stade = res.getString("nom");
				 lesStades.add(stade);
			 }
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return lesStades;
	}
	/**
	 * Cette methode permet de rendre dynamique la liste des zones en renvoyant le nombre de zone que possède le stade
	 * @param stade
	 * @return int
	 */
	public int NB_Zone_Stade(String stade) {
		 int count = 0;
		 
		try {
	
			ResultSet res = smt.executeQuery("select  count(distinct numZone) from mesure  where nom = '" + stade + "';");
			res.next();
			count = res.getInt("count(distinct numZone)");

		} 
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		System.out.println(count);
		return count;
	}
	
	
	/**
	 * Cette méthode permet à l'utilisateur de se connecter avec un pseudo et un mot de passe crypter
	 * @param uti
	 * @param mdp
	 * @return true si l'utilisateur existe et que le mot de passe saisie correspond avec celui dans la BDD
	 */
	public boolean connectUTI(String uti, String mdp) {
		boolean ok = false;
		
		try {
		ResultSet res = smt.executeQuery("select passwords,admin from utilisateurs where pseudo = '" + uti +"';");
		res.next();
		if (BCrypt.checkpw(mdp, res.getString("passwords"))) {
			System.out.println("lemot de passe hasher correspond");
			ok = true;
		
			if(res.getString("admin").compareTo("1")==0) {
				this.setAdmin(true);
				
			
			}
	
		}
		
		}
		catch (Exception e) {
			System.out.println("erreur lors de la connexion niveau BDD");
		}
		return ok;
		
	}
	
	
	
	public boolean SaveMinMaxTemp(String stade, int min, int max) {
		boolean bool = false;
		System.out.println(stade+ min+ max);
		try {
		
			smt.executeUpdate("update stade set min_temp=" + min +", max_temp=" + max +" where nom='"+stade+"';");
			bool = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bool;
		
	}
	
	
	public int[] getMaxMinTemp(String stade) {
		int[] a = new int[2];
		try {
			
			ResultSet res = smt.executeQuery("select min_temp,max_temp from stade where nom='"+stade+"';");
			res.next();
			a[0] = res.getInt("min_temp");
			a[1] = res.getInt("max_temp");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
	}
	
	/**
	 * Cette methonde permet de creer un nouveau utilisateur depuis le compte admin
	 * @param uti
	 * @param mdp
	 * @param role
	 * @return true si la création de l'utilisateur à réussi
	 * @throws SQLException
	 */
	public boolean createUti(String uti, String mdp, boolean role) throws SQLException {
		try {
			
		smt.executeUpdate("insert into utilisateurs  values ('" + uti + "','" + mdp +"'," + role +");");
		
		}catch (Exception e) {
			
			JOptionPane.showMessageDialog(null,"Problème lors de la création de l'utilisateur : " + uti );
			return false;
		}
		return true;
	}
	
	public String getTel(String stade) {
		String tel = "";
		try {
			
			ResultSet res = smt.executeQuery("select tel from utilisateurs inner join stade on utilisateurs.pseudo = stade.pseudo_uti where nom ='"+stade+"';");
			res.next();
			tel = res.getString("tel");
	}
		catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
		return tel;
		
	}
	
	
	@Override
	public String toString() {
		return "BDD [BDD=" + BDD + ", url=" + url + ", user=" + user + ", passwd=" + passwd + "]";
	}

}
