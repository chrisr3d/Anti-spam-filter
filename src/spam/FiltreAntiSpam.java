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
	
	
	
	
	public static void main(String[] args) throws IOException{
		charger_dictionnaire("dictionnaire1000en.txt");
		lire_message("baseapp/spam/0.txt");
		System.out.println("Entrer un nombre de Ham");
		Scanner sc = new Scanner(System.in);
		/*
		int nbSpam = sc.nextInt();
		appprentissageSpam(nbSpam);
		
		
		for(int j = 0; j<probaSpam.length;j++){
			System.out.println(probaSpam[j]);
		}
		*/
		int nbHam = sc.nextInt();
		appprentissageHam(nbHam);
		
		
		for(int j = 0; j<probaHam.length;j++){
			System.out.println(probaHam[j]);
		}
		
		
		
		
	}
	
	public static void appprentissageHam(int nbFichier) throws IOException{
		ensembleMessageHam = new boolean[nbFichier][dico.size()];
		probaHam = new double[dico.size()];
		for(int i =0; i<nbFichier; i++){
			
			lire_message("baseapp/ham/"+i+".txt");
			ensembleMessageHam[i] = message;
			}
		
		for(int j = 0; j<dico.size();j++){
			
			for(int k = 0; k<nbFichier;k++){
				
				if( ensembleMessageHam[k][j] == true){
					probaHam[j]++;
				}
			}
			
			probaHam[j] = (probaHam[j]/nbFichier);
		}
		}
		
	
	public static void appprentissageSpam(int nbFichier) throws IOException{
		ensembleMessageSpam = new boolean[nbFichier][dico.size()];
		probaSpam = new double[dico.size()];
		for(int i =0; i<nbFichier; i++){
			
			lire_message("baseapp/spam/"+i+".txt");
			ensembleMessageSpam[i] = message;
			}
		
		for(int j = 0; j<dico.size();j++){
			
			for(int k = 0; k<nbFichier;k++){
				
				if( ensembleMessageSpam[k][j] == true){
					probaSpam[j]++;
				}
			}
			
			probaSpam[j] = (probaSpam[j]/nbFichier);
			
		}
			
		}
		
	
	public static void charger_dictionnaire(String fichier)throws IOException{
		try {
			Scanner sc = new Scanner(new FileReader(fichier));
			String ligne;
			while(sc.hasNextLine()){
				ligne = sc.nextLine();
				if (ligne.length()>=3)
					dico.add(ligne);
			}
			sc.close();
		}
		catch (IOException e){ 
            System.out.println(e.getMessage()); 
            System.exit(1);
		}
	}
	
	public static void lire_message(String fichier) throws IOException{
		message = new boolean[dico.size()];
		try {
			Scanner sc = new Scanner(new FileReader(fichier));
			String mot;
			boolean j=false;
			while(sc.hasNext()){
				mot = sc.next();
				j=false;
				for(int i=0;i<dico.size();i++){
					if(dico.get(i).equalsIgnoreCase(mot)){
						message[i]=true;
						j=true;
						break;
					}
				}
			}
			sc.close();
		}
		catch (IOException e){ 
            System.out.println(e.getMessage()); 
            System.exit(1);
            
		}
	}
	
}
