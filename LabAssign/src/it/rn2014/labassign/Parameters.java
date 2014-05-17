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
 * Classe che rappresenta i parametri di esecuzione del sistema.
 * 
 * @author Nicola Corti
 */
public class Parameters {
	
	/** Numero massimo di iterazioni del sistema oltre il quale si ferma */
	public static final double MAX_ITERATIONS = 100000;
	/** Valore di soglia di soddisfacimento globale del sistema (in percentuale) */
	public static final double SATISFATION_THRESHOLD = 0.80;
	
	/** Massimo numero di partecipanti ai laboratori */
	public static final int LABORATORY_MAX_USER = 35;
	/** Massimo numero di partecipanti alle tavole rotonde */
	public static final int ROUNDTABLE_MAX_USER = 500;
	
	/** Numero massimo di partecipanti per lo stesso gemellaggio per laboratorio */
	public static final int LABORATORY_MAX_TWINNING_USER = 3;
	/** Numero massimo di partecipanti per lo stesso gemellaggio per tavola rotonda */
	public static final int ROUNDTABLE_MAX_TWINNING_USER = 5;
	
}
