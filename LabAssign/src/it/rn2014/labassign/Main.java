package it.rn2014.labassign;

public class Main {

	public static void main(String[] args) {

		RoverList rl = new RoverList(null);
		EventList el = new EventList(null);
		
		beginningAssignment(rl, el);
		
		double itercount = 0;
		double globalsat = rl.totalSatisfaction()/rl.totalMaxSatisfaction();
		while (itercount < Parameters.MAX_ITERATIONS && globalsat < Parameters.SATISFATION_THRESHOLD){
			
			/* Si deve scrivere la procedura che:
			 * - Pesca dalla lista di priorita' il ragazzo meno soddisfatto
			 * - Va a capire quale e' il laboratorio che gli crea problemi
			 * - Vede se puo' rimuovere l'associazione per il ragazzo dal laboratorio
			 * 		- Se si, prende il primo laboratorio per quella strada di coraggio e prova ad assegnarglielo. 
			 * 		- Oppure prende il ragazzo piu' soddisfatto e prova a scambiare un laboratorio con lui.
			 */
			
			itercount++;
			globalsat = rl.totalSatisfaction()/rl.totalMaxSatisfaction();
		}
		
		System.exit(0);
	}

	private static void beginningAssignment(RoverList rl, EventList el) {
		for (int workingday = 1; workingday <= 3; workingday++){
			for (Rover r : rl){
				Event e = el.getEventByRoad(r.getRandomRoad());
				while (!r.isSuitable(e, workingday)){
					e = el.getEventByRoad(r.getRandomRoad());
				}
				r.assignToEvent(workingday, e);
				el.updateEvent(e);
			}
		}
	}

}
