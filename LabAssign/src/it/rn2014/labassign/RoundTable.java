package it.rn2014.labassign;

public class RoundTable extends Event {

	public RoundTable(String name, int road,
			int minpartecipant, Group organizer) {
		super(name, road, Parameters.ROUNDTABLE_MAX_USER, minpartecipant, organizer);
	}

}
