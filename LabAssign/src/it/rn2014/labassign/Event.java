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

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che rappresenta un evento generico che deve essere assegnato ad un rover, con tutti
 * i dati relativi a nome, strada di coraggio, etc...
 * 
 * @author Nicola Corti
 */
public class Event implements Comparable<Event> {

	
	/** Codice dell'evento */
	private String code;
	/** Nome dell'evento */
	private String name;
	
	/** Numero massimo di partecipanti */
	private int maxpartecipant;
	/** Numero minimo di partecipanti ammesso */
	private int minpartecipant;
	
	/** Gruppo organizzatore */
	private Group organizer;
	
	/** Elenco dei partecipanti alla prima realizzazione dell'evento */
	private List<Rover> partecipant1;
	/** Elenco dei partecipanti alla seconda realizzazione dell'evento */
	private List<Rover> partecipant2;
	/** Elenco dei partecipanti alla terza realizzazione dell'evento */
	private List<Rover> partecipant3;
	
	/** Il laboratorio e' svolto per la 1a strada di coraggio? */
	private boolean road1;
	/** Il laboratorio e' svolto per la 2a strada di coraggio? */
	private boolean road2;
	/** Il laboratorio e' svolto per la 3a strada di coraggio? */
	private boolean road3;
	/** Il laboratorio e' svolto per la 4a strada di coraggio? */
	private boolean road4;
	/** Il laboratorio e' svolto per la 5a strada di coraggio? */
	private boolean road5;
	
	/** Rappresentazione numerica della strada di coraggio */
	private int roadnum;
	
	/** IMPLEMENTAZIONE Indica il giorno con cui si sta lavorando, al fine di 
	 *  realizzare l'ordinamento fra due eventi */
	private int workingday = 1;
	/** IMPLEMENTAZIONE Indica se un evento deve essere considerato ai fini dell'assegnamento oppure no */
	private boolean enabled = true;
	
	/**
	 * Costruttore per generare un nuovo evento fornendo tutti i campi necessari
	 * 
	 * @param name Nome dell'evento
	 * @param maxpartecipant Vincolo sul massimo dei partecipanti
	 * @param minpartecipant Vincolo sul minimo dei paretcipanti
	 * @param organizer Gruppo organizzatore dell'evento
	 */
	public Event(String code, String name, int maxpartecipant, int minpartecipant,
			Group organizer) {
		super();
		this.code = code;
		this.name = name;
		this.maxpartecipant = maxpartecipant;
		this.minpartecipant = minpartecipant;
		this.organizer = organizer;
		this.partecipant1 = new ArrayList<>();
		this.partecipant2 = new ArrayList<>();
		this.partecipant3 = new ArrayList<>();
	}

	/**
	 * Ritorna il codice dell'evento
	 * 
	 * @return Il codice testuale dell'evento
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * Aggiorna contemporaneamente i dati su tutte le strade di coraggio
	 * 
	 * @param r1 Preferenza da assegnare alla prima strada di coraggio
	 * @param r2 Preferenza da assegnare alla seconda strada di coraggio
	 * @param r3 Preferenza da assegnare alla terza strada di coraggio
	 * @param r4 Preferenza da assegnare alla quarta strada di coraggio
	 * @param r5 Preferenza da assegnare alla quinta strada di coraggio
	 */
	public void setRoadsPreference(boolean r1, boolean r2, boolean r3, boolean r4, boolean r5){
		this.road1 = r1;
		this.road2 = r2;
		this.road3 = r3;
		this.road4 = r4;
		this.road5 = r5;
		if (r1) roadnum = 1;
		if (r2) roadnum = 2;
		if (r3) roadnum = 3;
		if (r4) roadnum = 4;
		if (r5) roadnum = 5;
		
	}
	
	
	/**
	 * Controlla se un evento è full per un dato giorno in input
	 * 
	 * @param day Giorno per cui si vuole controllare
	 * @return True se l'evento e full, false altrimenti
	 */
	public boolean isFull(int day){
		
		switch (day) {
		case 1: return partecipant1.size() >= maxpartecipant;
		case 2: return partecipant2.size() >= maxpartecipant;
		case 3: return partecipant3.size() >= maxpartecipant;
		default: return false;
		}
	}
	
