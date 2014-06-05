package it.rn2014.labassign;

public class Lab extends Event {
	
	private boolean suitablenovice;
	private boolean suitablehandicap;
	private int maxage = 200;
	private int minage = 0;
	private int subcamp;
	
	public Lab(String code, String name, int road, int minpartecipant, int maxpartecipant, 
			Group organizer, boolean novice, boolean handicap, int maxage, int minage, int subcamp) {
		super(code, name, road, maxpartecipant, minpartecipant, organizer);
		
		this.suitablenovice = novice;
		this.suitablehandicap = handicap;
		
		this.maxage = maxage;
		this.minage = minage;
		this.subcamp = subcamp;
	}
	
	public boolean getSuitableNovice(){ return suitablenovice; }
	public boolean getSuitableHandicap(){ return suitablehandicap; }
	public int getMaxAge(){ return maxage; }
	public int getMinAge(){ return minage; }
	public int getSubcamp(){ return subcamp; }

	
}
