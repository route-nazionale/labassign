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

import java.util.Iterator;

public class EventIteratorRoad implements Iterator<Event> {

	private int road;
	private Iterator<Event> it;
	
	private Event actual;
	
	public EventIteratorRoad(EventList l, int road){
		this.it = l.iterator();
		this.road = road;
		while(it.hasNext()){
			actual = it.next();
			if (actual.getRoad() == road) break;
		}
		actual = null;
	}
	
	
	@Override
	public boolean hasNext() {
		while(it.hasNext()){
			actual = it.next();
			if (actual.getRoad() == road) return true;
		}
		actual = null;
		return (false);
	}

	@Override
	public Event next() {
		return actual;
	}

	@Override
	public void remove() {throw new RuntimeException("Removing not possibile"); }

}