	/**
	 * Controlla se un evento è ancora vuoto per un dato giorno in input
	 * 
	 * @param day Giorno per cui si vuole controllare
	 * @return True se l'evento e ancora vuoto, false altrimenti
	 */
	public boolean isStillEmpty(int day){
		switch (day) {
		case 1: return partecipant1.size() <= minpartecipant;
		case 2: return partecipant2.size() <= minpartecipant;
		case 3: return partecipant3.size() <= minpartecipant;
		default: return false;
		}
	}
	
	/**
	 * Assegna all'evento un dato rover in un dato giorno.
	 * 
	 * @param day Giorno per cui si vuole assengnare
	 * @param r Rover che si vuole assegnare
	 */
	public void assign(int day, Rover r){
		switch (day) {
		case 1: partecipant1.add(r); break;
		case 2: partecipant2.add(r); break;
		case 3: partecipant3.add(r); break;
		}
	}
	
	/**
	 * Ritorna il numero di partecipanti dello stesso gemellaggio registrati in un dato giorno per
	 * un evento.
	 * 
	 * @param day Giorno di cui si vuole controllare
	 * @param r1 Rover di cui si vuole controllare
	 * @return Il numero di partecipanti all'evento nel giorno DAY dello stesso gemellaggio di R1
	 */
	public int getPartecipantsTwinnings(int day, Rover r1) {
		
		List<Rover> list = null;
		int count = 0;
		
		switch (day) {
		case 1: list = partecipant1; break;
		case 2: list = partecipant2; break;
		case 3: list = partecipant3; break;
		}
		
		for (Rover r2 : list){
			if (r2.getGroup() != null && r1.getGroup() != null){
				if (r2.getGroup().getTwinning() == r1.getGroup().getTwinning()) count++;
			}
		}
		
		return count;
	}

	/**
	 * Aggiorna il working day.
	 * Solamente a scopo IMPLEMENTATIVO.
	 * 
	 * @param day Nuovo working day
	 */
	protected void updateWorkingDay(int day) {
		workingday = day;
	}
	/**
	 * Ritorna il working day.
	 * Solamente a scopo IMPLEMENTATIVO.
	 * 
	 * @return Il working day
	 */
	protected int getWorkingDay() {
		return workingday;
	}
	
	/**
	 * Ritorna la stringa che rappresenta l'evento
	 * @return stringa che rappresenta l'evento
	 */
	@Override
	public String toString(){
		return "EV: " + name + " COD: " + code + " PART: " + partecipant1.size() + " MAX " + maxpartecipant;
	}


	/**
	 * Metodo dell'interfaccia Comparable, che confronta due eventi, e ritorna quale dei due
	 * è minore rispetto all'altro in relazione a:
	 * - Se uno dei due eventi è ancora vuoto (numero di partecipanti ancora inferiore rispetto al limite)
	 * - In base a quale dei due ha la lista dei partecipanti più ridotta
	 * 
	 * IMPLEMENTAZIONE: utilizza la variabile working day.
	 */
	@Override
	public int compareTo(Event arg0) {
		List<Rover> l1 = null;
		List<Rover> l2 = null;
		switch (workingday) {
		case 1:
			l1 = this.partecipant1;
			l2 = arg0.partecipant1;
			break;
		case 2:
			l1 = this.partecipant2;
			l2 = arg0.partecipant2;
			break;
		case 3:
			l1 = this.partecipant3;
			l2 = arg0.partecipant3;
			break;
		}
		
		// Condizioni che indicano a quale evento dare priorita'
		if (this.isStillEmpty(workingday) && !arg0.isStillEmpty(workingday)) return -1;
		if (!this.isStillEmpty(workingday) && arg0.isStillEmpty(workingday)) return 1;
		if (l1.size() < l2.size()) return -1;
		else if (l1.size() > l2.size()) return 1;
		else return 0;
	}

