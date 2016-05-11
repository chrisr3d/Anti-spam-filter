package spam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FiltreAntiSpam {
	private static String[] dico = new String[1000];
	private static boolean[] message;
	
	public static void main(String[] args) throws IOException{
		charger_dictionnaire("dictionnaire1000en.txt");
	}
	
	public static void charger_dictionnaire(String fichier)throws IOException{
		int i=0;
		try {
			BufferedReader bfr = new BufferedReader(new FileReader(fichier));
			String ligne;
			while((ligne=bfr.readLine()) != null){
				dico[i]=ligne;
				i++;
			}
			bfr.close();
		}
		catch (IOException e){ 
            System.out.println(e.getMessage()); 
            System.exit(1);
		}
	}
	
}
