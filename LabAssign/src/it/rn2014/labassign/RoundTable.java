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
 * @author nicola
 */
public class RoundTable extends Event {

	/**
	 * Costruttore base per una nuova tavola rotonda, che imposta automaticamente il numero
	 * massimo di partecipanti ad una tavola rotonda.
	 * 
	 * @param name Nome della tavola rotonda
	 * @param road Strada di coraggio della tavola rotonda
	 * @param minpartecipant Minimo numero di partecipanti
	 * @param organizer Gruppo organizzatore della tavola rotonda.
	 */
	public RoundTable(String name, int road,
			int minpartecipant, Group organizer) {
		super(name, road, Parameters.ROUNDTABLE_MAX_USER, minpartecipant, organizer);
	}
}
