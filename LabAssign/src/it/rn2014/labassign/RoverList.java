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
 * 
 */
package it.rn2014.labassign;

import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * Classe che rappresenta una collezione di Rover che vengono considerati per
 * l'esecuzione all'interno dell'algoritmo.
 * 
 * @author Nicola Corti
 *
 */
public class RoverList implements Iterable<Rover> {

	/** Lista di priorità dei rover */
	private PriorityQueue<Rover> queue;
	
	/**
	 * Costruttore che inizializza la lista dei rover da considerare.
	 */
	public RoverList(){
		queue = new PriorityQueue<>();
	}
	
	/**
	 * Metodo per aggiungere un rover alla lista se questo non e' ancora presente
	 * 
	 * @param r Rover da aggiungere
	 */
	public void addRover(Rover r){
		if (!queue.contains(r)) queue.add(r);
	}
	
	/**
	 * Ritorna il rover meno soddisfatto, senza rimuoverlo dalla lista dei rover.
	 * 
	 * @return Il rover meno soddisfatto
	 */
	public Rover getRover(){
		return queue.poll();
	}
	
	/**
	 * Ritorna il rover meno soddisfatto, e che ha indicato la strada di coraggio indicata come
	 * parametro.
	 * 
	 * @param road La strada di coraggio desiderata 
	 * @return Il primo rover meno soddisfatto
	 */
	public Rover getRoverByRoad(int road){
		for (Rover r : queue){
			if (r.getRoad(road)) return r;
		}
		return null;
	}
	
	/**
	 * Aggiorna un rover di cui è stata modificata l'associazione (e quindi il soddisfacimento) 
	 * 
	 * @param e Il rover da aggiornare
	 */
	public void updateRover(Rover e){
		// TODO Qui va controllato...
		queue.remove(e);
		queue.offer(e);
	}
	
	/**
	 * Ritorna il valore di soddisfacimento totale di tutto il sistema.
	 * 
	 * @return Il numero che rappresenta il soddisfacimento globale del sistema
	 */
	public double totalSatisfaction(){
		double sum = 0;
		for (Rover r: queue)
			sum += r.getSatisfaction();
		return sum;
	}
	
	/**
	 * Ritorna il valore di soddisfacimento massimo raggiungibile da tutto il sistema.
	 * 
	 * @return Il numero che rappresenta il soddisfacimento massimo del sistema
	 */
	public double totalMaxSatisfaction(){
		return queue.size()*queue.peek().getMaxSatisfaction();
	}

	/**
	 * Ritorna l'iteratore per iterare sulla collezione di rover contenuti nel sistema
	 */
	@Override
	public Iterator<Rover> iterator() {
		return queue.iterator();
	}
	
	public void print(){
		int i = 0;
		for (Rover r: queue){
			System.out.println(r.getCode());
			i++;
		}
		System.out.println("Total Rover " + i);
	}
}
