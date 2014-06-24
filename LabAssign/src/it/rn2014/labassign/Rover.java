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

import it.rn2014.labassign.Parameters;

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
	private int code;
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
	/** Indica se deve essere assegnato il 1' evento */
	private boolean to_assign1 = true;
	/** Indica se deve essere assegnato il 2' evento */
	private boolean to_assign2 = true;
	/** Indica se deve essere assegnato il 3' evento */
	private boolean to_assign3 = true;
	/** Priorita' Primo evento assegnato */
	private int prio_1 = 0;
	/** Priorita' Secondo evento assegnato */
	private int prio_2 = 0;
	/** Priorita' Terzo evento assegnato */
	private int prio_3 = 0;	
	
	/**
	 * Costruttore di base per generare un nuovo rs a partira da dati di input
	 * 
	 * @param name Nome del rs
	 * @param surname Cognome del rs
	 * @param code Codice censimento
	 * @param age Eta del rover
	 * @param handicap Portatore di handicap
	 * @param novice Novizio
	 * @param group Gruppo di apparteneza
	 */
	public Rover(String name, String surname, int code, int age,
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
	public int getCode(){ return code; }
	
	/**
	 * Ritorna true se il rover e' un novizio
	 * 
	 * @return True se il rover e' un novizio
	 */
	public boolean isNovice(){ return novice; }
	

	
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
	
	/**
	 * Permette di impostare i flag che non fanno assegnare un ragazzo se gia' vincolato
	 * 
	 * @param b1 Vincolo sulla prima scelta
	 * @param b2 Vincolo sulla seconda scelta
	 * @param b3 Vincolo sulla terza scelta
	 */
	public void setNoAssign(boolean b1, boolean b2, boolean b3){
		this.to_assign1 = b1;
		this.to_assign1 = b2;
		this.to_assign1 = b3;
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
			
			if (priority <= Parameters.PRIO_ROAD && !l.getRoadMask(this)) return false;
			
			if (priority <= Parameters.PRIO_ROAD_2){
				if (day == 2 && !l.getRoadMask(this)) return false;
				if (day == 2 && !this.assign1.getRoadMask(this)) return false;
				if (day == 3 && !l.getRoadMask(this)) return false;
				if (day == 3 && !this.assign2.getRoadMask(this)) return false;
				if (day == 3 && (this.assign1.getRoad() == this.assign2.getRoad()) && (this.assign1.getRoad() == l.getRoad())) return false;
			}
			
			if (priority <= Parameters.PRIO_ROAD_3 && day == 3){
				if (!this.assign1.getRoadMask(this)) return false;
				if (!this.assign2.getRoadMask(this)) return false;
				if (!l.getRoadMask(this)) return false;
				if (l.getRoad() == this.assign1.getRoad()) return false;
				if (l.getRoad() == this.assign2.getRoad()) return false;
				if (this.assign1.getRoad() == this.assign2.getRoad()) return false;
			}
			
			if (priority <= Parameters.PRIO_AGE && l.getMaxAge() < this.age) return false;
			
			if (priority <= Parameters.PRIO_AGE && l.getMinAge() > this.age) return false;
			
			if (priority <= Parameters.PRIO_TWIN_LAB && l.getPartecipantsTwinnings(day, this) >= Parameters.LABORATORY_MAX_TWINNING_USER) return false;
			
			// Inibito in seguito alla chiaccherata con Stefano 
			// il 21/06
			//if (priority <= Parameters.PRIO_NOVICE && (l.getSuitableNovice() || l.getMinAge() > 17) == false && this.novice == true) return false;
			
			if (priority <= Parameters.PRIO_HANDICAP && l.getSuitableHandicap() == false && this.handicap == true) return false;
			
			if (priority <= Parameters.PRIO_QUART && this.group != null && l.getSubcamp() != this.group.getSubcamp()) return false;
			
			if (priority <= Parameters.PRIO_FULL && l.isFull(day)) return false;
			
			if (priority <= Parameters.PRIO_FULL_2 && l.isOverFull(day)) return false;
			
			if (priority <= Parameters.PRIO_EQUALS){
				if (day == 2 && l.getCode().contentEquals(this.assign1.getCode())) 
					return false;
				if (day == 3 && (l.getCode().contentEquals(this.assign1.getCode()) ||
								 l.getCode().contentEquals(this.assign2.getCode())))
					return false;
			}
			
			
		} else if (e instanceof RoundTable) {
			
			// Caso Tavola Rotonda
			RoundTable r = (RoundTable) e;
			if (priority <= Parameters.PRIO_FULL && r.isFull(day)) return false;
			
			if (priority <= Parameters.PRIO_TWIN_TAV && r.getPartecipantsTwinnings(day, this) >= Parameters.ROUNDTABLE_MAX_TWINNING_USER) return false;
			
			if (priority <= Parameters.PRIO_EQUALS){
				if (day == 2 && r.getCode().contentEquals(this.assign1.getCode())) 
					return false;
				if (day == 3 && (r.getCode().contentEquals(this.assign1.getCode()) ||
								 r.getCode().contentEquals(this.assign2.getCode())))
					return false;
			}
			if (priority <= Parameters.PRIO_ONE_LAB && day == 3 && (this.assign2 instanceof RoundTable) && (this.assign1 instanceof RoundTable)) return false;
		}
		
		return true;
	}
	
	/**
	 * Assegna un evento al ragazzo, aggiornando la lista di iscritti all'evento
	 * 
	 * @param day Giorno in cui si vuole assegnare l'evento
	 * @param evt Evento da assegnare
	 * @param prio La priorita' con cui e' stato effettuato l'assegnamento
	 */
	public void assignToEvent(int day, Event evt, int prio){
		switch (day) {
		case 1:
			assign1 = evt;
			this.prio_1 = prio;
			break;
		case 2:
			assign2 = evt;
			this.prio_2 = prio;
			break;
		case 3:
			assign3 = evt;
			this.prio_3 = prio;
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
		String result = code + "\t " + name + ", " + surname + " GRUPPO: " + this.group +" - SATISF: " + this.getSatisfaction();
		return result;
	}
	
	/**
	 * Stampa una rappresentazione simbola del rover su standard output
	 */
	public void print(){
		System.out.println(code + "\t " + name + ", " + surname + " GRUPPO: " + this.group +" - SATISF: " + this.getSatisfaction());
		System.out.println("1)" + this.assign1.getCode() + " PRIO: " + this.prio_1 + " 2)" + this.assign2.getCode() + " PRIO: " + this.prio_2 + " 3)" + this.assign1.getCode() + " PRIO: " + this.prio_3); 
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

	/**
	 * Ritorna true se il rover deve essere assegnato nel giorno in questione.
	 * 
	 * @param day Giorno da considerare
	 * @return True se il rover deve essere assegnato, false altrimenti
	 */
	public boolean toBeAssigned(int day){
		if (day == 1) return to_assign1;
		if (day == 2) return to_assign2;
		if (day == 3) return to_assign3;
		return false;
	}
	
	/**
	 * Permette di assegnare un vincolo di preassegnamento su un ragazzo
	 * 
	 * @param e Evento che deve essere preassegnato
	 * @param day Giorno che deve essere preassegnato
	 */
	public void setConstraint(Event e, int day) {
		switch (day) {
		case 1:
			this.assign1 = e;
			this.to_assign1 = false;
			this.prio_1 = -1;
			break;
		case 2:
			this.assign2 = e;
			this.to_assign2 = false;
			this.prio_2 = -1;
			break;
		case 3:
			this.assign3 = e;
			this.to_assign3 = false;
			this.prio_3 = -1;
			break;
		}
	}

	/**
	 * Ritorna il codice evento del giorno desiderato.
	 * Deve essere effettuato dopo un assegnamento.
	 * 
	 * @param day Giorno dell'evento di cui si vuole il codice
	 * @return La stringa che rappresenta il codice dell'evento
	 */
	public String getEventCode(int day) {
		switch (day) {
		case 1: return assign1.getCode();
		case 2: return assign2.getCode();
		case 3: return assign3.getCode();
		}
		return null;
	}
	
	/**
	 * Ritorna la priorita' dell'assegnamento relativo al giorno desiderato.
	 * Deve essere effettuato dopo un assegnamento.
	 * 
	 * @param day Giorno dell'evento di cui si vuole la priorita dell'assegnamento
	 * @return Il livello di priorita' con cui e' stato effettuato l'assegnamento
	 */
	public int getPriority(int day) {
		switch (day) {
		case 1: return prio_1;
		case 2: return prio_2;
		case 3: return prio_3;
		}
		return -1;
	}
}