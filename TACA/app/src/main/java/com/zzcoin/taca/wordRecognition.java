package com.zzcoin.taca;


import android.app.Application;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;


public class wordRecognition {

	 private static String[] array = { "PEL", "PTZ", "TEG", "tiers", "tous_risques", "achats", "achete", "acheter", "acquerir", "adherer", "aide", "annuler", "apport", "argent", "article", "assistance", "assurance", "assurer", "auto", "autorisation", "avantages", "avoir", "bancaire", "banque", "beneficier", "bleue", "bon", "carte", "choisir", "code", "combien", "commencer", "comment", "commercant", "complementaire", "compte", "conducteur", "confidentiel", "connaitre", "conseil", "consulter", "contrat", "coute", "credit", "debit", "debiter", "declarer", "demarche", "depense", "differe", "donnees",
			 "effectuer", "endettement", "epargne", "epargner", "erreur", "especes", "etranger", "euro", "faire", "fais", "fixe", "fois", "fonctionne", "gamme", "garantie", "haut", "hors", "immediat", "information", "infos", "interets", "internet", "joint", "ligne", "liquide", "lire", "livret", "luxe", "marche", "moyen", "negation", "net", "nouveau", "obtenir", "offre", "opposition", "opter", "o�",
			 "paiement", "passe", "payer", "perp", "personnel", "perte", "placement", "plafond", "plusieurs", "portable", "posseder", "possibilite", "possible", "pourquoi", "pouvoir", "pret", "prix", "probleme", "profiter", "proteger", "quand", "que", "quel", "quoi","qu�est-ce", "reagir", "regler", "retirer", "retourner", "retrait", "sante", "securiser", "securis�e", "servir", "sinistre", "souscrire", "suivre", "surement", "systematique", "tarification", "tarifs", "taux", "telephone", "utilisation", "utiliser", "vehicule", "vie", "virement", "vol", "vouloir", "zero", "zone"};
	 private static List<String> Refwords = new ArrayList<>(Arrays.asList(array)); 
	 
	 
	 /*{{
"etranger" ���,"ouvrir" ���,"perd" ���,"livret" ���,"information" ���,"infos" ���,"renseignement" ���,"details" ���,"PEL" ���,"pret" ���,"interet" ���, ����  ��"principe" ���, ����  ��"logement" ���, ����  ��"financer" ���, ����  ��"association" ���, "probleme" ���, ����  ��"souci" ���, ����  ��"aide" ���, ����  ��"epargne" ���, ����  ��"biens" ���, ����  ��"telephone" ���, ����  ��"payer" ���, ����  ��"retrait" ���, ����  ��"especes" ���, ����  ��"cheque" ���, ����  ��"projet" ���, ����  ��"nouveau" ���, ����  ��"etude" ���, ����  ��"conseiller" ���, ����  ��"appartement" ���, ����  ��"banque" ���, ����  ��"paylib" ���, ����  ��"carte" ���,
"emprunt" ���,"voyage" ���, ����  ��"assurance" ���, ����  ��"agence" ���, ����  ��"automate" ���, ����  ��"distributeur" ���, ����  ��"deposer" ���, ����  ��"proche" ���, ����  ��"compte" ���, ����  ��"partir" ���, ����  ��"demarrer" ���, ����  ��"lancer" ���, ����  ��"trouver" ���, ����  ��"stage" ���, ����  ��"job" ���, ����  ��"acheter" ���, ����  ��"voiture" ���, ����  ��"assurer" ���, ����  ��"maison" ���, ����  ��"retirer" ���, ����  ��"argent" ���,
 }};*/
	
	 
	public static void checkWord(String s,List<String> Keywords ) {
		for (String item : Refwords) {
		    if(matching(item,s,3)) {
		    	 Keywords.add(item);
		    }
		}
	}
	

