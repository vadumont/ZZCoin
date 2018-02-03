package hackathon;

import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

public class wordRecognition {


	/*private static final Hashtable<String,Integer> words = new Hashtable<String,Integer>() {{
	    put("carte",      1);
	    put("emprunt",      2);
	    put("etranger",     3);
	    put("ouvrir", 4);
	    put("perd",    5);
	    put("probleme",    6);
	 }};*/
	 private static final List<String> Refwords = new Vector<String>() {{
		    add("carte");
		    add("emprunt");
		    add("etranger");
		    add("ouvrir");
		    add("perd");
		    add("probleme");
		 }};
	
	//private static List<String> Keywords = new Vector<String>();
	 
	public static void checkWord(String s,List<String> Keywords ) {
		for (String item : Refwords) {
		    if(matching(item,s,3)) {
		    	 Keywords.add(item);
		    }
		}
	}
	
	/////////////////////
/*			String modele = "Toiture";
			String strAComparer = "Bordure";
			boolean resultat = matching(modele, strAComparer, 3);
			System.out.println(resultat);
		
	 */
static boolean matching(String modele, String strAComparer, int nbErreursPermis)
{
	int nbErreursTrouves = 0;
	int r=0;
	//On teste s'il y a une différence de taille des 2 chaines, 
	//un caractère de plus ou de moins est une erreur
	int diffTailleChaines = modele.length() - strAComparer.length();
	nbErreursTrouves += Math.abs(diffTailleChaines);
	if (nbErreursTrouves > nbErreursPermis)
		return false;
	//La différence de taille n'est pas supérieur au nombre d'erreurs permis, 
	//on compare les chaines caractère par caractère
		int longueur = Math.min(modele.length(),strAComparer.length());
		if (longueur > 3) {
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
	
	
	
	////////////////////////
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int i=0;
		//System.out.println("hello");
		StringTokenizer phrase = new StringTokenizer("J'ai perdu ma carte à l'etranger. Y a t il une demarche particuliere pour ce probleme ? Qui dois-je contacter?",".");
	    while (phrase.hasMoreTokens()) {
	    	 StringTokenizer phrase2 = new StringTokenizer(phrase.nextToken(),"?");
	         while (phrase2.hasMoreTokens()) {
	        	 List<String> Keywords = new Vector<String>();
	        	 StringTokenizer mot = new StringTokenizer(phrase2.nextToken());
	        	 while(mot.hasMoreTokens()) {
	        		 checkWord(mot.nextToken(),Keywords);
	        	 }
	        	 System.out.println("Phrase"+i++);
	        	 
	        	 for (String item : Keywords) {
	        		 System.out.println(item);
	         	 }
		     }
	    }
	}
}
