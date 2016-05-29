package spam;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
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
		Scanner sc = new Scanner(System.in);
		System.out.println("Entrer un nombre de Spam pour l'apprentissage : ");
		int nbSpam = sc.nextInt();
		System.out.println("Entrer un nombre de Ham pour l'apprentissage : ");
		int nbHam = sc.nextInt();
		PYSpam=(double)nbSpam/(nbSpam+nbHam);
		PYHam=(double)nbHam/(nbSpam+nbHam);
		System.out.println("\nApprentissage ...");
		String base = "baseapp";
		apprentissageHam(nbHam, base);
		apprentissageSpam(nbSpam, base);
		System.out.println("\nTest : ");
		testSpam(100,200);
		testHam(200,100);
		System.out.println("Erreur de test sur les 100 SPAM : "+erreurSpam+"%");
		System.out.println("Erreur de test sur les 200 HAM : "+erreurHam+"%");
		double erreurGlobale = (erreurSpam*100 + erreurHam*200)/300;
		System.out.println("Erreur de test globale sur 300 mails : "+erreurGlobale+"%");
		sc.close();
	}

	public static void apprentissageHam(int nbHam, String base) throws IOException {
		ensembleMessageHam = new boolean[nbHam][dico.size()];
		probaHam = new double[dico.size()];
		for (int i = 0; i < nbHam; i++) {
			lire_message(base+"/ham/" + i + ".txt");
			ensembleMessageHam[i] = message; // sur chaque ligne on place le message lu
		}
		for (int j = 0; j < dico.size(); j++) {
			for (int k = 0; k < nbHam; k++) {
				if (ensembleMessageHam[k][j] == true) {  // si le message k contient le mot j
					probaHam[j]++; // on incremente le nombre de message contenant le mot j
				}
			}
			probaHam[j] = ((probaHam[j]+epsilon) / (nbHam+2*epsilon)); 
			// une fois tous les messages parcourus, on ajoute epsilon et on divise par le nombre de Ham + 2*epsilon
		}
	}

	public static void apprentissageSpam(int nbSpam, String base) throws IOException {
		ensembleMessageSpam = new boolean[nbSpam][dico.size()];
		probaSpam = new double[dico.size()];
		for (int i = 0; i < nbSpam; i++) {
			lire_message(base+"/spam/" + i + ".txt");
			ensembleMessageSpam[i] = message; // sur chaque ligne on place le message lu
		}
		for (int j = 0; j < dico.size(); j++) {
			for (int k = 0; k < nbSpam; k++) {
				if (ensembleMessageSpam[k][j] == true) { // si le message k contient le mot j
					probaSpam[j]++; // on incremente le nombre de message contenant le mot j
				}
			}
			probaSpam[j] = ((probaSpam[j]+epsilon) / (nbSpam+2*epsilon));
			// une fois tous les messages parcourus, on ajoute epsilon et on divise par le nombre de Ham + 2*epsilon
		}
	}

	public static void testHam(int nbHam, int nbSpam) throws IOException{
		for (int i=0; i<nbHam; i++){
			lire_message("basetest/ham/"+i+".txt");
			String Y = "";
			double PYSpamXx = Math.log(PYSpam*(nbHam+nbSpam));
			double PYHamXx = Math.log(PYHam*(nbHam+nbSpam));
			for(int j=0; j<message.length;j++){
				if(message[j]){
					PYSpamXx+=Math.log(probaSpam[j]);
					PYHamXx+=Math.log(probaHam[j]);
				}
				else{
					PYSpamXx+=Math.log(1.0-probaSpam[j]);
					PYHamXx+=Math.log(1.0-probaHam[j]);
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
		erreurHam = erreurHam/nbHam*100;
	}
	
	public static void testSpam(int nbSpam, int nbHam) throws IOException{
		for (int i=0; i<nbSpam; i++){
			lire_message("basetest/spam/"+i+".txt");
			String Y = "";
			double PYSpamXx = Math.log(PYSpam*(nbSpam+nbHam));
			double PYHamXx = Math.log(PYHam*(nbSpam+nbHam));
			for(int j=0; j<message.length;j++){
				if(message[j]){
					PYSpamXx+=Math.log(probaSpam[j]);
					PYHamXx+=Math.log(probaHam[j]);
				}
				else{
					PYSpamXx+=Math.log(1.0-probaSpam[j]);
					PYHamXx+=Math.log(1.0-probaHam[j]);
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
		erreurSpam = erreurSpam/nbSpam*100;
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
			while (sc.hasNext()) {
				mot = sc.next();
				String s = "";
				for(int i=0;i<mot.length();i++){
					if(Character.isLetter(mot.charAt(i)))
						s+=mot.charAt(i);
				}
				for (int i = 0; i < dico.size(); i++) {
					if (dico.get(i).equalsIgnoreCase(s)) {
						message[i] = true;
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
	
	public static void apprend_filtre(String[] args) throws NumberFormatException, IOException{
		apprentissageSpam(Integer.parseInt(args[2]), args[1]);
		apprentissageHam(Integer.parseInt(args[3]), args[1]);
		try{
			FileWriter fw = new FileWriter(args[0]+".txt");
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(args[2]+"\n"+args[3]+"\n");
			for(int i=0; i<probaSpam.length; i++){
				bw.write(probaSpam[i]+"\n");
			}
			for(int i=0; i<probaHam.length; i++){
				bw.write(probaHam[i]+"\n");
			}
			bw.close();
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
		
	}

	public static void filtre_mail(String[] args){
		try{
			Scanner sca = new Scanner(new FileReader(args[0]));
			for(int i=0; i<dico.size(); i++)
				probaSpam[i]=Double.parseDouble(sca.nextLine());
			for(int i=0; i<dico.size(); i++)
				probaHam[i]=Double.parseDouble(sca.nextLine());
			
			lire_message(args[1]);
			String Y = "";
			double PYSpamXx = Math.log(PYSpam);
			double PYHamXx = Math.log(PYHam);
			for(int j=0; j<message.length;j++){
				if(message[j]){
					PYSpamXx+=Math.log(probaSpam[j]);
					PYHamXx+=Math.log(probaHam[j]);
				}
				else{
					PYSpamXx+=Math.log(1.0-probaSpam[j]);
					PYHamXx+=Math.log(1.0-probaHam[j]);
				}
			}
			if(PYSpamXx >= PYHamXx)
				Y = "SPAM";
			else{
				Y = "HAM";
			}
			System.out.println("D'après "+args[0]+", le message "+args[1]+" est un "+Y);
			sca.close();
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
}