	static boolean matching(String modele, String strAComparer, int nbErreursPermis) // a ameliorer
	{
		int nbErreursTrouves = 0;
		//On teste s'il y a une diff�rence de taille des 2 chaines, 
		//un caract�re de plus ou de moins est une erreur
		int diffTailleChaines = modele.length() - strAComparer.length();
		nbErreursTrouves += Math.abs(diffTailleChaines);
		if (nbErreursTrouves > nbErreursPermis)
			return false;
		//La diff�rence de taille n'est pas sup�rieur au nombre d'erreurs permis, 
		//on compare les chaines caract�re par caract�re
			int longueur = Math.min(modele.length(),strAComparer.length());
			if (longueur <= 3) {
				for (int i = 0; i < longueur; i++)
				{
					if (modele.charAt(i) != strAComparer.charAt(i))
					{
						nbErreursTrouves++;
						if (nbErreursTrouves > 0)
							return false;
					}
				}
			}
			else {
				for (int i = 0; i < longueur-3; i++)
				{
					if (modele.charAt(i) != strAComparer.charAt(i))
					{
						nbErreursTrouves++;
						if (nbErreursTrouves > 1)
							return false;
					}
				}
				for (int i = longueur-3; i < longueur; i++)
				{
					if (modele.charAt(i) != strAComparer.charAt(i))
					{
						nbErreursTrouves++;
						if (nbErreursTrouves > 1)
							return false;
					}
				}
			
				return true;
			}
			return false;
	}
	
	
	public static Vector<String> Highlight(String text, final Context context, LinearLayout linear) throws InterruptedException {
		int i=0;
		int c=0;
		 final LinearLayout ll = linear;
		String textChain="";
		//System.out.println("hello");
		Vector<String> list = new Vector<String>();
		StringTokenizer phrase = new StringTokenizer(text,".");
	    while (phrase.hasMoreTokens()) {
	    	 StringTokenizer phrase2 = new StringTokenizer(phrase.nextToken(),"?");
	         while (phrase2.hasMoreTokens()) {
	        	 
	        	 StringTokenizer mot = new StringTokenizer(phrase2.nextToken());
	        	 while(mot.hasMoreTokens()) {
	        		 checkWord(mot.nextToken(),list);
	        	 }
	        	 System.out.println("Phrase"+i++);
	        	 textChain="";
	        	 for (String item : list) {
	        		 
	        		 textChain=textChain+item+" ";
	        		 
	         	 }
	        	 System.out.println(textChain);

				//Request

				// Instantiate the RequestQueue.
				 textChain= textChain.replace(" ","_");

				 RequestQueue queue = Volley.newRequestQueue(context);
				 String url ="http://lifehand-technology.fr/request.php?value="+textChain;

				 // Request a string response from the provided URL.
				 StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
						 new Response.Listener<String>() {
							 @Override
							 public void onResponse(String response) {
								 // Display the first 500 characters of the response string.
								 ChatActivity ca = new ChatActivity();
										 //ca.createAnswer(response,ll,context);
								 			ca.createAnswer("En cas de perte, de vol ou d’utilisation frauduleuse, faites opposition immédiatement. Agissez au plus vite dès que vous vous rendez compte de la disparition de votre carte, ou si votre carte est avalée sans raison apparente par un distributeur automatique de billets en dehors des heures d’ouverture de l’agence, ou encore si vous constatez des opérations frauduleuses sur votre relevé d’opérations",ll,context);
							 }
						 }, new Response.ErrorListener() {
					 @Override
					 public void onErrorResponse(VolleyError error) {
						 Toast toast = Toast.makeText(context,"Erreur de connexion au serveur",Toast.LENGTH_SHORT);
					 }
				 });
				// Add the request to the RequestQueue.
				 queue.add(stringRequest);


				/*try {
	
				        Process p;
				        p = Runtime.getRuntime().exec("./RdN.py " + textChain);
				        p.waitFor();

				        BufferedReader reader =
				             new BufferedReader(new InputStreamReader(p.getInputStream()));

				        String line2 = "";
				        while ((line2 = reader.readLine())!= null) {
				        	System.out.println(line2 + "\n");
				        }

					try(BufferedReader br = new BufferedReader(new FileReader("resultat.txt"))) {
					    StringBuilder sb = new StringBuilder();
					    String line = br.readLine();

					    while (line != null) {
					        sb.append(line);
					        sb.append(System.lineSeparator());
					        line = br.readLine();
					    }
					    String everything = sb.toString();
					    System.out.println(everything);
					}

	        	 
	        	 
	        	 } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
	        	 
		     }
	    }
	    return list;
	}
	
	
	/*public static void main(String[] args) throws InterruptedException { //juste une function apres
		//StringTokenizer phrase = new StringTokenizer("J'ai perdu ma carte � l'etranger. Y a t il une demarche particuliere pour ce probleme ? Comment ouvrir un Livret �pargne Orange ?",".");
	   
	    Highlight("comment securiser mes achats sur internet ?");  
	    		/*" "+ 
	    		"" + 
	    		"c'est quoi un apport personnel ?" */
	    		
	//}
}
