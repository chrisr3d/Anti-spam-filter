package spam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FiltreAntiSpam {
	private static ArrayList<String> dico = new ArrayList<String>();
	private static boolean[] message;
	private static boolean[][] ensembleMessageHam;
	private static boolean[][] ensembleMessageSpam;
	private static double[] probaHam;
	private static double[] probaSpam;
	private static double epsilon = 1.0;
	private static double PYSpam, PYHam, erreurSpam, erreurHam;

	public static void main(String[] args) throws IOException {
		charger_dictionnaire("dictionnaire1000en.txt");
		lire_message("baseapp/spam/0.txt");
		Scanner sc = new Scanner(System.in);
		System.out.println("Entrer un nombre de Spam pour l'apprentissage : ");
		int nbSpam = sc.nextInt();
		System.out.println("Entrer un nombre de Ham pour l'apprentissage : ");
		int nbHam = sc.nextInt();
		PYSpam=(double)nbSpam/(nbSpam+nbHam);
		PYHam=(double)nbHam/(nbSpam+nbHam);
		System.out.println("\nApprentissage ...");
		appprentissageHam(nbHam);
		appprentissageSpam(nbSpam);
		System.out.println("\nTest : ");
		testSpam(100);
		testHam(200);
		System.out.println("Erreur de test sur les 100 SPAM : "+erreurSpam+"%");
		System.out.println("Erreur de test sur les 200 HAM : "+erreurHam+"%");
		double erreurGlobale = (erreurSpam*100 + erreurHam*200)/300;
		System.out.println("Erreur de test globale sur 300 mails : "+erreurGlobale+"%");
		sc.close();
	}

	public static void appprentissageHam(int nbFichier) throws IOException {
		ensembleMessageHam = new boolean[nbFichier][dico.size()];
		probaHam = new double[dico.size()];
		for (int i = 0; i < nbFichier; i++) {
			lire_message("baseapp/ham/" + i + ".txt");
			ensembleMessageHam[i] = message;
		}
		for (int j = 0; j < dico.size(); j++) {
			for (int k = 0; k < nbFichier; k++) {
				if (ensembleMessageHam[k][j] == true) {
					probaHam[j]++;
				}
			}
			probaHam[j] = ((probaHam[j]+epsilon) / (nbFichier+2*epsilon));
		}
	}

	public static void appprentissageSpam(int nbFichier) throws IOException {
		ensembleMessageSpam = new boolean[nbFichier][dico.size()];
		probaSpam = new double[dico.size()];
		for (int i = 0; i < nbFichier; i++) {
			lire_message("baseapp/spam/" + i + ".txt");
			ensembleMessageSpam[i] = message;
		}
		for (int j = 0; j < dico.size(); j++) {

			for (int k = 0; k < nbFichier; k++) {

				if (ensembleMessageSpam[k][j] == true) {
					probaSpam[j]++;
				}
			}
			probaSpam[j] = ((probaSpam[j]+epsilon) / (nbFichier+2*epsilon));
		}
	}

	public static void testHam(int nbFichier) throws IOException{
		for (int i=0; i<nbFichier; i++){
			lire_message("basetest/ham/"+i+".txt");
			String Y = "";
			double PYSpamXx = PYSpam/message.length;
			double PYHamXx = PYHam/message.length;
			for(int j=0; j<message.length;j++){
				if(message[j]){
					PYSpamXx*=probaSpam[j];
					PYHamXx*=probaHam[j];
				}
				else{
					PYSpamXx*=(1.0-probaSpam[j]);
					PYHamXx*=(1.0-probaHam[j]);
				}
			}
			if(PYHamXx >= PYSpamXx)
				Y = "HAM";
			else{
				Y = "SPAM	***erreur***";
				erreurHam+=1;
			}
			System.out.println("HAM numero "+i+" : P(Y=SPAM | X=x) = "+PYSpamXx+", P(Y=HAM | X=x) = "+PYHamXx+" => identifie comme un "+Y);
		}
		erreurHam = erreurHam/nbFichier*100;
	}
	
	public static void testSpam(int nbFichier) throws IOException{
		for (int i=0; i<nbFichier; i++){
			lire_message("basetest/spam/"+i+".txt");
			String Y = "";
			double PYSpamXx = PYSpam/message.length;
			double PYHamXx = PYHam/message.length;
			for(int j=0; j<message.length;j++){
				if(message[j]){
					PYSpamXx*=probaSpam[j];
					PYHamXx*=probaHam[j];
				}
				else{
					PYSpamXx*=(1.0-probaSpam[j]);
					PYHamXx*=(1.0-probaHam[j]);
				}
			}
			if(PYSpamXx >= PYHamXx)
				Y = "SPAM";
			else{
				Y = "HAM	***erreur***";
				erreurSpam+=1;
			}
			System.out.println("SPAM numero "+i+" : P(Y=SPAM | X=x) = "+PYSpamXx+", P(Y=HAM | X=x) = "+PYHamXx+" => identifie comme un "+Y);
		}
		erreurSpam = erreurSpam/nbFichier*100;
	}
	
	public static void charger_dictionnaire(String fichier) throws IOException {
		try {
			Scanner sc = new Scanner(new FileReader(fichier));
			String ligne;
			while (sc.hasNextLine()) {
				ligne = sc.nextLine();
				if (ligne.length() >= 3)
					dico.add(ligne);
			}
			sc.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}

	public static void lire_message(String fichier) throws IOException {
		message = new boolean[dico.size()];
		try {
			Scanner sc = new Scanner(new FileReader(fichier));
			String mot;
			boolean j = false;
			while (sc.hasNext()) {
				mot = sc.next();
				j = false;
				for (int i = 0; i < dico.size(); i++) {
					if (dico.get(i).equalsIgnoreCase(mot)) {
						message[i] = true;
						j = true;
						break;
					}
				}
			}
			sc.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}

}
