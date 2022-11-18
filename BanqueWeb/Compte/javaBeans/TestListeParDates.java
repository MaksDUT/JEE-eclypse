package javaBeans;

import java.rmi.UnexpectedException;
import java.sql.SQLException;

import gestionErreurs.MessagesDErreurs;
import gestionErreurs.TraitementException;

public class TestListeParDates {
	public static void main(String[] args) throws SQLException, UnexpectedException {
		var odb = new BOperations();
		try {
			odb.ouvrirConnexion();
		} catch(TraitementException e) {
			System.out.println(e.getMessage());
		}
		
		odb.setNoDeCompte("0001");

		odb.setDateInf("2004-09-04");
		odb.setDateSup("2004-09-07");
		odb.listerParDates();
		//affichage
		try {
			odb.consulter();
		} catch (TraitementException e) {
		    System.out.println(e.getMessage());
		}
		
		System.out.println(odb);
		System.out.println("Les opérations du N° de compte : "+odb.getNoDeCompte()+" ayant eu lieu du "+odb.getDateInf()+" au "+odb.getDateSup()+" :");
		odb.getOperationsParDates().forEach(row -> System.out.println(row[0]+" "+row[1]+" "+row[2]));
		odb.fermerConnexion();
	}
}
