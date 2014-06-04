package it.rn2014.labassign;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CsvImporter {

	/**
	 * Funzione che legge dal file "codici.csv" l'elenco dei ragazzi che parteciperanno al laboratorio CODICI
	 * e li inserisce nel DB come vincoli
	 * 
	 * @param conn Connettore MySQL al DB di destinazione
	 * @param rl Elenco di rover (per verificare se esistono)
	 * @throws Exception In caso di funzionamento errato di I/O
	 */
	public static void insertConstraintCodici(MySqlConnector conn, RoverList rl) throws Exception {
		
			BufferedReader f = new BufferedReader(new FileReader("codici.csv"));
			String line = f.readLine();
			
			while((line = f.readLine()) != null){
				String[] sep = line.split(";");
				
				// Ricerco se il ragazzo e' presente nel DB
				boolean found = false;
				for(Rover r: rl)
					if (Double.parseDouble(sep[2]) == r.getCode()) found = true;
				
				if (!found) {
					System.err.println("RAGAZZO " + sep[2]);
				} else {
					conn.update("INSERT INTO vincoli (codicecensimento, turn1, turn2, turn3) VALUES ('" + sep[2] + "'," + "'CODICI','CODICI',NULL)");
				}
			}
			f.close();
	}
	

	public static void insertConstraintLabRS(MySqlConnector conn, RoverList rl) throws Exception {
	
		
			BufferedReader f = new BufferedReader(new FileReader("lab-rs.csv"));
			String line = f.readLine();
			
			while((line = f.readLine()) != null){
				String[] sep = line.split(";");
				
				// Escludo i capi dall'inserimento
				if (sep[0].contains("capo")) continue;
				
				// Ricerco i ragazzi
				boolean found = false;
				for(Rover r: rl)
					if (Double.parseDouble(sep[3]) == r.getCode()) found = true;
				
				if (!found) {
					System.err.println("ASSENTE: " + sep[3] + " - " + sep[1] + ", " + sep[2] + ", " + sep[4]);
					continue;
				} 
				
				// Controllo inconsistenza se e' presente un altro vincolo
				if (conn.executeCount("SELECT * FROM vincoli WHERE codicecensimento = '" + sep[3] + "'") > 0){		
					String result = conn.executeString("SELECT turn" + sep[7] + " FROM vincoli 	WHERE codicecensimento = '" + sep[3] + "'");
					if (result != null) 
						System.err.println("PROBLEMA VINCOLI: " + sep[3] + " - " + sep[1] + ", " + sep[2] + ", " + sep[4]);
					else 
						conn.update("UPDATE vincoli SET turn" + sep[7] + "='TEMP-RS-LAB-?-?-" + sep[5] + "' WHERE codicecensimento = '" + sep[3] + "'");
					
					continue;
				}
				
				if (sep[7].contains("1"))
					conn.update("INSERT INTO vincoli (codicecensimento, turn1, turn2, turn3) VALUES ('" + sep[3] + "','TEMP-RS-LAB-?-?-" + sep[5] + "', NULL, NULL)");
				if (sep[7].contains("2"))
					conn.update("INSERT INTO vincoli (codicecensimento, turn1, turn2, turn3) VALUES ('" + sep[3] + "', NULL, 'TEMP-RS-LAB-?-?-" + sep[5] + "', NULL)");
				if (sep[7].contains("3"))
					conn.update("INSERT INTO vincoli (codicecensimento, turn1, turn2, turn3) VALUES ('" + sep[3] + "', NULL, NULL,'TEMP-RS-LAB-?-?-" + sep[5] + "')");
			}

			f.close();
			
	} 
	
	public static void insertLabs(MySqlConnector conn) {
		try {
			BufferedReader f = new BufferedReader(new FileReader("laboratori.csv"));
			String line = f.readLine();
			
			while((line = f.readLine()) != null){
				String[] sep = line.split(";");
				System.out.println(sep);
			}
			f.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
