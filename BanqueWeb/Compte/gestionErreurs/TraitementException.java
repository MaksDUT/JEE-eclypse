package gestionErreurs;

public class TraitementException extends Exception {
	
	public TraitementException(String msg) {
		super(MessagesDErreurs.getMessage(msg));		
	}
	
	

}
