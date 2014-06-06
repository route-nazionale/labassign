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

		MySqlConnector conn = new MySqlConnector();
		conn.connect();
		
		List<Group> gl = conn.getGroups();
		System.err.println(" --- GRUPPI LETTI --- ");
		RoverList rl = conn.getRovers(gl);
		System.err.println(" --- ROVER LETTI --- ");
		EventList el = conn.getLabs();
		System.err.println(" --- LAB LETTI --- ");
		
		// Generazione casuale delle tavole rotonde
		for(int i = 1; i <= 33; i++){
			RoundTable r = new RoundTable("TAV-" + i, "TAVOLA " + i, 0, null);
			if (i % 5 == 0) r.setRoadsPreference(true, false, false, false, false);
			if (i % 5 == 1) r.setRoadsPreference(false, true, false, false, false);
			if (i % 5 == 2) r.setRoadsPreference(false, false, true, false, false);
			if (i % 5 == 3) r.setRoadsPreference(false, false, false, true, false);
			if (i % 5 == 4) r.setRoadsPreference(false, false, false, false, true);
			el.addEvent(r);
		}
		
		beginningAssignment(rl, el);
		
		System.err.println("SATISFACTION --- " + rl.totalSatisfaction() + " --- MAX --- " + rl.totalMaxSatisfaction());
		System.err.println(rl.totalSatisfaction()/rl.totalMaxSatisfaction());
		
		conn.close();		

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
	@SuppressWarnings("unused")
	public static void beginningAssignment(RoverList rl, EventList el) {
		
		// Assegnamento iniziale, itero sui 3 giorni
		for (int workingday = 1; workingday <= 3; workingday++){
			
			el.updateWorkingDay(workingday);
			
			// Itero sui rover in archivio
			for (Rover r : rl){				
				
				boolean find = false;		// Flag per indicare se ho trovato un'associazione
				int find_prio = -1;			// Valore di priorita' attuale (utile per sistemare)
				Iterator<Event> it;			// Iteratore su Eventi
				Event temp = null;			// Evento temporaneo
				
				// Itero sui livelli di priorita'
				for (int prio = 1; (prio <= 6 && !find); prio++){
					
					it = el.iterator();
					
					// Itero sugli eventi
					while (it.hasNext() && !find){
						temp = it.next();
						if (r.isSuitable(temp, workingday, prio))
							find = true;
							find_prio = prio;
					}
				}
				
				if (!find) { 
					throw new RuntimeException("ASSOCIAZIONE IMPOSSIBILE!!! - Rivedere le priorita' dei vincoli");
				} else {
					r.assignToEvent(workingday, temp);
					el.updateEvent(temp);
				}
			}
		}
	}
}
