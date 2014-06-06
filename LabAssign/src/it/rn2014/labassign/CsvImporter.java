/*   This file is part of LabAssign
 *
 *   LabAssign is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   LabAssign is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with LabAssign.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.rn2014.labassign;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

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
	 * Funzione che legge dal file "laboratori.csv" l'elenco deiconn.updatePrepared(s); laboratori alla route nazionale
	 * e li inserisce nel DB
	 * 
	 * @param conn Connettore MySQL al DB di destinazione
	 * @throws IOException Problema di lettura del file
	 */
	public static void insertLabs(MySqlConnector conn) throws IOException {

		BufferedReader f = new BufferedReader(new FileReader("laboratori.csv"));
		String line = f.readLine();
		
		while((line = f.readLine()) != null){
			String[] sep = line.split(";", 14);
			
			String codice =				sep[1];
			//Integer sottocampo =		0;
			String strada = 			sep[3];
			String titolo = 			sep[4];
			//String gruppo = 			sep[5];
			String mineta = 			sep[7];
			String maxeta = 			sep[8];
			String maxpartecipanti =	sep[12];
			String handicap = 			sep[13];
			
			// Genero il pattern per le strade di coraggio
			String stradatuple = "0, 0, 0, 0, 0";
			switch (Integer.parseInt(strada)) {
			case 1: stradatuple = "1, 0, 0, 0, 0"; break;
			case 2: stradatuple = "0, 1, 0, 0, 0"; break;
			case 3: stradatuple = "0, 0, 1, 0, 0"; break;
			case 4: stradatuple = "0, 0, 0, 1, 0"; break;
			case 5: stradatuple = "0, 0, 0, 0, 1"; break;
			}
			
			// Prepared statement per lavorare con stringhe escaped (titoli e codici)
			java.sql.PreparedStatement s = conn.getPrepared( "INSERT INTO laboratori (codice, nome, sottocampo, maxpartecipanti, minpartecipanti, organizzatore, novizio, handicap, etamassima, etaminima, stradadicoraggio1, stradadicoraggio2, stradadicoraggio3, stradadicoraggio4, stradadicoraggio5 )" +
					" VALUES (" +
					"?," +
					"?," +
					"0," +
					"" + (maxpartecipanti.contentEquals("") ? Parameters.LABORATORY_MAX_USER : Integer.parseInt(maxpartecipanti)) + "," +
					"0," + 
					"'" + codice.substring(5, 8) + "'," + 
					"0," +
					"" + (handicap.contains("x") || handicap.contains("X") ? 1 : 0) + "," + 
					Integer.parseInt(maxeta) + "," + 
					Integer.parseInt(mineta) + "," + 
					stradatuple + ");"); 
			
			try {
				s.setString(1, codice);
				s.setString(2, titolo);
				conn.updatePrepared(s);
			} catch (SQLException e) { e.printStackTrace(); }
		}
		f.close();
	}
}
