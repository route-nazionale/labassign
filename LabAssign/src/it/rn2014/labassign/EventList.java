package it.rn2014.labassign;

import java.util.PriorityQueue;

public class EventList {

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
		return queue.poll();
	}
	
	public Event getEventByRoad(int road){
		for (Event e : queue){
			if (e.getRoad() == road) return e;
		}
		return null;
	}
	
	public void updateEvent(Event e){
		queue.offer(e);
	}
}
