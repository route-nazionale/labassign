package it.rn2014.labassign;

import java.util.Iterator;
import java.util.PriorityQueue;

public class RoverList implements Iterable<Rover> {

	private PriorityQueue<Rover> queue;
	
	public RoverList(String file){
		/* Ancora da decidere come importare i dati. */
		queue = new PriorityQueue<>();
		
		/*
		 * for (Lines in file)
		 * 		queue.offer(new Event...)
		 */
	}
	
	public Rover getRover(){
		return queue.poll();
	}
	
	public Rover getRoverByRoad(int road){
		for (Rover r : queue){
			if (r.getRoad(road)) return r;
		}
		return null;
	}
	
	public void updateRover(Rover e){
		queue.offer(e);
	}
	
	public double totalSatisfaction(){
		double sum = 0;
		for (Rover r: queue)
			sum += r.getMaxSatisfaction();
		return sum;
	}
	
	public double totalMaxSatisfaction(){
		return queue.size()*queue.peek().getMaxSatisfaction();
	}

	@Override
	public Iterator<Rover> iterator() {
		return queue.iterator();
	}
}
