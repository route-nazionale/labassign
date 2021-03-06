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
import java.util.PriorityQueue;

/**
 * Classe che rappresenta una collezione di Eventi che vengono considerati per
 * l'esecuzione all'interno dell'algoritmo.
 * 
 * @author Nicola Corti
 */
public class EventList implements Iterable<Event>{

	/** Lista di priorità degli eventi */
	private PriorityQueue<Event> queue;
	
	
	/**
	 * Costruttore che inizializza la lista degli eventi da considerare.
	 */
	public EventList(){
		queue = new PriorityQueue<>();
	}
	
	
	/**
	 * Aggiunge un evento nella lista degli eventi da considerare
	 * 
	 * @param e Evento da aggiungere
	 */
	public void addEvent(Event e){
		queue.add(e);
	}
	
	/**
	 * Ritorna il primo evento nella coda di priorità 
	 * @return Il primo evento nella coda di priorità
	 */
	public Event getEvent(){
		return queue.peek();
	}
	
	/**
	 * Ritorna l'evento con il codice richiesto 
	 * @return L'evento con il codice richiesto, false altrimenti
	 */
	public Event getEvent(String code){
		for(Event e: queue)
			if (e.getCode().contentEquals(code)) return e;
		return null;
	}
	
	/**
	 * Ritorna il primo evento nella coda di priorità in relazione alla strada di coraggio indicata come
	 * parametro.
	 * 
	 * @param road La strada di coraggio desiderata 
	 * @return Il primo evento nella coda che soddisfa la strada di coraggio deside
	 */
	public Event getFirstEventByRoad(int road){
		for (Event e : queue){
			if (e.getRoad() == road) return e;
		}
		return null;
	}
	
	/**
	 * Aggiorna un evento nella coda di priorità
	 * 
	 * @param e Evento da aggiornare
	 */
	public void updateEvent(Event e){
		queue.remove(e);
		queue.offer(e);
	}

	/**
	 * Aggiorna il working day di tutti gli eventi (Implementazione)
	 * 
	 * @param newworkingday Il working day da aggiornare
	 */
	protected void updateWorkingDay(int newworkingday){
		for (Event e: queue)
			e.updateWorkingDay(newworkingday);
	}
	
	/**
	 * Ritorna l'iteratore per iterare sulla collezione di eventi contenuti nel sistema
	 */
	@Override
	public Iterator<Event> iterator() {
		return queue.iterator();
	}


	/**
	 * Metodo che calcola i vincoli sugli organizzatori delle tavole rotonde, in modo che
	 * chi organizza una tavola rotonda sia impegnato per tutto il turno in cui viene svolta
	 * la tavola.
	 * 
	 * @param rl Lista di ragazzi
	 */
	public void computeConstraints(RoverList rl) {
		for (Event e : queue){
			if (e instanceof RoundTable){
				RoundTable r = (RoundTable) e;
				Group organizer = r.getOrganizer();
				
				for (Rover rov: rl){
					if (rov.getGroup() == organizer)
						rov.setConstraint(r, r.getTurn());
				}
				
				Group organizer2 = r.getOrganizer2();
				if (organizer2 != null){
					for (Rover rov: rl){
						if (rov.getGroup() == organizer2)
							rov.setConstraint(r, r.getTurn());
					}
				}
			}
			
		}
		
	}
}
