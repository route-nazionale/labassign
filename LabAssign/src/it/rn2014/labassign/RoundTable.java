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
			int maxpartecipant, int minpartecipant, Group organizer) {
		super(code, name, maxpartecipant, minpartecipant, organizer);
	}
	
	/**
	 * Funzione che imposta il turno di una tavola rotonda e attiva o disattiva
	 * l'evento per l'assegnamento in funzione del giorno
	 * 
	 * @param t Turno in cui deve essere attiva la tavola rotonda
	 */
	public void setTurn(int t){
		this.turn = t;
		this.setEnabled((this.getWorkingDay() == turn) ? true : false);
	}
	
	/**
	 * Ritorna il turno della tavola rotonda
	 * 
	 * @return Il turno della tavola rotonda
	 */
	public int getTurn(){
		return this.turn;
	}

	@Override
	protected void updateWorkingDay(int day) {
		super.updateWorkingDay(day);
		this.setEnabled((day == turn) ? true : false); 
	}
}
