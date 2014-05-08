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
