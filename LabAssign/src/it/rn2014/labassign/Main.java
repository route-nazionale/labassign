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
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

/**
 * Classe main per l'esecuzione dell'algoritmo di assegnazione dei
 * rover/scolte partecipanti alla route nazionale ai vari laboratori.
 * 
 * @author Nicola Corti
 */
public class Main {

	/**
	 * Funzione main eseguita dal programma
	 * @param args Elenco dei parametri
	 * @throws Exception Eccezione generica generata dall'esecuzione (non gestita).
	 */
	public static void main(String[] args) throws Exception {

		if (args.length < 1){
			System.err.println("Lab Assign: Invocazione errata. E' necessario indicare un file di configurazione");
			System.err.println("\t Usage: java -jar labassign.jar labassign.conf");
		}
		Parameters.getParameters(args[0]);
		
		System.out.println("-----------------------------------------");
		System.out.println("| LabAssign per Route Nazionale 2014    |");
		System.out.println("-----------------------------------------");
		
		System.out.println();
		System.out.println("--------- PARAMETRI ---------");
		System.out.println("| PRIO_FULL: \t\t" + Parameters.PRIO_FULL);
		System.out.println("| PRIO_ROAD: \t\t" + Parameters.PRIO_ROAD);
		System.out.println("| PRIO_ROAD_2: \t\t" + Parameters.PRIO_ROAD_2);
		System.out.println("| PRIO_ROAD_3: \t\t" + Parameters.PRIO_ROAD_3);
		System.out.println("| PRIO_AGE: \t\t" + Parameters.PRIO_AGE);
		System.out.println("| PRIO_TWIN_LAB: \t" + Parameters.PRIO_TWIN_LAB);
		System.out.println("| PRIO_TWIN_TAV: \t" + Parameters.PRIO_TWIN_TAV);
		System.out.println("| PRIO_NOVICE: \t\t" + Parameters.PRIO_NOVICE);
		System.out.println("| PRIO_QUART: \t\t" + Parameters.PRIO_QUART);
		System.out.println("| PRIO_HANDICAP: \t" + Parameters.PRIO_HANDICAP);
		System.out.println("| PRIO_EQUALS: \t\t" + Parameters.PRIO_EQUALS);
		System.out.println("| PRIO_ONE_LAB: \t" + Parameters.PRIO_ONE_LAB);
		System.out.println("-----------------------------");
		System.out.println("--------- CONNESSIONE DB ---------");
		
		MySqlConnector conn = new MySqlConnector();
		
		System.out.print("Connessione al DB...");
		conn.connect();
		System.out.print("OK!\n");

		if(args.length > 1){
			if(args[1].equals("--check")){
				
				SanityChecker sc = new SanityChecker(conn);
				sc.runAllChecks();
				
				System.out.print("Chiudo la connessione al DB...");
				conn.close();
				System.out.print("OK!\n");
				return;
			}
		}
		
		System.out.print("Recupero i gruppi dal DB...");
		List<Group> gl = conn.getGroups();
		System.out.print("OK!\n");
		
		System.out.print("Recupero i rover dal DB...");
		RoverList rl = conn.getRovers(gl);
		System.out.print("OK!\n");
		
		System.out.print("Recupero i laboratori dal DB...");
		EventList el = conn.getLabs(gl);
		System.out.print("OK!\n");
		
		// Aggiungo l'evento codici
		Lab codici = new Lab("CODICI", "CODICI", 0, 1000, null, false, false, 100, 1, 0);
		codici.setRoadsPreference(true, true, true, true, true);
		codici.setEnabled(false);
		el.addEvent(codici);
		
		System.out.print("Recupero le tavole rotonde dal DB...");
		conn.getRoundTable(gl, el);
		System.out.print("OK!\n");
		
		System.out.print("Recupero i vincoli preesistenti dal DB...");
		conn.getCostraints(rl, el);
		System.out.print("OK!\n");
		
		System.out.print("Imposto i vincoli preesistenti per le tavole rotonde...");
		el.computeConstraints(rl);
		System.out.print("OK!\n");
		
		System.out.print("Ripartisco i laboratori nei sottocampi...");
		conn.setSubcamps(el);
		System.out.print("OK!\n");
		
		// Genero l'assegnamento
		beginningAssignment(rl, el);
		
		
		BufferedReader var = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Stampo le schede dei Laboratori? [s/n]");
		String res = var.readLine();
		if (res.contains("s") || res.contains("S")){
			for (Event e: el){
				if (e instanceof RoundTable) continue;
				e.print();
			}
		}
		System.out.println("Stampo le schede delle Tavole Rotonde? [s/n]");
		res = var.readLine();
		if (res.contains("s") || res.contains("S")){
			for (Event e: el){
				if (e instanceof Lab) continue;
				e.print();
			}
		}
		System.out.println("Stampo le associazioni dei ragazzi? [s/n]");
		res = var.readLine();
		if (res.contains("s") || res.contains("S")){
			for (Rover r: rl)
				r.print();
		}
		
		System.out.println("Carico sul DB i risultati? [s/n]");
		res = var.readLine();
		if (res.contains("s") || res.contains("S")){
			System.out.print("Eseguo query per l'inserimento dei risultati...");
			conn.sendResult(rl, el);
			System.out.print("OK!\n");
		}
		
		System.out.print("Chiudo la connessione al DB...");
		conn.close();	
		System.out.print("OK!\n");
		
		
		System.out.println("############## FINE! ##############");
		System.out.println("# SODDISFAZIONE: " + rl.totalSatisfaction());
		System.out.println("# SU: " + rl.totalMaxSatisfaction());
		System.out.println("#####");
		System.out.println("# PERCENTUALE: " + (rl.totalSatisfaction()/rl.totalMaxSatisfaction()));
		System.out.println("###################################");
		
		/*
		double itercount = 0;
		double globalsat = rl.totalSatisfaction()/rl.totalMaxSatisfaction();
		while (itercount < Parameters.MAX_ITERATIONS && globalsat < Parameters.SATISFATION_THRESHOLD){
		*/
			/* Si deve scrivere la procedura che:
			 * - Pesca dalla lista di priorita' il ragazzo meno soddisfatto
			 * - Va a capire quale e' il laboratorio che gli crea problemi
			 * - Vede se puo' rimuovere l'associazione per il ragazzo dal laboratorio
			 * 		- Se si, prende il primo laboratorio per quella strada di coraggio e prova ad assegnarglielo. 
			 * 		- Oppure prende il ragazzo piu' soddisfatto e prova a scambiare un laboratorio con lui.
			 */
		/*	
			itercount++;
			globalsat = rl.totalSatisfaction()/rl.totalMaxSatisfaction();
		}
		*/
		System.exit(0);
	}

