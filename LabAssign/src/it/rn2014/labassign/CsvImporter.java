package it.rn2014.labassign;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Classe che gestisce l'import dei dati da file CSV a Database MySQL, effettuato i
 * controlli del caso.
 * 
 * @author Nicola Corti
 */
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
	

	/**
	 * Funzione che legge dal file "lab-rs.csv" l'elenco dei ragazzi che animano un laboratorio RS
	 * e li inserisce nel DB come vincolo
	 * 
	 * @param conn Connettore MySQL al DB di destinazione
	 * @param rl Elenco di rover (per verificare se esistono)
	 * @throws Exception In caso di funzionamento errato di I/O
	 */
	public static void insertConstraintLabRS(MySqlConnector conn, RoverList rl) throws Exception {
	
		
			BufferedReader f = new BufferedReader(new FileReader("lab-rs.csv"));
			String line = f.readLine();
			
			while((line = f.readLine()) != null){
				String[] sep = line.split(";");
				
				String tipologia =			sep[0];
				String nome = 				sep[1];
				String cognome = 			sep[2];
				String codicecensimento = 	sep[3];
				String gruppo = 			sep[4];
				String templabcode = 		sep[5];
				String turno =				sep[7];
				
				// Escludo i capi dall'inserimento
				if (tipologia.contains("capo")) continue;
				
				// Ricerco i ragazzi
				boolean found = false;
				for(Rover r: rl)
					if (Double.parseDouble(codicecensimento) == r.getCode()) found = true;
				
				if (!found) {
					System.err.println("ASSENTE: " + codicecensimento + " - " + nome + ", " + cognome + ", " + gruppo);
					continue;
				} 
				
				// Controllo inconsistenza se e' presente un altro vincolo (se puo' fa un update)
				if (conn.executeCount("SELECT * FROM vincoli WHERE codicecensimento = '" + codicecensimento + "'") > 0){		
					String result = conn.executeString("SELECT turn" + turno + " FROM vincoli 	WHERE codicecensimento = '" + codicecensimento + "'");
					if (result != null) 
						System.err.println("PROBLEMA VINCOLI: " + codicecensimento + " - " + nome + ", " + cognome + ", " + gruppo);
					else 
						conn.update("UPDATE vincoli SET turn" + turno + "='TEMP-RS-LAB-?-?-" + templabcode + "' WHERE codicecensimento = '" + codicecensimento + "'");
					
					continue;
				}
				
				if (turno.contains("1"))
					conn.update("INSERT INTO vincoli (codicecensimento, turn1, turn2, turn3) VALUES ('" + codicecensimento + "','TEMP-RS-LAB-?-?-" + templabcode + "', NULL, NULL)");
				if (turno.contains("2"))
					conn.update("INSERT INTO vincoli (codicecensimento, turn1, turn2, turn3) VALUES ('" + codicecensimento + "', NULL, 'TEMP-RS-LAB-?-?-" + templabcode + "', NULL)");
				if (turno.contains("3"))
					conn.update("INSERT INTO vincoli (codicecensimento, turn1, turn2, turn3) VALUES ('" + codicecensimento + "', NULL, NULL,'TEMP-RS-LAB-?-?-" + templabcode + "')");
			}

			f.close();
			
	} 
	
	/**
	 * Funzione che legge dal file "laboratori.csv" l'elenco dei laboratori alla route nazionale
	 * e li inserisce nel DB
	 * 
	 * @param conn Connettore MySQL al DB di destinazione
	 */
	@SuppressWarnings("unused")
	public static void insertLabs(MySqlConnector conn) {
		try {
			BufferedReader f = new BufferedReader(new FileReader("laboratori.csv"));
			String line = f.readLine();
			
			while((line = f.readLine()) != null){
				String[] sep = line.split(";", 14);
				
				String codice =				sep[1];
				String strada = 			sep[3];
				String titolo = 			sep[4];
				String gruppo = 			sep[5];
				String mineta = 			sep[7];
				String maxeta = 			sep[8];
				String maxpartecipanti =	sep[12];
				String handicap = 			sep[13];
				
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
