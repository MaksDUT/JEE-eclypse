package javaBeans;

import java.rmi.UnexpectedException;
import java.sql.SQLException;

import gestionErreurs.TraitementException;

public class TestTraitement {
	public static void main(String[] args) throws SQLException, UnexpectedException {
		var odb = new BOperations();
		try {
			odb.ouvrirConnexion();
		} catch(TraitementException e) {
			System.out.println(e.getMessage());
		}
		odb.setNoDeCompte("0001");
		//traitement
		odb.setOp("+");
		odb.setValeur("210.00");
		try {
			odb.traiter();
		} catch(TraitementException e) {
			System.out.println(e.getMessage());
		}
		//affichage
		try {
			odb.consulter();
		} catch (TraitementException e) {
		    System.out.println(e.getMessage());
		}
		System.out.println(odb);
		odb.fermerConnexion();
	}
}
