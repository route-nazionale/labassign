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
import java.util.Locale;

public class MySqlConnector {

	private String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	private String DB_URL = null;

	Connection conn;
	Statement stat;
	
	public MySqlConnector(){
		DB_URL = "jdbc:mysql://"+Parameters.DB_HOST+"/"+Parameters.DB_NAME;
		conn = null;
		stat = null;
	}
	
	public void connect(){
		try {
			Class.forName(JDBC_DRIVER);
			
		    conn = DriverManager.getConnection(DB_URL,Parameters.DB_USER,Parameters.DB_PASS);
		    
		} catch (ClassNotFoundException e) { e.printStackTrace();
		} catch (SQLException e) { e.printStackTrace(); }
	}
	
	public void execute(String p){
		
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
	
	public RoverList getRovers(List<Group> gl){
		RoverList list = new RoverList();
		
		
		try {
			stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("SELECT * FROM ragazzi");
			
			
			
			Rover r;
			while(rs.next()){
				
				String name = rs.getString("nome");
				String surname = rs.getString("cognome");
				double code = rs.getDouble("codicecensimento");
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
				if (group == null) {
					
					System.err.println("\nGRUPPO ASSENTE NEL DB! ID: " + idgroup + " UNITA " + idunity);
					//throw new RuntimeException("GRUPPO NON PRESENTE NEL DB");
				}
				
				boolean novice = rs.getBoolean("novizio");
				boolean handicap = rs.getBoolean("handicap");
				
				r = new Rover(name, surname, code, age, handicap, novice, group);
				
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
	
	public EventList getLabs(){
		EventList list = new EventList();
				
		try {
			stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("SELECT * FROM laboratori");
			
			Lab l;
			while(rs.next()){
				
				//int id = rs.getInt("id");
				String name = rs.getString("nome");
				String code = rs.getString("codice");
				//String organizer = rs.getString("organizzatore");
				int subcamp = rs.getInt("sottocampo");
				
				int maxpartecipant = rs.getInt("maxpartecipanti");
				int minpartecipant = rs.getInt("minpartecipanti");
				
				int maxage = rs.getInt("etamassima");
				int minage = rs.getInt("etaminima");
				
				boolean road1 = rs.getBoolean("stradadicoraggio1");
				boolean road2 = rs.getBoolean("stradadicoraggio2");
				boolean road3 = rs.getBoolean("stradadicoraggio3");
				boolean road4 = rs.getBoolean("stradadicoraggio4");
				boolean road5 = rs.getBoolean("stradadicoraggio5");
				
				boolean handicap = rs.getBoolean("handicap");
				boolean novice = rs.getBoolean("novizio");
				
				l = new Lab(code, name, minpartecipant, maxpartecipant, null, novice, handicap, maxage, minage, subcamp);
				l.setRoadsPreference(road1,road2, road3, road4, road5);
				
				list.addEvent(l);
			}
			
		} catch (SQLException e) { e.printStackTrace(); }
		return list;
	}
	
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
			
			int limit = (count / 5)+1;
			
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
	
	
	public void close(){
		try {
			stat.close();
			conn.close();
		} catch (SQLException e) { e.printStackTrace(); }
	}

	public List<Group> getGroups() {
		
		List<Group> list = new ArrayList<>();
		
		try {
			stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("SELECT * FROM gruppi");
			
			Group g;
			while(rs.next()){
				
				int id = rs.getInt("id");
				String name = rs.getString("nome");
				int sottocampo = rs.getInt("sottocampo");
				String idgruppo = rs.getString("idgruppo");
				String idunita = rs.getString("idunita");
				int gemellaggio = rs.getInt("gemellaggio");
				
				g = new Group(id, name, idgruppo, idunita, sottocampo, gemellaggio);
				list.add(g);
			}
			
		} catch (SQLException e) { e.printStackTrace(); }
		return list;
	}

	public void update(String string) {
		try {
			stat = conn.createStatement();
			stat.executeUpdate(string);
			
		} catch (SQLException e) { e.printStackTrace(); }
	}
	
	public java.sql.PreparedStatement getPrepared(String s){
		try {
			return conn.prepareStatement(s);
		} catch (SQLException e) { e.printStackTrace();
			return null;
		}
	}
	
	public void updatePrepared(java.sql.PreparedStatement s) {
		try {
			s.executeUpdate();
		} catch (SQLException e) { e.printStackTrace(); }
	}
	
	public int executeCount(String string) {
		int size = 0;
		try {
			stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(string);
			while (rs.next()) size++;			
		} catch (SQLException e) { e.printStackTrace(); }
		return size;
		
	}
	
	public String executeString(String string) {
		try {
			stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(string);
			rs.next();
			System.out.println("RETURNING " + rs.getString(1));
			return rs.getString(1);		
		} catch (SQLException e) { e.printStackTrace(); }
		return null;
	}
}
