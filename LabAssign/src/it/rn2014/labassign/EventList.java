package it.rn2014.labassign;

import java.util.Iterator;
import java.util.PriorityQueue;

public class EventList implements Iterable<Event>{

	private PriorityQueue<Event> queue;
	
	public EventList(String file){
		/* Ancora da decidere come importare i dati. */
		queue = new PriorityQueue<>();
		
		/*
		 * for (Lines in file)
		 * 		queue.offer(new Event...)
		 */
	}
	
	public Event getEvent(){
		return queue.peek();
	}
	
	public Event getFirstEventByRoad(int road){
		for (Event e : queue){
			if (e.getRoad() == road) return e;
		}
		return null;
	}
	
	public void updateEvent(Event e){
		queue.remove(e);
		queue.offer(e);
	}

	@Override
	public Iterator<Event> iterator() {
		return queue.iterator();
	}
}
