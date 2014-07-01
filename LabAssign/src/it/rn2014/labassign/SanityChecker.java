package it.rn2014.labassign;

import java.sql.SQLException;
import java.sql.ResultSet;

/**
 * Classe che effettua il controllo sull'integrita' dei dati, al fine di evitare esecuzioni
 * anomale dovuti a dati malformati o non integri
 * 
 * @author Michele Carignani
 */
public class SanityChecker {

	/** Connettore al DB MySql */
	private MySqlConnector mc;

	/**
	 * Costruttore che inizializza l'oggetto e richiede una connessione con un
	 * DB MySQL gia' aperta
	 * 
	 * @param m Connettore al DB gia' aperto
	 */
	public SanityChecker(MySqlConnector m) {
		mc = m;
	}

	/**
	 * Metodo che esegue una query e controlla se il risultato della query e' uguale a zero o meno
	 * 
	 * @param query Query da eseguire
	 * @param desc Descrizione del controllo che si sta eseguendo (per fini di stampa)
	 * @return True se la query ha dato risultato 0, false altrimenti
	 */
	private boolean checkIsZero(String query, String desc) {
		
		ResultSet results = null;
		int qres = -1;
		
		System.out.print("Running check: " + desc + ".. ");
		
		try {
			
			results = mc.executeRaw(query);
			qres = Integer.parseInt(results.getString(0));
			
		} catch (SQLException e) { e.printStackTrace();}
		
		if (results != null && qres == 0) {
				System.out.println("\tOK");
				return true;
		} else {
				System.out.println("FAILED");
				return false;
		}
	}
	
	/**
	 * Metodo che esegue una query e controlla se il risultato della query e' maggiore di zero
	 * 
	 * @param query Query da eseguire
	 * @param desc Descrizione del controllo che si sta eseguendo (per fini di stampa)
	 * @return True se la query ha dato risultato maggiore di 0, false altrimenti
	 */
	private boolean checkMoreThanZero(String query, String desc) {
		
		ResultSet results = null;
		int qres = -1;
		
		System.out.print("Running check: " + desc + ".. ");
		
		try {
			
			results = mc.executeRaw(query);
			qres = Integer.parseInt(results.getString(0));
			
		} catch (SQLException e) { e.printStackTrace();}
		
		if (results != null && qres > 0) {
				System.out.println("\tOK");
				return true;
		} else {
				System.out.println("FAILED");
				return false;
		}
	}

	
	/** Query che controlla se i laboratori hanno i gruppi (codice,id) validi */
	private final String GroupValidForLab = "SELECT COUNT(*) FROM laboratori WHERE laboratori.organizzatore LIKE" +
			" '%RS%' AND  NOT EXISTS " +
			" (SELECT * FROM gruppi WHERE gruppi.idgruppo = laboratori.idgruppo AND" +
			" gruppi.idunita = laboratori.idunita)";

	/** Query che controlla se le tavole rotonde hanno i gruppi (codice,id) validi */
	private final String GroupValidForRoundTable = "SELECT COUNT(*) FROM tavolerotonde WHERE tavolerotonde.organizzatore LIKE" +
			" '%RS%' AND  NOT EXISTS " +
			" (SELECT * FROM gruppi WHERE gruppi.idgruppo = tavolerotonde.idgruppo AND" +
			" gruppi.idunita = tavolerotonde.idunita)";
	
	/** Query che controlla se le tavole rotonde hanno i gruppi (codice,id) validi (Secondo organizzatore) */
	private final String Group2ValidForRoundTable = "SELECT COUNT(*) FROM tavolerotonde WHERE tavolerotonde.organizzatore " +
			"LIKE '%RS%' AND idgruppo2 IS NOT NULL AND idunita2 IS NOT NULL AND NOT EXISTS" +
			" (SELECT * FROM gruppi WHERE gruppi.idgruppo = tavolerotonde.idgruppo2 AND" +
			" gruppi.idunita = tavolerotonde.idunita2)";
			
	/** Query che assicura che un ragazzo sia impostato su 3 strade di coraggio */
	private final String Non3StradeBoys = "SELECT COUNT( * )"
			+ "FROM  `ragazzi`"
			+ "WHERE stradadicoraggio1 + stradadicoraggio2 + "
			+ "stradadicoraggio3 + stradadicoraggio4 + stradadicoraggio5 != 3";

	/** Query che assicura che un lab abbia l'eta minima minore dell'eta massima */
	private final String MaxMinAge = "SELECT COUNT(id) FROM `laboratori` WHERE etamassima < etaminima;";

	/** Query che assicura che una tavola rotonda sia impostata sul una e solo una strada di coraggio */
	private final String StradeEvents = "SELECT COUNT( * )"
			+ "FROM  `tavolerotonde`"
			+ "WHERE stradacoraggio1 + stradacoraggio2 + stradacoraggio3 + stradacoraggio4 + stradacoraggio5 !=1";

	/** Query che assicura che un lab sia impostato sul una e solo una strada di coraggio */
	private final String StradeEvents2 = "SELECT COUNT( * ) "
			+ "FROM  `laboratori` "
			+ "WHERE stradadicoraggio1 + stradadicoraggio2 + "
			+ "stradadicoraggio3 + stradadicoraggio4 + stradadicoraggio5 !=1";

