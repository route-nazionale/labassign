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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Classe che rappresenta i parametri di esecuzione del sistema.
 * 
 * @author Nicola Corti
 */
public class Parameters {
	
	/** Numero massimo di iterazioni del sistema oltre il quale si ferma */
	public static double MAX_ITERATIONS = 100000;
	/** Valore di soglia di soddisfacimento globale del sistema (in percentuale) */
	public static double SATISFATION_THRESHOLD = 0.80;
	
	/** Massimo numero di partecipanti ai laboratori */
	public static final int LABORATORY_MAX_USER = 35;
	/** Massimo numero di partecipanti alle tavole rotonde */
	public static final int ROUNDTABLE_MAX_USER = 500;
	
	/** Numero massimo di partecipanti per lo stesso gemellaggio per laboratorio */
	public static final int LABORATORY_MAX_TWINNING_USER = 3;
	/** Numero massimo di partecipanti per lo stesso gemellaggio per tavola rotonda */
	public static final int ROUNDTABLE_MAX_TWINNING_USER = 5;
	
	///////////////
	// ACCESSO DB
	///////////////
	
	/** Host del database */
	public static String DB_HOST = "localhost";
	/** Utente di accesso al database */
	public static String DB_USER = "nicolacorti";
	/** Password di accesso al database */
	public static String DB_PASS = "***********";
	/** Nome del database */
	public static String DB_NAME = "provvisorio";
	
	/////////////////////
	// PRIORITA' VINCOLI
	/////////////////////
	
	/** Priorita' vincolo numero persone eventi */
	public static int PRIO_FULL = 5;
	/** Priorita' vincolo evento che soddisfa scelta strade */
	public static int PRIO_ROAD = 1;
	/** Priorita' vincolo eta prevista per laboratorio */
	public static int PRIO_AGE = 3;
	/** Priorita' vincolo numero persone gemellaggio in un laboratorio */
	public static int PRIO_TWIN_LAB = 2;
	/** Priorita' vincolo numero persone gemellaggio in una tavola rotonda */
	public static int PRIO_TWIN_TAV = 2;
	/** Priorita' vincolo novizio che partecia ad evento non adatto */
	public static int PRIO_NOVICE = 2;
	/** Priorita' vincolo eventi nel proprio quartiere */
	public static int PRIO_QUART = 5;
	/** Priorita' vincolo handicap che partecia ad evento non adatto */
	public static int PRIO_HANDICAP = 4;
	/** Priorita' vincolo eventi non uguali */
	public static int PRIO_EQUALS = 5;
	/** Priorita' vincolo almeno un laboratorio */
	public static int PRIO_ONE_LAB = 5;

	/** Massima priorita' impostata */
	public static int MAX_PRIO = 5;
	/**
	 * @param filename
	 * @throws IOException
	 */
	public static void getParameters(String filename) throws IOException{
		Properties prop = new Properties();
		InputStream input = null;
	 
		
			input = new FileInputStream(filename);
	 		prop.load(input);
	 
	 		DB_HOST = prop.getProperty("DB_HOST");
			DB_USER = prop.getProperty("DB_USER");
			DB_PASS = prop.getProperty("DB_PASS");
			DB_NAME = prop.getProperty("DB_NAME");
			
			int max = 0;
			
			if ((PRIO_FULL = Integer.parseInt(prop.getProperty("PRIO_FULL"))) > max) max = PRIO_FULL;
			if ((PRIO_ROAD = Integer.parseInt(prop.getProperty("PRIO_ROAD"))) > max) max = PRIO_ROAD;
			if ((PRIO_AGE = Integer.parseInt(prop.getProperty("PRIO_AGE"))) > max) max = PRIO_AGE;
			if ((PRIO_TWIN_LAB = Integer.parseInt(prop.getProperty("PRIO_TWIN_LAB"))) > max) max = PRIO_TWIN_LAB;
			if ((PRIO_TWIN_TAV = Integer.parseInt(prop.getProperty("PRIO_TWIN_TAV"))) > max) max = PRIO_TWIN_TAV;
			if ((PRIO_NOVICE = Integer.parseInt(prop.getProperty("PRIO_NOVICE"))) > max) max = PRIO_NOVICE;
			if ((PRIO_QUART = Integer.parseInt(prop.getProperty("PRIO_QUART"))) > max) max = PRIO_QUART;
			if ((PRIO_HANDICAP = Integer.parseInt(prop.getProperty("PRIO_HANDICAP"))) > max) max = PRIO_HANDICAP;
			if ((PRIO_EQUALS = Integer.parseInt(prop.getProperty("PRIO_EQUALS"))) > max) max = PRIO_EQUALS;
			if ((PRIO_ONE_LAB = Integer.parseInt(prop.getProperty("PRIO_ONE_LAB"))) > max) max = PRIO_ONE_LAB;
		
			MAX_PRIO = max;
	}
	
}
