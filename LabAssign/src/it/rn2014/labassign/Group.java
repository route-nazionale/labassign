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
 * NOTA: Due unita' appartenenti allo stesso gruppo agesci, figurano come due istanze della classe Group
 * 
 * @author Nicola Corti
 */
public class Group {

	/** Nome del gruppo */
	private String name;
	/** Codice del gruppo */ 
	private String code;
	/** Codice dell'unita' */ 
	private String unity;
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
	public Group(String name, String code, String unity, int subcamp, int twinning) {
		super();
		this.name = name;
		this.code = code;
		this.unity = unity;
		this.subcamp = subcamp;
		this.twinning = twinning;
	}
	
	/**
	 * Ritorna il codice del gruppo (nel formato lettera + 4 numeri) 
	 * 
	 * @return Il codice agesci del gruppo
	 */
	public String getCode(){
		return this.code;
	}
	
	/**
	 * Ritorna il codice dell'unita (nel formato lettera + numero) 
	 * 
	 * @return Il codice dell'unita' considerata
	 */
	public String getUnity(){
		return this.unity;
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
	 * Controlla se il gruppo istanza corrisponde alla coppia (codice, unita') data in input.
	 * 
	 * @param code Codice del gruppo
	 * @param unity Codice dell'unita'
	 * @return True se il gruppo in questione e' lo stesso, false altrimenti.
	 */
	public boolean sameGroup(String code, String unity){
		return (this.code.contentEquals(code) && this.unity.contentEquals(unity));
	}
	
	
	/** 
	 * Ritorna una stringa rappresentante il gruppo
	 */
	@Override
	public String toString(){
		return "Gruppo: " + name + " Code: " + code + " Unita: " + unity;
	}
	
	/**
	 * Effettua la comparazione fra due gruppi
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Group){
			Group g = (Group) obj;
			return (g.code.contentEquals(this.code) && g.unity.contentEquals(this.unity));
		}
		return false;
	}
}
