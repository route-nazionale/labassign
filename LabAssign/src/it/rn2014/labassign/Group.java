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
 * Classe che rappresenta un gruppo in cui sono censiti una serie di ragazzi
 * 
 * @author Nicola Corti
 */
public class Group {

	/** Nome del gruppo */
	private String name;
	/** Codice del gruppo */ 
	private String code;
	/** Codice del sottocampo */
	private int subcamp;
	/** Codice del gemellaggio */
	private int twinning;
	
	/**
	 * Costruttore base per generare un nuovo gruppo.
	 * 
	 * @param name Nome del gruppo
	 * @param code Codice del gruppo
	 * @param subcamp Codice del sottocampo
	 * @param twinning Codice del gemellaggio
	 */
	public Group(String name, String code, int subcamp, int twinning) {
		super();
		this.name = name;
		this.code = code;
		this.subcamp = subcamp;
		this.twinning = twinning;
	}
	
	/**
	 * Ritorna il sottocampo a cui è assegnato il gruppo
	 * @return Il sottocampo a cui è assegnato il gruppo
	 */
	public int getSubcamp(){ return subcamp; }
	/**
	 * Ritorna il gemellaggio a cui è assegnato il gruppo
	 * @return Il gemellaggio a cui è assegnato il gruppo
	 */
	public int getTwinning(){ return twinning; }
	
	/** 
	 * Ritorna una stringa rappresentante il gruppo
	 */
	@Override
	public String toString(){
		return "Group " + name + " Code: " + code;
	}
}
