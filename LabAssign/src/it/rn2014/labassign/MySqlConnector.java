/*   This file is part of LabAssign
 *
 *   LabAssign is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   LabAssign is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with LabAssign.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.rn2014.labassign;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe che si occupa di gestire la comunicazione con il Database MySQL e
 * che permette di raccogliere i dati e di popolare le strutture necessarie al calcolo.
 * 
 * I dati di accesso al DB vengono impostati nel file `labassign.conf`
 * 
 * @author Nicola Corti
 */
public class MySqlConnector {

	/** Stringa della classe del driver */
	private String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	/** Stringa del URL del database */
	private String DB_URL = null;

	/** Connesione al DB */
	private Connection conn;
	/** Statement contenente le query */
	private Statement stat;
	
	/**
	 * Costruttore che inizializza i parametri di accesso al db
	 */
	public MySqlConnector(){
		DB_URL = "jdbc:mysql://"+Parameters.DB_HOST+"/"+Parameters.DB_NAME;
		conn = null;
		stat = null;
	}
	
	/**
	 * Metodo che stabilisce la connessione con il DB.
	 * E' necessario invocare questa funzione prima di effettuare qualsiasi altro metodo
	 * della classe MySqlConnector
	 */
	public void connect(){
		try {
			Class.forName(JDBC_DRIVER);
		    conn = DriverManager.getConnection(DB_URL,Parameters.DB_USER,Parameters.DB_PASS);
		    
		} catch (ClassNotFoundException e) { e.printStackTrace();
		} catch (SQLException e) { e.printStackTrace(); }
	}
		
	/**
	 * Raccoglie i gruppi dal database e li restituisce in una lista
	 * 
	 * @return Una lista di Group (vuota se non ci sono gruppi).
	 */
	public List<Group> getGroups() {
		
		List<Group> list = new ArrayList<>();
		
		try {
			stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("SELECT * FROM gruppi");
			
			Group g;
			while(rs.next()){
				
				String name = rs.getString("nome");
				int sottocampo = rs.getInt("sottocampo");
				String idgruppo = rs.getString("idgruppo");
				String idunita = rs.getString("idunita");
				int gemellaggio = rs.getInt("gemellaggio");
				
				g = new Group(name, idgruppo, idunita, sottocampo, gemellaggio);
				list.add(g);
			}
		} catch (SQLException e) { e.printStackTrace(); }
		return list;
	}

