package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

import data.*;

public class Database {
	
	private ArrayList<Appointment> appointments;
	private ArrayList<Group> groups;
	private ArrayList<Member> members;
	private ArrayList<Participant> participants;
	private ArrayList<Room> rooms;
	private ArrayList<Subgroup> subgroups;
	private ArrayList<User> users;
	
	Database() {
		
		Connection con = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection("jdbc:sqlserver://localhost:1433","sa","CalFiftyPassword");
			if (con != null) {
				System.out.println("Connection established");
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

}
