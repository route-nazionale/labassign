/**
 * 
 */
package it.rn2014.labassign;

import java.util.ArrayList;
import java.util.List;

public class Event implements Comparable<Event> {

	private String name;
	private int road;
	
	private int maxpartecipant;
	private int minpartecipant;
	
	private Group organizer;
	
	private List<Rover> partecipant1;
	private List<Rover> partecipant2;
	private List<Rover> partecipant3;
	
	private int workingday = 1;
	
	public Event(String name, int road, int maxpartecipant, int minpartecipant,
			Group organizer) {
		super();
		this.name = name;
		this.road = road;
		this.maxpartecipant = maxpartecipant;
		this.minpartecipant = minpartecipant;
		this.organizer = organizer;
		this.partecipant1 = new ArrayList<>();
		this.partecipant2 = new ArrayList<>();
		this.partecipant3 = new ArrayList<>();
	}

	public int getRoad() {
		return road;
	}
	
	public boolean isFull(int day){
		switch (day) {
		case 1: return partecipant1.size() >= maxpartecipant;
		case 2: return partecipant2.size() >= maxpartecipant;
		case 3: return partecipant3.size() >= maxpartecipant;
		default: return false;
		}
	}
	
	public boolean isStillEmpty(int day){
		switch (day) {
		case 1: return partecipant1.size() <= minpartecipant;
		case 2: return partecipant2.size() <= minpartecipant;
		case 3: return partecipant3.size() <= minpartecipant;
		default: return false;
		}
	}
	
	public void assign(int day, Rover r){
		switch (day) {
		case 1: partecipant1.add(r); break;
		case 2: partecipant2.add(r); break;
		case 3: partecipant3.add(r); break;
		}
	}
	
	public int getPartecipantsTwinnings(int day, Rover r1) {
		
		List<Rover> list = null;
		int count = 0;
		
		switch (day) {
		case 1: list = partecipant1; break;
		case 2: list = partecipant2; break;
		case 3: list = partecipant3; break;
		}
		
		for (Rover r2 : list)
			if (r2.getGroup().getTwinning() == r1.getGroup().getTwinning()) count++;
		
		return count;
	}
	
	@Override
	public String toString(){
		return "Event " + name + " Organiz: " + organizer;
	}

	protected void updateWorkingDat(int day) {
		workingday = day;
	}
	
	@Override
	public int compareTo(Event arg0) {
		List<Rover> l1 = null;
		List<Rover> l2 = null;
		switch (workingday) {
		case 1:
			l1 = this.partecipant1;
			l2 = arg0.partecipant1;
			break;
		case 2:
			l1 = this.partecipant2;
			l2 = arg0.partecipant2;
			break;
		case 3:
			l1 = this.partecipant3;
			l2 = arg0.partecipant3;
			break;
		}
		
		if (this.isStillEmpty(workingday) && !arg0.isStillEmpty(workingday)) return -1;
		if (!this.isStillEmpty(workingday) && arg0.isStillEmpty(workingday)) return -1;
		if (l1.size() < l2.size()) return -1;
		else if (l1.size() > l2.size()) return 1;
		else return 0;
	}
	
	
}
