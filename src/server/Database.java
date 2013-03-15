package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import data.*;

public class Database {
	
	private Map<Integer, Appointment> appointments;
	private Map<String, Group> groups;
	private ArrayList<Member> members;
	private ArrayList<Participant> participants;
	private Map<String, Room> rooms;
	private ArrayList<Subgroup> subgroups;
	private Map<String, User> users;
	
	Database() {
		appointments = new HashMap<Integer, Appointment>();
		groups = new HashMap<String, Group>();
		members = new ArrayList<Member>();
		participants = new ArrayList<Participant>();
		rooms = new HashMap<String, Room>();
		subgroups = new ArrayList<Subgroup>();
		users = new HashMap<String, User>();
		Connection con = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection("jdbc:sqlserver://localhost:1433","sa","CalFiftyPassword");
			if (con != null) {
				System.out.println("Connection established");
				con.createStatement().execute("USE CalFiftyDB");
				ResultSet rs = con.createStatement().executeQuery("SELECT * FROM Appointments");
				while (rs.next()) {
					appointments.put(rs.getInt(1), new Appointment(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getLong(4), rs.getLong(5), rooms.get(rs.getString(6)), users.get(rs.getString(7))));
				}
			} else {
				System.out.println("Connection failed");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	Map<Integer, Appointment> getAllAppointments() {
		return appointments;
	}

}
