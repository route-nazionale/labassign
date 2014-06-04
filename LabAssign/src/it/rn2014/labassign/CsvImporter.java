package it.rn2014.labassign;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CsvImporter {

	public static void insertConstraintCodici(MySqlConnector conn, RoverList rl) {
		try {
			BufferedReader f = new BufferedReader(new FileReader("codici.csv"));
			String line = f.readLine();
			
			while((line = f.readLine()) != null){
				String[] sep = line.split(";");
				boolean found = false;
				for(Rover r: rl){
					if (Double.parseDouble(sep[2]) == r.getCode()) found = true;
				}
				if (!found) {
					System.err.println("RAGAZZO " + sep[2]);
				} else {
					conn.update("INSERT INTO vincoli (codicecensimento, turn1, turn2, turn3) VALUES ('" + sep[2] + "'," + "'CODICI','CODICI',NULL)");
				}
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
	

	public static void insertConstraintLabRS(MySqlConnector conn, RoverList rl) {
		try {
			BufferedReader f = new BufferedReader(new FileReader("lab-rs.csv"));
			String line = f.readLine();
			int s = 0;
			while((line = f.readLine()) != null){
				String[] sep = line.split(";");
				if (sep[0].contains("capo")) continue;
				boolean found = false;
				for(Rover r: rl){
					if (Double.parseDouble(sep[3]) == r.getCode()) found = true;
				}
				if (!found) {
					System.err.println("ASSENTE: " + sep[3] + " - " + sep[1] + ", " + sep[2] + ", " + sep[4]);
					s++;
				} else {
					if (conn.executeCount("SELECT * FROM vincoli WHERE codicecensimento = '" + sep[3] + "'") > 0){
						System.err.println("PROBLEMA " + sep[3] + " - " + sep[1] + ", " + sep[2] + ", " + sep[4]);
						String result = null;
						if (sep[7].contains("1")){
							result = conn.executeString("SELECT turn1 FROM vincoli 	WHERE codicecensimento = '" + sep[3] + "'");
							if (result == null)
								System.err.println("C'E POSTO");
							else
								System.err.println("NON C'E POSTO");
						}
						if (sep[7].contains("2")){
							result = conn.executeString("SELECT turn2 FROM vincoli 	WHERE codicecensimento = '" + sep[3] + "'");
							if (result == null)
								System.err.println("C'E POSTO");
							else
								System.err.println("NON C'E POSTO");
						}
						if (sep[7].contains("3")){
							result = conn.executeString("SELECT turn3 FROM vincoli 	WHERE codicecensimento = '" + sep[3] + "'");
							if (result == null)
								System.err.println("C'E POSTO");
							else
								System.err.println("NON C'E POSTO");
						}
					
					}
					/*else {
						if (sep[7].contains("1"))
							conn.update("INSERT INTO vincoli (codicecensimento, turn1) VALUES ('" + sep[3] + "','LAB-RS-TEMP-" + sep[6] + "', NULL, NULL)");
						if (sep[7].contains("2"))
							conn.update("INSERT INTO vincoli (codicecensimento, turn1) VALUES (NULL,'" + sep[3] + "','LAB-RS-TEMP-" + sep[6] + "', NULL)");
						if (sep[7].contains("3"))
							conn.update("INSERT INTO vincoli (codicecensimento, turn1) VALUES (NULL, NULL,'" + sep[3] + "','LAB-RS-TEMP-" + sep[6] + "')");
						
					}*/
				}
			}
			System.err.println("TOTALE ERRORI " + s);
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
