package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javaBeans.BOperations;

import java.io.IOException;
import java.sql.SQLException;

import gestionErreurs.TraitementException;

/**
 * Servlet implementation class SOperations
 */
public class SOperations extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private BOperations bo;
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SOperations() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}
	
	private boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        int d = Integer.parseInt(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	
	
	private void traitement(HttpServletRequest request) throws TraitementException {
		var op = request.getParameter("oper");
		var compValue = request.getParameter("valeurEuro");	
		System.out.println("  fafaefaefea " + compValue);
		bo.setOp(op);
		if(!compValue.contains(".")) {
			bo.setValeur(compValue+".00");
		} else {
			bo.setValeur(compValue);
		}
		bo.traiter();
		request.setAttribute("solde", bo.getSolde());
		this.getServletContext().getRequestDispatcher("/JOperations.jsp").forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		var exec = request.getParameter("button");
		
		if(exec == null) {
			this.getServletContext().getRequestDispatcher("/JSaisieNoDeCompte.jsp").forward(request, response);
			//response.getWriter().append("le paramètre NoDeCompte n'a pas été spécifié");
		}
		
		switch(exec) {
			case "consultation": {
				var par = request.getParameter("NoDeCompte");
				try {
					
					response.setCharacterEncoding("UTF-16");
					if(par == null) {
						this.getServletContext().getRequestDispatcher("/JSaisieNoDeCompte.jsp").forward(request, response);
						//response.getWriter().append("le paramètre NoDeCompte n'a pas été spécifié");
					}
					
					bo = new BOperations();
					if(!isNumeric(par)) {
						  throw new TraitementException("25");
					} else if (par.length() <= 3) {
						throw new TraitementException("26");
					}
					bo.ouvrirConnexion();
					//System.out.println("UUUUUUUUUUUUUUUUUUUUUUUUIIIIIIIIIIIIIIIIIIII");
					bo.setNoDeCompte(par);
					bo.consulter();
					//System.out.println("YESSS");
					request.setAttribute("nco", bo.getNoDeCompte());
					request.setAttribute("nom", bo.getNom());
					request.setAttribute("prenom", bo.getPrenom());
					request.setAttribute("solde", bo.getSolde());
					response.getWriter().append("N° de Compte : ").append(bo.getNoDeCompte());
					response.getWriter().append("\nNom: ").append(bo.getNom());
					response.getWriter().append("\nPrénom : ").append(bo.getPrenom());
					response.getWriter().append("\nSolde : ").append(bo.getSolde().toString());
					this.getServletContext().getRequestDispatcher("/JOperations.jsp").forward(request, response);
					//bo.fermerConnexion();
				} catch (TraitementException e) {
					// TODO Auto-generated catch block
					System.out.println("hello" + e.getMessage());
					e.printStackTrace();
					response.getWriter().append(e.getMessage());
					request.setAttribute("err", e.getMessage());
					request.setAttribute("NoDeCompte",par);
					this.getServletContext().getRequestDispatcher("/JSaisieNoDeCompte.jsp").forward(request, response);
				} 
				
			}
			
			case "operation" : {	
			
				try {
					
					traitement(request);
				} catch (TraitementException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
					var op = request.getParameter("oper");
					var compValue = request.getParameter("valeurEuro");	
					
					request.setAttribute("err", e.getMessage());
					request.setAttribute("NoDeCompte",compValue);
					this.getServletContext().getRequestDispatcher("/JSaisieNoDeCompte.jsp").forward(request, response);
				}
				
			}
			
			
			default : {
				this.getServletContext().getRequestDispatcher("/JSaisieNoDeCompte.jsp").forward(request, response);
			}
		}
		
//		try {
//			
//			response.setCharacterEncoding("UTF-16");
//			if(par == null) {
//				this.getServletContext().getRequestDispatcher("/JSaisieNoDeCompte.jsp").forward(request, response);
//				//response.getWriter().append("le paramètre NoDeCompte n'a pas été spécifié");
//			}
//			
//			bo = new BOperations();
//			if(!isNumeric(par)) {
//				  throw new TraitementException("25");
//			} else if (par.length() <= 3) {
//				throw new TraitementException("26");
//			}
//			bo.ouvrirConnexion();
//			//System.out.println("UUUUUUUUUUUUUUUUUUUUUUUUIIIIIIIIIIIIIIIIIIII");
//			bo.setNoDeCompte(par);
//			bo.consulter();
//			//System.out.println("YESSS");
//			request.setAttribute("nco", bo.getNoDeCompte());
//			request.setAttribute("nom", bo.getNom());
//			request.setAttribute("prenom", bo.getPrenom());
//			request.setAttribute("solde", bo.getSolde());
//			response.getWriter().append("N° de Compte : ").append(bo.getNoDeCompte());
//			response.getWriter().append("\nNom: ").append(bo.getNom());
//			response.getWriter().append("\nPrénom : ").append(bo.getPrenom());
//			response.getWriter().append("\nSolde : ").append(bo.getSolde().toString());
//			this.getServletContext().getRequestDispatcher("/JOperations.jsp").forward(request, response);
//			bo.fermerConnexion();
//		} catch (TraitementException e) {
//			// TODO Auto-generated catch block
//			System.out.println("hello" + e.getMessage());
//			e.printStackTrace();
//			response.getWriter().append(e.getMessage());
//			request.setAttribute("err", e.getMessage());
//			request.setAttribute("NoDeCompte",par);
//			this.getServletContext().getRequestDispatcher("/JSaisieNoDeCompte.jsp").forward(request, response);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}

}
