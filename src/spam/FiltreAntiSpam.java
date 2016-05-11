package spam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FiltreAntiSpam {
	private static ArrayList<String> dico = new ArrayList<String>();
	private static boolean[] message;
	
	public static void main(String[] args) throws IOException{
		charger_dictionnaire("dictionnaire1000en.txt");
	}
	
	public static void charger_dictionnaire(String fichier)throws IOException{
		try {
			BufferedReader bfr = new BufferedReader(new FileReader(fichier));
			String ligne;
			while((ligne=bfr.readLine()) != null){
				if (ligne.length()>=3)
					dico.add(ligne);
			}
			bfr.close();
		}
		catch (IOException e){ 
            System.out.println(e.getMessage()); 
            System.exit(1);
		}
	}
	
	public static void lire_message(String fichier) throws IOException{
		message = new boolean[dico.size()];
		try {
			BufferedReader bfr = new BufferedReader(new FileReader(fichier));
			String mot;
		}
		catch (IOException e){ 
            System.out.println(e.getMessage()); 
            System.exit(1);
		}
	}
	
}
