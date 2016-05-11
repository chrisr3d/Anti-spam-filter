package spam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FiltreAntiSpam {
	private static ArrayList<String> dico = new ArrayList<String>();
	private static boolean[] message;
	
	public static void main(String[] args) throws IOException{
		charger_dictionnaire("dictionnaire1000en.txt");
		lire_message("baseapp/spam/test.txt");
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
