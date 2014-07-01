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
 * Classe che estende un evento e che rappresenta una tavola rotonda.
 * 
 * @author Nicola Corti
 */
public class RoundTable extends Event {

	/** Turno in cui viene effettuata una tavola rotonda */
	private int turn = 0;
	
	/** Rappresenta il secondo gruppo che organizza la tavola rotonda (se presente, null altrimenti) */
	private Group organizer2 = null;
	
	/**
	 * Costruttore base per una nuova tavola rotonda, che imposta automaticamente il numero
	 * massimo di partecipanti ad una tavola rotonda.
	 * 
	 * @param code Codice della tavola rotonda
	 * @param name Nome della tavola rotonda
	 * @param minpartecipant Minimo numero di partecipanti
	 * @param organizer Gruppo organizzatore della tavola rotonda.
	 */
	public RoundTable(String code, String name, 
			int minpartecipant, Group organizer) {
		super(code, name, Parameters.ROUNDTABLE_MAX_USER, minpartecipant, organizer);
	}
	
	/**
	 * Costruttore base per una nuova tavola rotonda, che permette di impostare il numero
	 * massimo di partecipanti ad una tavola rotonda.
	 * 
	 * @param code Codice della tavola rotonda
	 * @param name Nome della tavola rotonda
	 * @param minpartecipant Minimo numero di partecipanti
	 * @param maxpartecipant Massimo numero di partecipanti
	 * @param organizer Gruppo organizzatore della tavola rotonda.
	 */
	public RoundTable(String code, String name, 
			int maxpartecipant, int minpartecipant, Group organizer, Group organizer2) {
		super(code, name, maxpartecipant, minpartecipant, organizer);
		this.organizer2 = organizer2;
	}
	
	/**
	 * Funzione che imposta il turno di una tavola rotonda e attiva o disattiva
	 * l'evento per l'assegnamento in funzione del giorno
	 * 
	 * @param t Turno in cui deve essere attiva la tavola rotonda
	 */
	public void setTurn(int t){
		this.turn = t;
		if (1 == turn)
			this.setEnabled(true);
		else
			this.setEnabled(false);
	}
	
	/**
	 * Ritorna il turno della tavola rotonda
	 * 
	 * @return Il turno della tavola rotonda
	 */
	public int getTurn(){
		return this.turn;
	}
	
	/**
	 * Ritorna il secondo gruppo organizzatore dell'evento
	 * 
	 * @return Riferimento al gruppo (Istanza di Group) che effettuera' l'evento
	 */
	public Group getOrganizer2() {
		return organizer2;
	}

	/**
	 * Imposta il secondo gruppo che organizzera' l'evento (se presente)
	 * Impostare a null nel caso in cui non sia presente il secondo gruppo.
	 * 
	 * @param organizer Riferimento al gruppo (Istanza di Group) che effettuera' l'evento
	 */
	public void setOrganizer2(Group organizer) {
		this.organizer2 = organizer;
	}
	

	@Override
	protected void updateWorkingDay(int day) {
		super.updateWorkingDay(day);
		if (day == turn)
			this.setEnabled(true);
		else 
			this.setEnabled(false);
		
	}
}
