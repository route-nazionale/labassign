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
		RoverList rl = conn.getRovers(gl);
		
		
		//CsvImporter.insertLabs(conn);
		
		
		conn.close();
		System.out.println("Connection closed");
		
		/*
		RoverList rl = new RoverList(null);
		EventList el = new EventList(null);
		
		beginningAssignment(rl, el);
		
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
		
		// Cicla sui 3 giorni che devono essere assegnati.
		for (int workingday = 1; workingday <= 3; workingday++){
			
			for (Rover r : rl){
				
				// Sceglie una strada a caso fra quelle del rover e
				// prova ad assegnargli un laboratorio
				Event temp = null;
				EventIteratorRoad eir = new EventIteratorRoad(el, workingday);
				while(eir.hasNext()){
					temp = eir.next();
					if (r.isSuitable(temp, workingday)) break;
				}
				
				// Controllo se ho trovato una possibile associazione senno' genero un'eccezione.
				if (temp == null || !eir.hasNext()) throw new RuntimeException("NO ASSOCIATION POSSIBLE");
				else {
					r.assignToEvent(workingday, temp);
					el.updateEvent(temp);
				}
			}
		}
	}

}