	public ResultSet executeRaw(String query) {
		ResultSet rs = null;
		try{
			stat = conn.createStatement();
			rs = stat.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	/**
	 * Raccoglie i rover dal database e li inserisce in una RoverList
	 * Inoltre effettua un controllo se sono presenti gruppi non presenti nel modello
	 * 
	 * @param gl Una lista di gruppi relativi ai ragazzi
	 * @return Una lista di Rover
	 */
	public RoverList getRovers(List<Group> gl){
		RoverList list = new RoverList();
		
		try {
			stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("SELECT * FROM ragazzi ORDER BY RAND()");
			
			while(rs.next()){
				
				String name = rs.getString("nome");
				String surname = rs.getString("cognome");
				int code = rs.getInt("codicecensimento");
				int age = rs.getInt("eta");
				
				String idgroup = rs.getString("idgruppo");
				String idunity = rs.getString("idunita");
				
				Group group = null;
				for (Group g: gl){
					if (g.sameGroup(idgroup, idunity)){
						group = g;
						break;
					}
				}
				if (group == null) System.err.println("\nGRUPPO ASSENTE NEL DB! ID: " + idgroup + " UNITA " + idunity);
				
				// Vincolo su novizi disabilitato
				//boolean novice = rs.getBoolean("novizio");
				boolean handicap = rs.getBoolean("handicap");
				
				Rover r = new Rover(name, surname, code, age, handicap, false, group);
				
				boolean road1 = rs.getBoolean("stradadicoraggio1");
				boolean road2 = rs.getBoolean("stradadicoraggio2");
				boolean road3 = rs.getBoolean("stradadicoraggio3");
				boolean road4 = rs.getBoolean("stradadicoraggio4");
				boolean road5 = rs.getBoolean("stradadicoraggio5");
				
				r.setRoadsPreference(road1, road2, road3, road4, road5);
				list.addRover(r);
			}
			
		} catch (SQLException e) { e.printStackTrace(); }
		return list;
	}
	

	/**
	 * Raccoglie le tavole rotonde dal database e li aggiunge ad una EventList gia' esistente
	 * Controlla inoltre se sono presenti eventi organizzati da gruppi assenti
	 * 
	 * @param gl Lista di gruppi
	 * @param el Lista di eventi
	 */
	public void getRoundTable(List<Group> gl, EventList el){
		
		try {
			stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("SELECT * FROM tavolerotonde");
			while(rs.next()){

				String name = rs.getString("titolo");
				String code = rs.getString("codice");
				
				String organizer = rs.getString("organizzatore");
				Group group = null;
				
				// Vado a prendere il gruppo
				if(organizer.contains("RS")){
					String idgruppo = rs.getString("idgruppo");
					String idunita = rs.getString("idunita");
					for (Group g: gl){
						if (g.sameGroup(idgruppo, idunita)){
							group = g;
							break;
						}
					}
					if (group == null) System.err.println("\nGRUPPO ASSENTE NEL DB! ID: " + idgruppo + " UNITA " + idunita);
				}	
				
				// Controllo la presenza del secondo gruppo
				
				Group group2 = null;
				String idgruppo2 = rs.getString("idgruppo2");
				String idunita2 = rs.getString("idunita2");
				if (idgruppo2 != null){
					for (Group g: gl){
						if (g.sameGroup(idgruppo2, idunita2)){
							group2 = g;
							break;
						}
					}
					if (group2 == null) System.err.println("\nGRUPPO (2) ASSENTE NEL DB! ID: " + idgruppo2 + " UNITA " + idunita2);
				}	
				
			
				int maxpartecipant = rs.getInt("maxpartecipanti");
				int minpartecipant = rs.getInt("minpartecipanti");
				
				boolean road1 = rs.getBoolean("stradacoraggio1");
				boolean road2 = rs.getBoolean("stradacoraggio2");
				boolean road3 = rs.getBoolean("stradacoraggio3");
				boolean road4 = rs.getBoolean("stradacoraggio4");
				boolean road5 = rs.getBoolean("stradacoraggio5");
				
				int overmax = 0, max = 0;
				switch (maxpartecipant) {
					case 500: max = 440; overmax = 450; break;
					case 450: max = 440; overmax = 450; break;
					case 0: max = 0; overmax = 0; break;
					default: max = 0; overmax = 0; break;
				}
				
				RoundTable roundTable = new RoundTable(code, name, max, overmax, minpartecipant, group, group2);
				roundTable.setRoadsPreference(road1, road2, road3, road4, road5);
				
				int day = 1;
				boolean turno1 = rs.getBoolean("turno1");
				if(turno1) day = 1;
				boolean turno2 = rs.getBoolean("turno2");
				if(turno2) day = 2;
				boolean turno3 = rs.getBoolean("turno3");
				if(turno3) day = 3;
				roundTable.setTurn(day);
				
				el.addEvent(roundTable);
			}
		} catch (SQLException e) { e.printStackTrace(); }
	}


	/**
	 * Raccoglie i laboratori dal database e ritorna una EventList che li contiene
	 * Controlla inoltre se sono presenti eventi organizzati da gruppi assenti
	 * 
	 * @param gl Lista di gruppi
	 * @return Una Lista di eventi contenente solamente Laboratori
	 */
	public EventList getLabs(List<Group> gl){
		EventList list = new EventList();
				
		try {
			stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("SELECT * FROM laboratori");
			
			Lab l;
			while(rs.next()){
				
				String name = rs.getString("nome");
				String code = rs.getString("codice");

				int subcamp = rs.getInt("sottocampo");
				String organizer = rs.getString("organizzatore");
				Group group = null;
				
				// Vado a prendere il gruppo se e' un evento RS
				if(organizer.contains("RS")){
					String idgruppo = rs.getString("idgruppo");
					String idunita = rs.getString("idunita");
					if (idgruppo != null && idunita != null){
					for (Group g: gl){
						if (g.sameGroup(idgruppo, idunita)){
							group = g;
							break;
						}
					}
					if (group == null) System.err.println("\nGRUPPO ASSENTE NEL DB! ID: " + idgruppo + " UNITA " + idunita);
					}
				}			
				
				int maxpartecipant = rs.getInt("maxpartecipanti");
				int minpartecipant = rs.getInt("minpartecipanti");
				
				int maxage = rs.getInt("etamassima");
				int minage = rs.getInt("etaminima");
				
				boolean road1 = rs.getBoolean("stradadicoraggio1");
				boolean road2 = rs.getBoolean("stradadicoraggio2");
				boolean road3 = rs.getBoolean("stradadicoraggio3");
				boolean road4 = rs.getBoolean("stradadicoraggio4");
				boolean road5 = rs.getBoolean("stradadicoraggio5");
				
				boolean handicap = !rs.getBoolean("handicap");
				
				// Non disponiamo dell'informazione se un laboratorio e' adatto o meno
				// ai novizi per cui e' stato disabilitato
				//boolean novice = rs.getBoolean("novizio");
				boolean novice = false;
				
				// Impostazione max e overmax partecipants
				int overmax = 0, max = 0;
				switch (maxpartecipant) {
					case 35: max = 28; overmax = 32; break;
					case 32: max = 28; overmax = 32; break;
					case 25: max = 23; overmax = 25; break;
					case 20: max = 18; overmax = 20; break;
					case 0: max = 0; overmax = 0; break;
				}
				
				l = new Lab(code, name, minpartecipant, max, overmax, group, novice, handicap, maxage, minage, subcamp);
				l.setRoadsPreference(road1,road2, road3, road4, road5);
				
				list.addEvent(l);
			}
			
		} catch (SQLException e) { e.printStackTrace(); }
		return list;
	}
	

	/**
	 * Raccoglie gli assegnamenti preesistenti dal database e li aggiunge ai ragazzi.
	 * 
	 * @param rl Lista di rover
	 * @param el Lista di eventi
	 */
	public void getCostraints(RoverList rl, EventList el){
		try {
			stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("SELECT * FROM vincoli");
			
			while(rs.next()){
				int code = rs.getInt("codicecensimento");
				String turn1 = rs.getString("turn1");
				String turn2 = rs.getString("turn2");
				String turn3 = rs.getString("turn3");
				
				Rover r = rl.getRover(code);
				if (r == null) {
					System.err.println("\nVINCOLO SU RAGAZZO ASSENTE: " + code);
					continue;
				}
				Event e = null;
				if (turn1 != null){
					if ((e = el.getEvent(turn1)) == null) System.err.println("EVENTO CON VINCOLO ASSENTE: " + turn1);
					else r.setConstraint(e, 1);
				}
				if (turn2 != null){
					if ((e = el.getEvent(turn2)) == null) System.err.println("EVENTO CON VINCOLO ASSENTE: " + turn2);
					else r.setConstraint(e, 2);
				}
				if (turn3 != null){
					if ((e = el.getEvent(turn3)) == null) System.err.println("EVENTO CON VINCOLO ASSENTE: " + turn3);
					else r.setConstraint(e, 3);
				}
			}
		} catch (SQLException e) { e.printStackTrace(); }
	}
	
	/**
	 * Assegna i laboratori ai sottocampi in modo che siano bilanciati e che le strade di coraggio
	 * siano ripartite equamente
	 * 
	 * @param el Lista di eventi
	 */
	public void setSubcamps(EventList el){
		
		// Associazione degli eventi ai sottocampi
		for (int road = 1; road <= 5; road++){
			
			int count = 0;
			int count_subcamp1 = 0;
			int count_subcamp2 = 0;
			int count_subcamp3 = 0;
			int count_subcamp4 = 0;
			int count_subcamp5 = 0;
			Lab l;
			
			for (Event e: el){
				
				if(e instanceof Lab) { l = (Lab) e;	} else continue;
				
				if (l.getRoad() == road) count++;
				if (l.getRoad() == road && l.getSubcamp() != 0) {
					if (l.getSubcamp() == 1) count_subcamp1++;
					if (l.getSubcamp() == 2) count_subcamp2++;
					if (l.getSubcamp() == 3) count_subcamp3++;
					if (l.getSubcamp() == 4) count_subcamp4++;
					if (l.getSubcamp() == 5) count_subcamp5++;
				}
			}
			
			int limit = (count / 5) + 1;
			
			int current_camp = 1;
			
			for (Event e: el){
				if(e instanceof Lab) { l = (Lab) e;	} else continue;
				
				if (l.getRoad() == road && l.getSubcamp() == 0) {
					if(current_camp == 1 && count_subcamp1 < limit) {l.setSubcamp(current_camp); count_subcamp1++;}
					if(current_camp == 2 && count_subcamp2 < limit) {l.setSubcamp(current_camp); count_subcamp2++;}
					if(current_camp == 3 && count_subcamp3 < limit) {l.setSubcamp(current_camp); count_subcamp3++;}
					if(current_camp == 4 && count_subcamp4 < limit) {l.setSubcamp(current_camp); count_subcamp4++;}
					if(current_camp == 5 && count_subcamp5 < limit) {l.setSubcamp(current_camp); count_subcamp5++;}
				}
				
				int min = count;
				if (count_subcamp1 < min) {min = count_subcamp1; current_camp = 1;}
				if (count_subcamp2 < min) {min = count_subcamp2; current_camp = 2;}
				if (count_subcamp3 < min) {min = count_subcamp3; current_camp = 3;}
				if (count_subcamp4 < min) {min = count_subcamp4; current_camp = 4;}
				if (count_subcamp5 < min) {min = count_subcamp5; current_camp = 5;}
			}
		}
	}
	
	
	/**
	 * Metodo che scrive sul Database, nelle tabelle dei risultati gli assegnamenti dei ragazzi, e gli
	 * assegnamenti delle tavole rotonde.
	 * 
	 * @param rl Elenco di Rover con assegnamenti effettuati
	 * @param el Elenco di Eventi gia' assegnati
	 */
	public void sendResult(RoverList rl, EventList el){
		
		this.update("DELETE FROM risultati_laboratori");
		for (Event e: el){
			if (e instanceof Lab){
				Lab l = (Lab) e;
				String query = "INSERT INTO risultati_laboratori (codice, sottocampo) VALUES (?," + l.getSubcamp() + ")";
				java.sql.PreparedStatement p = this.getPrepared(query);
				try {
					p.setString(1, l.getCode());
				} catch (SQLException e1) {	e1.printStackTrace(); }
				this.updatePrepared(p);
			}
		}
		
		this.update("DELETE FROM risultati_ragazzi");
		for (Rover r: rl){
			String query = "INSERT INTO risultati_ragazzi (codicecensimento, turno1, priorita1, turno2, priorita2, turno3, priorita3, soddisfacimento) VALUES (";
			query += r.getCode() + ",?," + r.getPriority(1)+ ",?," + r.getPriority(2) + ",?," + r.getPriority(3) + "," + r.getSatisfaction() + ")";
			java.sql.PreparedStatement p = this.getPrepared(query);
			try{
				p.setString(1, r.getEventCode(1));
				p.setString(2, r.getEventCode(2));
				p.setString(3, r.getEventCode(3));
				
				this.updatePrepared(p);
			} catch ( SQLException e){ e.printStackTrace(); }
		}
	}
	
	
	/**
	 * Chiude la connessione con il database
	 */
	public void close(){
		try {
			stat.close();
			conn.close();
		} catch (SQLException e) { e.printStackTrace(); }
	}
	
	
	////////////////////////////////////////
	//////////// Metodi Protetti ///////////
	////////////////////////////////////////
	
	/**
	 * DEBUG: Effettua una query sul database e stampa i risultati su
	 * Standard Output
	 * 
	 * @param p La query da eseguire
	 */
	protected void execute(String p){
		try {
			stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(p);
			
			int column = rs.getMetaData().getColumnCount();
			for(int i = 1; i <= column; i++){
				System.out.print(rs.getMetaData().getColumnName(i) + ", ");
			}
			System.out.println();
			
			while(rs.next()){
				for(int i = 1; i <= column; i++){
					System.out.print(rs.getString(i) + ", ");
				}
				System.out.print("\n");
			}
		} catch (SQLException e) { e.printStackTrace(); }
	}
	
	/**
	 * Esegue una query di update/insert/delete sul database
	 * 
	 * @param string Query da eseguire
	 */
	protected void update(String string) {
		try {
			stat = conn.createStatement();
			stat.executeUpdate(string);
			
		} catch (SQLException e) { e.printStackTrace(); }
	}
	
	/**
	 * Genera un prepared Statement per effettuare un operazione su DB con stringhe
	 * che siano correttamente `escaped`
	 * 
	 * @param s Query che contiene marcatori che devono essere riempiti
	 * @return Un prepared statement, che deve essere completato e poi eseguito
	 */
	protected java.sql.PreparedStatement getPrepared(String s){
		try {
			return conn.prepareStatement(s);
		} catch (SQLException e) { e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Esegue un PreparedStatement sul database.
	 * Nota che il PreparedStatement deve essere correttamente completato per poter
	 * essere eseguito
	 * 
	 * @param s Un prepared statement
	 */
	protected void updatePrepared(java.sql.PreparedStatement s) {
		try {
			s.executeUpdate();
		} catch (SQLException e) { e.printStackTrace(); }
	}
	
	/**
	 * Esegue una query e calcola la dimensione del recordset che viene ritornato come risultato
	 * 
	 * @param string Query da eseguire
	 * @return La dimensione in righe del risultato
	 */
	protected int executeCount(String string) {
		int size = 0;
		try {
			stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(string);
			while (rs.next()) size++;			
		} catch (SQLException e) { e.printStackTrace(); }
		return size;
		
	}
	
	/**
	 * Esegue una query che ritorna una sola riga ed una sola colonna, e ritorna quel valore
	 * nel formato Stringa
	 * 
	 * @param string Query da eseguire
	 * @return Stringa relativa al risultato
	 */
	protected String executeString(String string) {
		try {
			stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(string);
			rs.next();
			return rs.getString(1);		
		} catch (SQLException e) { e.printStackTrace(); }
		return null;
	}
}
