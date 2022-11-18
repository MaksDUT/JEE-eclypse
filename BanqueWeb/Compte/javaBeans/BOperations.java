package javaBeans;

import java.math.BigDecimal;
import java.rmi.UnexpectedException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.stream.Stream;
import com.mysql.cj.protocol.ValueDecoder;
import com.mysql.cj.xdevapi.PreparableStatement;

import gestionErreurs.TraitementException;

public class BOperations {

	private String noDeCompte;
	private String nom;
	private String prenom;
	private BigDecimal solde;
	private BigDecimal ancienSolde;
	private BigDecimal nouveauSolde;
	private Connection co;
	//exo 3 
	private BigDecimal valeur;	
	private String op;
	//exo 4
	private String dateInf;
	private String dateSup;
	private ArrayList<String[]> operationsParDates;
	
	
	public BOperations() {
		// TODO Auto-generated constructor stub
	}

	public void setNoDeCompte(String noDeCompte) {
		this.noDeCompte = noDeCompte;
	}

	public void consulter() throws TraitementException {
		try {
			String request = "select * from Compte where nocompte=?";
			PreparedStatement pre = co.prepareStatement(request);
			pre.setString(1, noDeCompte);
//			Statement stmt = co.createStatement();
//			var resultats = stmt.executeQuery(request);
			var resultats = pre.executeQuery();
			resultats.next();
			nom = resultats.getString("nom");
			prenom = resultats.getString("prenom");
			solde = resultats.getBigDecimal("solde");
			
		} catch (SQLException e) {
			throw new TraitementException("3");
		}
	}

	public String getNoDeCompte() {
		return noDeCompte;
	}

	public String getNom() {
		return nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public BigDecimal getSolde() {
		return solde;
	}
	
	
	public String getValeur() {
		return valeur.toString();
	}

	public void setValeur(String valeur) {
		this.valeur = new BigDecimal(valeur);
	}

	public String getOp() {
		return op;
	} 

	public void setOp(String op) {
		if(!op.equals("+") && !op.equals("-")) {
			System.out.println("you can't do that to me");
			return;
		}
		this.op = op;
	}
	
	
	public String getDateInf() {
		return dateInf;
	}

	public void setDateInf(String dateInf) {
		this.dateInf = dateInf;
	}

	public String getDateSup() {
		return dateSup;
	}

	public void setDateSup(String dateSup) {
		this.dateSup = dateSup;
	}

	public ArrayList<String[]> getOperationsParDates() {
		return operationsParDates;
	}

	public void traiter() throws TraitementException {
		
		try {
			String request = "select solde from Compte where nocompte=?";
			PreparedStatement pre = co.prepareStatement(request);
			pre.setString(1, noDeCompte);

			var resultats = pre.executeQuery();
			resultats.next();
			this.ancienSolde = resultats.getBigDecimal("solde");
			if(op.equals("+")) {
				this.nouveauSolde = this.ancienSolde.add(this.valeur);
			} else if(op.equals("-")) {
				if(valeur.compareTo(this.ancienSolde) < 0 ) {
					throw new TraitementException("24");
				}
				var maybeyoupoor = this.ancienSolde.subtract(valeur);
				if(maybeyoupoor.compareTo(BigDecimal.ZERO) < 0) {
					System.out.println("Compte pas suffisament alimenté");
					//co.rollback();
					return;
				}
				this.nouveauSolde = maybeyoupoor;
			}
			System.out.println(nouveauSolde);
			co.setAutoCommit(false);
			String rq = "update Compte set solde=? where nocompte=?";
			PreparedStatement pre2 = co.prepareStatement(rq);
			pre2.setBigDecimal(1, nouveauSolde);
			pre2.setString(2, noDeCompte);
			pre2.executeUpdate();
			
			String insert = "INSERT INTO Operations (nocompte,date,heure,op,valeur) VALUES (?,CURRENT_DATE(),CURRENT_TIME(),?,?);";
			PreparedStatement pre3 = co.prepareStatement(insert);
			pre3.setString(1, noDeCompte);
			pre3.setString(2, op);
			pre3.setBigDecimal(3, nouveauSolde);
			pre3.executeUpdate();
			co.commit();
			co.setAutoCommit(true);
		} catch (SQLException e) {
			try {
				co.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				throw new TraitementException("22");
			}
			throw new TraitementException("22");
		}
	}
	
	
	//exo 4
	public void listerParDates() throws UnexpectedException {
		this.operationsParDates= new ArrayList<>();
		Objects.requireNonNull(noDeCompte);
		Objects.requireNonNull(dateInf);
		Objects.requireNonNull(dateSup);
		
		try {
			String request = "select * from Operations where (date BETWEEN ? AND ?);";
			PreparedStatement pre = co.prepareStatement(request);
			pre.setString(1, dateInf);
			pre.setString(2, dateSup);
			var resultats = pre.executeQuery();
			while(resultats.next()) {
				System.out.println("Oui");
				String[] strings = Stream.of(resultats.getString("date"), resultats.getString("op"), resultats.getBigDecimal("valeur").toString()).toArray(String[]::new);
				this.operationsParDates.add(strings);
			}
			
		}catch (SQLException e) {
			// TODO: handle exception
			throw new UnexpectedException(e.getMessage());
		}
		
	}

	public void ouvrirConnexion() throws TraitementException{
		try {
			String hostName = "sqletud.u-pem.fr";
			String dbName = "maxime.dumerat_db";
			String userName = "maxime.dumerat";
			String password = "root";
			String connectionURL = "jdbc:mysql://" + hostName + ":3306/" + dbName;
			co = DriverManager.getConnection(connectionURL, userName,password);
			
		} catch(SQLException e) {
			throw new TraitementException("21");
		}

	}

	public void fermerConnexion() throws SQLException {
		co.close();
		co = null;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return  noDeCompte +  " " + nom + " " + prenom + " "+ solde + " " ;
	}
	
	public static void main(String[] args) throws SQLException, UnexpectedException {
		var odb = new BOperations();
		try {
			odb.ouvrirConnexion();
		} catch(TraitementException e) {
			System.out.println(e.getMessage());
		}
		odb.setNoDeCompte("0001");
		try {
			odb.consulter();
		} catch (TraitementException e) {
		    System.out.println(e.getMessage());
		}
		
		System.out.println(odb);
		
		
		odb.fermerConnexion();
		
		
	}

}
