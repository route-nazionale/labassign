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

/**
 * Classe che rappresenta un rs iscritto alla route nazionale,
 * corredato delle informazioni raccolte in fase di iscrizione e 
 * delle informazioni sulle associazioni calcolate
 * 
 * Nota che ogni rs puo' indicare solo ESATTAMENTE 3 strade di coraggio,
 * e devono essere computate ESATTAMENTE 3 associazioni.
 * 
 * @author Nicola Corti
 *
 */
public class Rover implements Comparable<Rover> {

	/** Nome del rs */
	private String name;
	/** Cognome del rs */
	private String surname;
	/** Codice censimento del rs */
	private double code;
	/** Eta del rs */
	private int age;
	/** Gruppo di appartenenza del rs */
	private Group group;
	
	/** Il rs e' portatore di handicap */
	private boolean handicap;
	/** Il rs e' un novizio */
	private boolean novice;

	/** Il rs ha indicato la preferenza per la 1a strada di coraggio? */
	private boolean road1;
	/** Il rs ha indicato la preferenza per la 2a strada di coraggio? */
	private boolean road2;
	/** Il rs ha indicato la preferenza per la 3a strada di coraggio? */
	private boolean road3;
	/** Il rs ha indicato la preferenza per la 4a strada di coraggio? */
	private boolean road4;
	/** Il rs ha indicato la preferenza per la 5a strada di coraggio? */
	private boolean road5;
	
	/** Primo evento assegnato */
	private Event assign1 = null;
	/** Secondo evento assegnato */
	private Event assign2 = null;
	/** Terzo evento assegnato */
	private Event assign3 = null;
	
	
	/**
	 * Costruttore di base per generare un nuovo rs a partira da dati di input
	 * 
	 * @param name Nome del rs
	 * @param surname Cognome del rs
	 * @param code Codice censimento
	 * @param age Eta del rover
	 * @param handicap Portatore di handicap
	 * @param novice Novizio
	 * @param b Gruppo di apparteneza
	 */
	public Rover(String name, String surname, double code, int age,
			boolean handicap, boolean novice, Group group) {
		this.name = name;
		this.surname = surname;
		this.code = code;
		this.age = age;
		this.handicap = handicap;
		this.novice = novice;
		this.group = group;
	}
	
	/**
	 * Ritorna il gruppo di appartenenza
	 * 
	 * @return Il gruppo di appartenenza
	 */
	public Group getGroup(){ return group; }
	
	/**
	 * Ritorna il codice censimento
	 * 
	 * @return Il codice censimento del ragazzo
	 */
	public double getCode(){ return code; }
	
	
	/**
	 * Ritorna la preferenza indicata relativa ad una delle 5 strade di coraggio
	 * 
	 * @param road Numero della strada di coraggio
	 * @return true se la strada di coraggio e' stata scelta o meno
	 */
	public boolean getRoad(int road){
		switch (road) {
		case 1: return road1;
		case 2: return road2;
		case 3: return road3;
		case 4: return road4;
		case 5: return road5;
		}
		return false;
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
		
		
		if (!(r1 || r2 || r3 || r4 || r5)){
			
			//throw new RuntimeException("PROBLEMA CON LE STRADE DI CORAGGIO: " + this);
			
			this.road1 = true;
			this.road2 = true;
			this.road3 = true;
		}
	}
	
	////////////////////////////////////
	// SODDISFAZIONE
	//
	/**
	 * Ritorna il valore di soddisfazione del ragazzo.
	 * Il valore di soddisfazione del ragazzo e calcolato nel modo seguente:
	 * +5 per ogni evento che soddisfa una delle strade di coraggio del ragazzo,
	 * +3 per ogni coppia di eventi che soddisfano due strade distinte.
	 * 
	 * @return Il totale del soddisfacimento del ragazzo
	 */
	public double getSatisfaction(){
		double sat = 0;
		if (assign1 != null && getRoad(assign1.getRoad())) sat += 5;
		if (assign2 != null && getRoad(assign2.getRoad())) sat += 5;
		if (assign3 != null && getRoad(assign3.getRoad())) sat += 5;
		if (assign1 != null && assign2 != null && assign1.getRoad() != assign2.getRoad()) sat += 2;
		if (assign1 != null && assign3 != null && assign1.getRoad() != assign3.getRoad()) sat += 2;
		if (assign2 != null && assign3 != null && assign2.getRoad() != assign3.getRoad()) sat += 2;
		return sat;
	}
	//
	/**
	 * Ritorna il massimo del soddisfacimento del ragazzo (Il valore 21).
	 * @return Soddisfacimento massimo (21).
	 */
	public double getMaxSatisfaction(){
		return 21;
	}
	//
	//
	////////////////////////////////////
	