	/**
	 * Funzione per calcolare l'assegnamento iniziale dei rover/scolte al laboratori
	 * 
	 * @param rl Elenco di rover partecipanti
	 * @param el Elenco di eventi che devono essere assegnati
	 */
	public static void beginningAssignment(RoverList rl, EventList el) {
		
		
		System.out.println("--------- ASSOCIAZIONI RAGAZZI ---------");
		// Assegnamento iniziale, itero sui 3 giorni
		for (int workingday = 1; workingday <= 3; workingday++){
			
			System.out.print("$ GIORNO " + workingday + "...");
			el.updateWorkingDay(workingday);
						
			// Itero sui rover in archivio
			for (Rover r : rl){				
				
				if (!r.toBeAssigned(workingday)) continue;
				
				boolean find = false;		// Flag per indicare se ho trovato un'associazione
				int find_prio = -1;			// Valore di priorita' attuale (utile per sistemare)
				Iterator<Event> it;			// Iteratore su Eventi
				Event temp = null;			// Evento temporaneo
				
				// Itero sui livelli di priorita'
				for (int prio = 1; (prio <= Parameters.MAX_PRIO && !find); prio++){
					
					it = el.iterator();
					
					// Itero sugli eventi
					while (it.hasNext() && !find){
						temp = it.next();
						if (!temp.isEnabled()) continue;
						if (r.isSuitable(temp, workingday, prio))
							find = true;
							find_prio = prio;
					}
				}
				
				if (!find) { 
					throw new RuntimeException("ASSOCIAZIONE IMPOSSIBILE!!! - Rivedere le priorita' dei vincoli");
				} else {
					r.assignToEvent(workingday, temp, find_prio);
					el.updateEvent(temp);
				}
			}
			System.out.print("Completato! :-)\n");
		}
	}
}
