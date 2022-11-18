package gestionErreurs;

import java.util.HashMap;

public class MessagesDErreurs {
	private static final HashMap<String, String> erros = new HashMap<>() {{
		put("3","Pb of access");
		put("21","Pb of access to db");
		put("22","Pb of traitment");
		put("24","Not enough money");
		put("25","Le N° de compte doit être numérique");
		put("26","Le N° de compte doit comporter 4 caractere");
		
	}};
	
	public static String getMessage(String num) {
		return erros.get(num);
	}
	
}