	/**
	 * Calcola se un evento e' adatto ad un ragazzo o meno, in base ai vincoli imposti dall'analisi.
	 * I vincoli hanno associato un livello di priorita' che puo' essere variabile.
	 * 
	 * Il livello di priorita' viene dato in input cosi' che sia possibile selezionare a
	 * quale livello di profondita' effettuare il controllo.
	 * 
	 * @param e L'evento in questione.
	 * @param day Il giorno in cui si vuole assegnare l'evento
	 * @param priority Il livello di priorita' a cui effettuare il controllo
	 * @return True se l'evento e' adatto, false altrimenti
	 */
	public boolean isSuitable(Event e, int day, int priority){
		
		
		if (e instanceof Lab){
			
			// Caso Laboratorio
			Lab l = (Lab) e;
			
			if (priority <= 1 && !l.getRoadMask(this)) return false;
			
			if (priority <= 3 && l.getMaxAge() < this.age) return false;
			if (priority <= 3 && l.getMinAge() > this.age) return false;
			
			if (priority <= 2 && l.getPartecipantsTwinnings(day, this) >= Parameters.LABORATORY_MAX_TWINNING_USER) return false;
			
			if (priority <= 2 && (l.getSuitableNovice() || l.getMinAge() > 17) == false && this.novice == true) return false;
			
			
			if (priority <= 4 && l.getSuitableHandicap() == false && this.handicap == true) return false;
			
			//if (priority <= 5 && l.getSubcamp() != this.group.getSubcamp()) return false;
			
			if (priority <= 5 && l.isFull(day)) return false;
			
			if (priority <= 5){
				if (day == 2 && l.getCode().contentEquals(this.assign1.getCode())) 
					return false;
				if (day == 3 && (l.getCode().contentEquals(this.assign1.getCode()) ||
								 l.getCode().contentEquals(this.assign2.getCode())))
					return false;
			}
			
			
		} else if (e instanceof RoundTable) {
			
			// Caso Tavola Rotonda
			RoundTable r = (RoundTable) e;
			if (priority <= 5 && r.isFull(day)) return false;
			if (priority <= 2 && r.getPartecipantsTwinnings(day, this) >= Parameters.ROUNDTABLE_MAX_USER) return false;
			
			if (priority <= 5){
				if (day == 2 && r.getCode().contentEquals(this.assign1.getCode())) 
					return false;
				if (day == 3 && (r.getCode().contentEquals(this.assign1.getCode()) ||
								 r.getCode().contentEquals(this.assign2.getCode())))
					return false;
			}
			if (priority <= 5 && day == 3 && (this.assign2 instanceof RoundTable) && (this.assign1 instanceof RoundTable)) return false;
		}
		
		return true;
	}
	
	/**
	 * Assegna un evento al ragazzo, aggiornando la lista di iscritti all'evento
	 * 
	 * @param day Giorno in cui si vuole assegnare l'evento
	 * @param evt Evento da assegnare
	 */
	public void assignToEvent(int day, Event evt){
		switch (day) {
		case 1:
			assign1 = evt;
			break;
		case 2:
			assign2 = evt;
			break;
		case 3:
			assign3 = evt;
			break;
		default:
			break;
		}
		evt.assign(day, this);
	}
	
	/**
	 * Controlla se e' stato assegnato un evento per il giorno in questione
	 * 
	 * @param day Giorno di cui si vuole controllare se e' stato assegnato un evento
	 * @return True se e' stato assegnato un evento, false altrimenti
	 */
	public boolean isAssigned(int day){
		switch (day) {
		case 1: return assign1 != null;
		case 2: return assign2 != null;
		case 3: return assign3 != null;
		default: return false;
		}
	}
	
	/**
	 * Controlla se un rs e' completamente assegnato
	 * 
	 * @return True se tutti i giorni sono assegnati, false altrimenti.
	 */
	public boolean isFullyAssigned(){
		return (assign1 != null && assign2 != null && assign3 != null);
	}

	/**
	 * Ritorna una delle strade di coraggio scelte in modo casuale
	 * 
	 * @return Una delle 3 strade scelte dal rs
	 */
	public int getRandomRoad(){
		int rand = (int) (Math.random()*100);
		while(true){
			if (rand < 20 && road1) return 1;
			if (rand >= 20 && rand < 40 && road2) return 2;
			if (rand >= 40 && rand < 60 && road3) return 3;
			if (rand >= 60 && rand < 80 && road4) return 4;
			if (rand >= 80 && rand <= 100 && road5) return 5;
			rand = (int) (Math.random()*100);
		}
	}
	
	
	/** 
	 * Ritorna una stringa rappresentante il rover da completare
	 */
	@Override
	public String toString() {
		String result = "ROVER: " + code + " - " + name + " - " + surname + " - SATISF: " + this.getSatisfaction();
		return result;
	}

	/**
	 * Esegue la comparazione fra due rover, indicando come minore quello con soddisfazione piu' bassa.
	 * (Interfaccia Comparable)
	 * 
	 * @param arg0 Il secondo rover con cui comparare
	 */
	@Override
	public int compareTo(Rover arg0) {
		double sat1 = this.getSatisfaction();
		double sat2 = arg0.getSatisfaction();
		if (sat1 < sat2 ) return -1;
		if (sat1 > sat2 ) return 1;
		return 0;
	}
}