	/** Query che controlla il numero di posti disponibili per gli eventi nel turno 1*/
	private final String TotalPlaces1 = "SELECT SUM(total)-MAX(rag) AS avanzo, SUM(total) AS posti, MAX(rag) AS partecipanti " +
			"FROM (SELECT SUM(maxpartecipanti) AS total FROM `laboratori` UNION SELECT SUM(maxpartecipanti) AS total FROM `tavolerotonde` WHERE turno1 = 1) AS s," +
			"(SELECT COUNT(*) AS rag FROM `ragazzi`) AS r";
	/** Query che controlla il numero di posti disponibili per gli eventi nel turno 2*/
	private final String TotalPlaces2 = "SELECT SUM(total)-MAX(rag) AS avanzo, SUM(total) AS posti, MAX(rag) AS partecipanti " +
			"FROM (SELECT SUM(maxpartecipanti) AS total FROM `laboratori` UNION SELECT SUM(maxpartecipanti) AS total FROM `tavolerotonde` WHERE turno2 = 1) AS s," +
			"(SELECT COUNT(*) AS rag FROM `ragazzi`) AS r";
	/** Query che controlla il numero di posti disponibili per gli eventi nel turno 3*/
	private final String TotalPlaces3 = "SELECT SUM(total)-MAX(rag) AS avanzo, SUM(total) AS posti, MAX(rag) AS partecipanti " +
			"FROM (SELECT SUM(maxpartecipanti) AS total FROM `laboratori` UNION SELECT SUM(maxpartecipanti) AS total FROM `tavolerotonde` WHERE turno3 = 1) AS s," +
			"(SELECT COUNT(*) AS rag FROM `ragazzi`) AS r";

	/** Query che assicura che una tavola rotonda sia presente solamente per un turno */
	private final String NTurns = "SELECT COUNT(*) FROM `tavolerotonde` "
			+ "WHERE turno1 + turno2 + turno3 !=1";

	/** Query che controlla se un ragazzo con un vincolo impostato e' presente nella tabella ragazzi */
	private final String ConstraintsCensimento = "SELECT COUNT(*) FROM vincoli WHERE NOT EXISTS" +
			"(SELECT codicecensimento FROM ragazzi WHERE ragazzi.codicecensimento = vincoli.codicecensimento)";
	
	/** Query che controlla se un evento con un vincolo impostato e' presente nel DB */
	private final String ConstraintsEvento1 = "SELECT COUNT(*) FROM vincoli WHERE turn1 IS NOT NULL " +
			"AND turn1 NOT LIKE 'CODICI' AND NOT EXISTS" +
			"(SELECT codice FROM `tavolerotonde` WHERE tavolerotonde.codice = vincoli.turn1) AND NOT EXISTS" +
			"(SELECT codice FROM `laboratori` WHERE laboratori.codice = vincoli.turn1)";

	/** Query che controlla se un evento con un vincolo impostato e' presente nel DB */
	private final String ConstraintsEvento2 = "SELECT COUNT(*) FROM vincoli WHERE turn2 IS NOT NULL " +
			"AND turn2 NOT LIKE 'CODICI' AND NOT EXISTS" +
			"(SELECT codice FROM `tavolerotonde` WHERE tavolerotonde.codice = vincoli.turn2) AND NOT EXISTS" +
			"(SELECT codice FROM `laboratori` WHERE laboratori.codice = vincoli.turn2)";
	
	/** Query che controlla se un evento con un vincolo impostato e' presente nel DB */
	private final String ConstraintsEvento3 = "SELECT COUNT(*) FROM vincoli WHERE turn3 IS NOT NULL " +
			"AND turn3 NOT LIKE 'CODICI' AND NOT EXISTS" +
			"(SELECT codice FROM `tavolerotonde` WHERE tavolerotonde.codice = vincoli.turn3) AND NOT EXISTS" +
			"(SELECT codice FROM `laboratori` WHERE laboratori.codice = vincoli.turn3)";
	

	/** Query che controlla se tutti i ragazzi hanno un gruppo presente su DB */
	private final String ValidGroup = "SELECT COUNT(*) FROM ragazzi WHERE NOT EXISTS " +
			"(SELECT * FROM gruppi WHERE gruppi.idgruppo = ragazzi.idgruppo AND" +
			"gruppi.idunita = ragazzi.idunita)";

	
	/**
	 * Metodo che esegue in cascata tutti i controlli presenti nella classe 
	 */
	public void runAllChecks() {
		checkIsZero(NTurns, "Corretto numero di turni");
		checkIsZero(StradeEvents, "Unica strada per laboratori");
		checkIsZero(StradeEvents2, "Unica strada per tavole rotonde");
		checkIsZero(MaxMinAge, "Età massima maggiore della minima");
		checkIsZero(Non3StradeBoys, "3 strade per ogni ragazzo");
		checkMoreThanZero(TotalPlaces1, "Numero di posti disponibili - Turno 1");
		checkMoreThanZero(TotalPlaces2, "Numero di posti disponibili - Turno 2");
		checkMoreThanZero(TotalPlaces3, "Numero di posti disponibili - Turno 3");
		checkIsZero(ConstraintsCensimento, "Controllo codice censimento su Vincoli");
		checkIsZero(ConstraintsEvento1, "Controllo codici evento - Turno 1");
		checkIsZero(ConstraintsEvento2, "Controllo codici evento - Turno 2");
		checkIsZero(ConstraintsEvento3, "Controllo codici evento - Turno 3");
		checkIsZero(ValidGroup, "Controllo codici gruppo ragazzi");
		checkIsZero(GroupValidForLab, "Controllo codici gruppo laboratori");
		checkIsZero(GroupValidForRoundTable, "Controllo codici gruppo tavole rotonde - Organizzatore 1");
		checkIsZero(Group2ValidForRoundTable, "Controllo codici gruppo tavole rotonde - Organizzatore 2");
	}
}