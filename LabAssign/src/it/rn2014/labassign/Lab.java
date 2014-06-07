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

/**
 * Classe per gestire un generico evento a cui devono essere assegnati dei ragazzi.
 * 
 * @author Nicola Corti
 */
public class Lab extends Event {
	
	/** Indica se adatto a novizi */
	private boolean suitablenovice;
	/** Indica se adatto a portatori di handicap */
	private boolean suitablehandicap;
	/** Eta massima del partecipante */
	private int maxage = 99;
	/** Eta minima del partecipante */
	private int minage = 0;
	/** Sottocampo di appartenenza */
	private int subcamp;
	
	
	/** 
	 * Costruttore base che crea un nuovo Laboratorio
	 * 
	 * @param code Codice del laboratorio
	 * @param name Nome del laboratorio
	 * @param minpartecipant Minimo numero di partecipanti
	 * @param maxpartecipant Massimo numero di partecipanti
	 * @param organizer Gruppo organizzatore dell'evento
	 * @param novice Adatto a novizi (booleano)
	 * @param handicap Adatto ad handicap (boolean)
	 * @param maxage Eta massima
	 * @param minage Eta minima
	 * @param subcamp Sottocampo in cui e' posizionato
	 */
	public Lab(String code, String name, int minpartecipant, int maxpartecipant, 
			Group organizer, boolean novice, boolean handicap, int maxage, int minage, int subcamp) {
		super(code, name, maxpartecipant, minpartecipant, organizer);
		
		this.suitablenovice = novice;
		this.suitablehandicap = handicap;
		
		this.maxage = maxage;
		this.minage = minage;
		this.subcamp = subcamp;
	}
	
	/**
	 * Ritorna un booleano per indicare se l'evento e' adatto o meno ad un novizio
	 * 
	 * @return True se adatto ad un novizio, false altrimenti
	 */
	public boolean getSuitableNovice(){ return suitablenovice; }
	
	/**
	 * Ritorna un booleano per indicare se l'evento e' adatto o meno ad un portatore di handicap
	 * 
	 * @return True se adatto ad un portatore di handicap, false altrimenti
	 */
	public boolean getSuitableHandicap(){ return suitablehandicap; }
	
	/**
	 * Ritorna l'eta' massima per la partecipazione all'evento
	 * 
	 * @return Intero che rappresenta l'eta' massima per l'evento
	 */
	public int getMaxAge(){ return maxage; }
	
	/**
	 * Ritorna l'eta' minima per la partecipazione all'evento
	 * 
	 * @return Intero che rappresenta l'eta' minima per l'evento
	 */
	public int getMinAge(){ return minage; }
	
	/**
	 * Ritorna il sottocampo di appartenenza
	 * 
	 * @return Intero che rappresenta il sottocampo del laboratorio (0 se ancora non assegnato).
	 */
	public int getSubcamp(){ return subcamp; }

	/**
	 * Imposta il sottocampo di appartenenza di un evento
	 * 
	 * @param camp Il sottocampo in cui deve essere svolto un evento
	 */
	public void setSubcamp(int camp) {
		this.subcamp = camp;
	}

	@Override
	public void print(){
		super.print();
		System.out.println("~~~ SOTTOCAMPO: " + this.subcamp);
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}
}
