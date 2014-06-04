package it.rn2014.labassign;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
			System.out.println("Connecting to database...");
		    conn = DriverManager.getConnection(DB_URL,Parameters.DB_USER,Parameters.DB_PASS);
		    System.out.println("Connection OK");

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void execute(String p){
		
		try {
			stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(p);
			
			int column = rs.getMetaData().getColumnCount();
			for(int i = 1; i <= column; i++){
				//System.out.print(rs.getMetaData().getColumnName(i) + ", ");
			}
			//System.out.println();
			
			while(rs.next()){
				for(int i = 1; i <= column; i++){
					System.out.print(rs.getString(i) + ", ");
				}
				System.out.print("\n");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public RoverList getRovers(List<Group> gl){
		RoverList list = new RoverList();
		List<String> ls = new ArrayList<>();
		
		try {
			stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("SELECT * FROM ragazzi");
			
			
			
			Rover r;
			while(rs.next()){
				
				int id = rs.getInt("id");
				String name = rs.getString("nome");
				String surname = rs.getString("cognome");
				double code = rs.getDouble("codicecensimento");
				int age = rs.getInt("eta");
				
				String idgroup = rs.getString("idgruppo");
				Group group = null;
				for (Group g: gl){
					if (g.getCode().contentEquals(idgroup)){
						group = g;
						break;
					}
				}
				if (group == null) {
					
					System.err.println("GRUPPO " + idgroup);
					ls.add(idgroup);
					//throw new RuntimeException("GRUPPO NON PRESENTE NEL DB");
				}
				
				boolean novice = rs.getBoolean("novizio");
				boolean handicap = rs.getBoolean("handicap");
				
				r = new Rover(id, name, surname, code, age, handicap, novice, group);
				
				boolean road1 = rs.getBoolean("stradadicoraggio1");
				boolean road2 = rs.getBoolean("stradadicoraggio2");
				boolean road3 = rs.getBoolean("stradadicoraggio3");
				boolean road4 = rs.getBoolean("stradadicoraggio4");
				boolean road5 = rs.getBoolean("stradadicoraggio5");
				
				r.setRoadsPreference(road1, road2, road3, road4, road5);
				
				list.addRover(r);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	
	public void close(){
		try {
			stat.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
				//String idunita = rs.getString("idunita");
				int gemellaggio = rs.getInt("gemellaggio");
				
				g = new Group(id, name, idgruppo, sottocampo, gemellaggio);
				list.add(g);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public void update(String string) {
		try {
			stat = conn.createStatement();
			stat.executeUpdate(string);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}
	
	public int executeCount(String string) {
		int size = 0;
		try {
			stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(string);
			while (rs.next()) size++;			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return size;
		
	}
	
	public String executeString(String string) {
		try {
			stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(string);
			rs.next();
			System.out.println("RETURNING " + rs.getString(1));
			return rs.getString(1);		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
