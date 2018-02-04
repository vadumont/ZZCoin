package hackathon;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

public class wordRecognition {

	 private static String[] array = { "PEL", "PTZ", "TEG", "tiers", "tous_risques", "achat", "achats", "achete", "acheter", "acquerir", "adherer", "aide", "annuler", "apport", "argent", "article", "assistance", "assurance", "assurer", "auto", "autorisation", "avantages", "avoir", "bancaire", "banque", "beneficier", "bleue", "bon", "carte", "choisir", "code", "combien", "commencer", "comment", "commercant", "complementaire", "compte", "conducteur", "confidentiel", "connaitre", "conseil", "consulter", "contrat", "coute", "credit", "debit", "debiter", "declarer", "demarche", "depense", "differe", "donnees", "effectuer", "endettement", "epargne", "epargner", "erreur", "especes", "etranger", "euro", "faire", "fais", "fixe", "fois", "fonctionne", "gamme", "garantie", "haut", "hors", "immediat", "information", "infos", "interets", "internet", "joint", "ligne", "liquide", "lire", "livret", "luxe", "marche", "moyen", "negation", "net", "nouveau", "obtenir", "offre", "opposition", "opter", "où", "paiement", "passe", "payer", "perp", "personnel", "personnelle", "perte", "placement", "plafond", "plusieurs", "portable", "posseder", "possibilite", "possible", "pourquoi", "pouvoir", "pret", "prix", "probleme", "profiter", "proteger", "quand", "que", "quel", "quoi","qu’est-ce", "reagir", "regler", "retirer", "retourner", "retrait", "sante", "securiser", "securisée", "servir", "sinistre", "souscrire", "suivre", "surement", "systematique", "tarification", "tarifs", "taux", "telephone", "utilisation", "utiliser", "vehicule", "vie", "virement", "vol", "vouloir", "zero", "zone"};
	 private static List<String> Refwords = new ArrayList<>(Arrays.asList(array)); 
	 
	 
	 /*{{
"etranger"    ,"ouvrir"    ,"perd"    ,"livret"    ,"information"    ,"infos"    ,"renseignement"    ,"details"    ,"PEL"    ,"pret"    ,"interet"    ,         "principe"    ,         "logement"    ,         "financer"    ,         "association"    , "probleme"    ,         "souci"    ,         "aide"    ,         "epargne"    ,         "biens"    ,         "telephone"    ,         "payer"    ,         "retrait"    ,         "especes"    ,         "cheque"    ,         "projet"    ,         "nouveau"    ,         "etude"    ,         "conseiller"    ,         "appartement"    ,         "banque"    ,         "paylib"    ,         "carte"    ,
"emprunt"    ,"voyage"    ,         "assurance"    ,         "agence"    ,         "automate"    ,         "distributeur"    ,         "deposer"    ,         "proche"    ,         "compte"    ,         "partir"    ,         "demarrer"    ,         "lancer"    ,         "trouver"    ,         "stage"    ,         "job"    ,         "acheter"    ,         "voiture"    ,         "assurer"    ,         "maison"    ,         "retirer"    ,         "argent"    ,
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
		//On teste s'il y a une différence de taille des 2 chaines, 
		//un caractère de plus ou de moins est une erreur
		int diffTailleChaines = modele.length() - strAComparer.length();
		nbErreursTrouves += Math.abs(diffTailleChaines);
		if (nbErreursTrouves > nbErreursPermis)
			return false;
		//La différence de taille n'est pas supérieur au nombre d'erreurs permis, 
		//on compare les chaines caractère par caractère
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
	
	
	
	
	public static Vector<Vector<String>> Highlight(String text) {
		int i=0;
		int c=0;
		//System.out.println("hello");
		Vector<String> list1 = new Vector<String>();
		Vector<String> list2 = new Vector<String>();
		Vector<String> list3 = new Vector<String>();
		Vector<String> list4 = new Vector<String>();
		Vector<String> list5 = new Vector<String>();
		Vector<Vector<String>> Keywords = new Vector<Vector<String>>() {{ add(list1);add(list2);add(list3);add(list4);add(list5);}};
		StringTokenizer phrase = new StringTokenizer(text,".");
	    while (phrase.hasMoreTokens()) {
	    	 StringTokenizer phrase2 = new StringTokenizer(phrase.nextToken(),"?");
	         while (phrase2.hasMoreTokens() && c < 6) {
	        	 Vector<String> currentList = Keywords.get(c++);
	        	 StringTokenizer mot = new StringTokenizer(phrase2.nextToken());
	        	 while(mot.hasMoreTokens()) {
	        		 checkWord(mot.nextToken(),currentList);
	        	 }
	        	 System.out.println("Phrase"+i++);
	        	
	        	 for (String item : currentList) {
	        		 System.out.println(item);
	         	 }
		     }
	    }
	    return Keywords;
	}
	
	
	
	public static void main(String[] args) { //juste une function apres
		//StringTokenizer phrase = new StringTokenizer("J'ai perdu ma carte à l'etranger. Y a t il une demarche particuliere pour ce probleme ? Comment ouvrir un Livret Épargne Orange ?",".");
	   
	    Highlight("comment faire opposition a ma carte bancaire ?" + 
	    		"qu'est-ce que la dispense d'accompte et comment la demander ? "+ 
	    		"quels sont les avantages du paiement par paylib ?" + 
	    		"comment augmenter votre decouvert autorise ?" + 
	    		"comment fonctionnent les alertes premiere operation a l' etranger ?");
		
	}
}