	/**
	 * Ritorna la strada di coraggio in forma numerica
	 * 
	 * @return La strada di coraggio dell'evento.
	 */
	public int getRoad() {
		return roadnum;
	}
	
	/**
	 * Calcola il risultato fra una maschera di bit di strade di coraggio e la strada di coraggio dell'evento
	 * 
	 * @param r1 Bit della prima strada di coraggio
	 * @param r2 Bit della seconda strada di coraggio
	 * @param r3 Bit della terza strada di coraggio
	 * @param r4 Bit della quarta strada di coraggio
	 * @param r5 Bit della quinta strada di coraggio
	 * @return True se c'e' un match, false altrimenti
	 */
	public boolean getRoadMask(boolean r1, boolean r2, boolean r3, boolean r4, boolean r5){
		return (r1 && road1)||(r2 && road2)||(r3 && road3)||(r4 && road4)||(r5 && road5);
	}
	
	/**
	 * Calcola il risultato fra le scelte delle strade di coraggio di un ragazzo e la strada di coraggio dell'evento
	 * 
	 * @param r Il rover da confrontare
	 * @return True se c'e' un match, false altrimenti
	 */
	public boolean getRoadMask(Rover r){
		return ((r.getRoad(1) && road1)||(r.getRoad(2) && road2)||(r.getRoad(3) && road3)||(r.getRoad(4) && road4)||(r.getRoad(5) && road5));
	}

	/**
	 * Stampa la scheda evento associata.
	 * Mostrando l'elenco dei partecipanti nei 3 turni.
	 */
	public void print() {
		System.out.println("~~~~~~~~~~~~ SCHEDA EVENTO ~~~~~~~~~~~");
		System.out.println("~ Code: " + this.code);
		System.out.println("~ Nome: " + this.name);
		System.out.println("~ Strada: " + this.roadnum);
		System.out.println("~~~ TURNO 1 ~~~");
		int i = 0;
		for (Rover r: this.partecipant1){
			System.out.println("~" + i + ") " + r);
			i++;
		}
		System.out.println("~~~ TURNO 2 ~~~");
		i = 0;
		for (Rover r: this.partecipant2){
			System.out.println("~" + i + ") " + r);
			i++;
		}
		System.out.println("~~~ TURNO 3 ~~~");
		i = 0;
		for (Rover r: this.partecipant1){
			System.out.println("~" + i + ") " + r);
			i++;
		}
	}
	
	/**
	 * Ritorna il gruppo organizzatore dell'evento
	 * 
	 * @return Riferimento al gruppo (Istanza di Group) che effettuera' l'evento
	 */
	public Group getOrganizer() {
		return organizer;
	}

	/**
	 * Imposta il gruppo che organizzera' l'evento (se presente)
	 * 
	 * @param organizer Riferimento al gruppo (Istanza di Group) che effettuera' l'evento
	 */
	public void setOrganizer(Group organizer) {
		this.organizer = organizer;
	}

	
	/**
	 * Permette di abilitare o disabilitare un evento per l'assegnamento
	 * 
	 * @param t True se l'evento deve essere abilitato, false altrimenti
	 */
	protected void setEnabled(boolean t){ this.enabled = t; }
	/**
	 * Ritorna true se l'evento e' abilitato ad essere assegnato oppure deve essere scartato.
	 * IMPLEMENTAZIONE: invocazioni differenti del metodo possono dare risultati differenti in funzione
	 * del Working day
	 * 
	 * @return True se l'evento e' abilitato, false altrimenti
	 */
	protected boolean isEnabled(){ return this.enabled; }

	public int getMaxPartecipant() {
		return maxpartecipant;
	}
}
