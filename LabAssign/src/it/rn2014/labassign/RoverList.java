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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Classe che rappresenta una collezione di Rover che vengono considerati per
 * l'esecuzione all'interno dell'algoritmo.
 * 
 * @author Nicola Corti
 */
public class RoverList implements Iterable<Rover> {

	/** Lista di priorità dei rover */
	private ArrayList<Rover> queue;
	
	/**
	 * Costruttore che inizializza la lista dei rover da considerare.
	 */
	public RoverList(){
		queue = new ArrayList<Rover>();
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
	 * Ritorna il primo rover
	 * 
	 * @return Il primo rover
	 */
	public Rover getRover(){
		return queue.get(0);
	}
	
	/**
	 * Ordina la lista dei ragazzi in funzione dei valori di priorita' risultanti
	 */
	public void sort(){
		Collections.sort(queue);
	}
	
	
	/**
	 * Ritorna il rover dato il codice censimento in input
	 * 
	 * @return Il rover richiesto, null se il rover non viene trovato
	 */
	public Rover getRover(String codice){
		for (Rover r: queue)
			if (r.getCode() == Double.parseDouble(codice)) return r;
		return null;
	}
	
	/**
	 * Ritorna il rover dato il codice censimento in input
	 * 
	 * @return Il rover richiesto, null se il rover non viene trovato
	 */
	public Rover getRover(int codice){
		for (Rover r: queue)
			if (r.getCode() == codice) return r;
		return null;
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
		return queue.size()*queue.get(0).getMaxSatisfaction();
	}

	/**
	 * Ritorna l'iteratore per iterare sulla collezione di rover contenuti nel sistema
	 */
	@Override
	public Iterator<Rover> iterator() {
		return queue.iterator();
	}
	
	/**
	 * Stampa tutti i rover nella lista
	 */
	public void print(){
		int i = 0;
		for (Rover r: queue){
			System.out.println(r.getCode());
			i++;
		}
		System.out.println("Total Rover " + i);
	}
}
