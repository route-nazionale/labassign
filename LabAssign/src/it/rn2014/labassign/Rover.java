/**
 * 
 */
package it.rn2014.labassign;

public class Rover implements Comparable<Rover> {

	private String name;
	private String surname;
	private double code;
	private int age;
	private boolean handicap;
	private boolean novice;
	private Group group;
	
	private boolean road1;
	private boolean road2;
	private boolean road3;
	private boolean road4;
	private boolean road5;
	
	private Event assign1;
	private Event assign2;
	private Event assign3;
	
	
	public Rover(String name, String surname, double code, int age,
			boolean handicap, boolean novice, Group group) {
		this.name = name;
		this.surname = surname;
		this.code = code;
		this.age = age;
		this.handicap = handicap;
		this.novice = novice;
		this.group = group;
	}
	
	public Group getGroup(){ return group; }
	
	public boolean getRoad(int road){
		switch (road) {
		case 1: return road1;
		case 2: return road2;
		case 3: return road3;
		case 4: return road4;
		case 5: return road5;
		}
		return false;
	}
	
	public void setRoadsPreference(boolean r1, boolean r2, boolean r3, boolean r4, boolean r5){
		this.road1 = r1;
		this.road2 = r2;
		this.road3 = r3;
		this.road4 = r4;
		this.road5 = r5;
	}
	
	////////////////////////////////////
	//
	//
	public double getSatisfaction(){
		double sat = 0;
		if (assign1 != null && isSatisfiedRoad(assign1.getRoad())) sat += 5;
		if (assign2 != null && isSatisfiedRoad(assign2.getRoad())) sat += 5;
		if (assign3 != null && isSatisfiedRoad(assign3.getRoad())) sat += 5;
		if (assign1 != null && assign2 != null && assign1.getRoad() != assign2.getRoad()) sat += 2;
		if (assign1 != null && assign3 != null && assign1.getRoad() != assign3.getRoad()) sat += 2;
		if (assign2 != null && assign3 != null && assign2.getRoad() != assign3.getRoad()) sat += 2;
		return sat;
	}
	//
	public double getMaxSatisfaction(){
		return 21;
	}
	//
	//
	////////////////////////////////////
	
	
	public boolean isSatisfiedRoad(int road){
		switch (road) {
		case 1: return road1;
		case 2: return road2;
		case 3: return road3;
		case 4: return road4;
		case 5: return road5;
		default: return false;
		}
	}
	
	public boolean isSuitable(Event e, int day){
		
		// Caso Laboratorio
		if (e instanceof Lab){
			
			Lab l = (Lab) e;
			if (l.isFull(day)) return false;
			if (l.getMaxAge() < this.age) return false;
			if (l.getMinAge() > this.age) return false;
			if (l.getSuitableHandicap() == false && this.handicap == true) return false;
			if (l.getSuitableNovice() == false && this.novice == true) return false;
			if (l.getSubcamp() != this.group.getSubcamp()) return false;
			if (l.getPartecipantsTwinnings(day, this) >= Parameters.LABORATORY_MAX_TWINNING_USER) return false;
			
		} else if (e instanceof RoundTable) {
			RoundTable r = (RoundTable) e;
			if (r.isFull(day)) return false;
			if (r.getPartecipantsTwinnings(day, this) >= Parameters.ROUNDTABLE_MAX_USER) return false;
		}
		
		return true;
	}
	
	public void assignToEvent(int day, Event evt){
		switch (day) {
		case 1:
			assign1 = evt;
			break;
		case 2:
			assign2 = evt;
			break;
		case 3:
			assign3 = evt;
			break;
		default:
			break;
		}
		evt.assign(day, this);
	}
	
	public boolean isAssigned(int day){
		switch (day) {
		case 1: return assign1 != null;
		case 2: return assign2 != null;
		case 3: return assign3 != null;
		default: return false;
		}
	}
	
	public boolean isFullyAssigned(){
		return (assign1 != null && assign2 != null && assign3 != null);
	}

	@Override
	public String toString() {
		String result = name + " - " + surname + " - " + code + " - TOCOMPLETE ";
		return result;
	}

	@Override
	public int compareTo(Rover arg0) {
		double sat1 = this.getSatisfaction();
		double sat2 = arg0.getSatisfaction();
		if (sat1 < sat2 ) return -1;
		if (sat1 > sat2 ) return 1;
		return 0;
	}
	
	public int getRandomRoad(){
		int rand = (int) (Math.random()*100);
		while(true){
			if (rand < 20 && road1) return 1;
			if (rand >= 20 && rand < 40 && road2) return 2;
			if (rand >= 40 && rand < 60 && road3) return 3;
			if (rand >= 60 && rand < 80 && road3) return 4;
			if (rand >= 80 && rand <= 100 && road3) return 5;
		}
	}
	
	
}