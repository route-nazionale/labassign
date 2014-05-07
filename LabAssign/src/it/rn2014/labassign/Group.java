package it.rn2014.labassign;

public class Group {

	private String name;
	private long code;
	private int subcamp;
	private int twinning;
	
	public Group(String name, long code, int subcamp, int twinning) {
		super();
		this.name = name;
		this.code = code;
		this.subcamp = subcamp;
		this.twinning = twinning;
	}
	
	public int getSubcamp(){ return subcamp; }
	public int getTwinning(){ return twinning; }
	
	@Override
	public String toString(){
		return "Group " + name + " Code: " + code;
	}
}
