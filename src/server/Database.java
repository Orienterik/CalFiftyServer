package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import data.Appointment;
import data.Group;
import data.Member;
import data.Participant;
import data.Room;
import data.Subgroup;
import data.User;

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
				ResultSet rs = con.createStatement().executeQuery("SELECT * FROM Rooms");
				while (rs.next()) {
					rooms.put(rs.getString(1), new Room(rs.getString(1), rs.getInt(2)));
				}
				rs = con.createStatement().executeQuery("SELECT * FROM Users");
				while (rs.next()) {
					users.put(rs.getString(1), new User(rs.getString(1), rs.getString(2), rs.getString(3)));
				}
				rs = con.createStatement().executeQuery("SELECT * FROM Appointments");
				while (rs.next()) {
					appointments.put(rs.getInt(1), new Appointment(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getLong(4), rs.getLong(5), rooms.get(rs.getString(6)), users.get(rs.getString(7))));
				}
				rs = con.createStatement().executeQuery("SELECT * FROM Groups");
				while (rs.next()) {
					groups.put(rs.getString(1), new Group(rs.getString(1), users.get(rs.getString(2))));
				}
				rs = con.createStatement().executeQuery("SELECT * FROM Members");
				while (rs.next()) {
					members.add(new Member(groups.get(rs.getString(1)), users.get(rs.getString(2))));
				}
				rs = con.createStatement().executeQuery("SELECT * FROM Participants");
				while (rs.next()) {
					participants.add(new Participant(appointments.get(rs.getInt(1)), users.get(rs.getString(2)), rs.getLong(3), rs.getString(4)));
				}
				rs = con.createStatement().executeQuery("SELECT * FROM Subgroups");
				while (rs.next()) {
					subgroups.add(new Subgroup(groups.get(rs.getString(1)), groups.get(rs.getString(2))));
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
	
	private void executeSQL(String sqlStatment) {
		Connection con = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection("jdbc:sqlserver://localhost:1433","sa","CalFiftyPassword");
			if (con != null) {
				con.createStatement().execute("USE CalFiftyDB");
				con.createStatement().executeQuery(sqlStatment);
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
	
	Map<Integer, Appointment> getAppointments() {
		return appointments;
	}
	
	void insertAppointment(Appointment appointment) {
		executeSQL("INSERT INTO Appointments VALUES (" + appointment.getAppointmentId() + ", " + appointment.getTitle() + ", " + appointment.getDescription() + ", " + appointment.getStartTime().getTimeInMillis() + ", " + appointment.getFinishTime().getTimeInMillis() + ", " + appointment.getRoom().getRoomNumber() + ", " + appointment.getOwner().getUsername() + ")");
		appointments.put(appointment.getAppointmentId(), appointment);
	}
	
	void updateAppointment(Appointment appointment) {
		executeSQL("UPDATE Appointments SET Title=" + appointment.getTitle() + ", Description=" + appointment.getDescription() + ", StartTime=" + appointment.getStartTime().getTimeInMillis() + ", FinishTime=" + appointment.getFinishTime().getTimeInMillis() + ", RoomNumber=" + appointment.getRoom().getRoomNumber() + ", Owner=" + appointment.getOwner().getUsername() + " WHERE ApplicationID=" + appointment.getAppointmentId());
		appointments.put(appointment.getAppointmentId(), appointment);
	}
	
	void deleteAppointment(Appointment appointment) {
		executeSQL("DELETE FROM Appointments WHERE ApplicationID=" + appointment.getAppointmentId());
		appointments.remove(appointment.getAppointmentId());
	}
	
	Map<String, Group> getGroups() {
		return groups;
	}
	
	ArrayList<Member> getMembers() {
		return members;
	}
	
	ArrayList<Participant> getParticipants() {
		return participants;
	}
	
	void insertParticipant(Participant participant) {
		executeSQL("INSERT INTO Participants VALUES (" + participant.getAppointment().getAppointmentId() + ", " + participant.getUser().getUsername() + ", " + participant.getAlarm().getTimeInMillis() + ", " + participant.getStatus() + ")");
		participants.add(participant);
	}
	
	void updateParticipant(Participant participant) {
		executeSQL("UPDATE Participants SET Alarm=" + participant.getAlarm().getTimeInMillis() + ", Status=" + participant.getStatus() + " WHERE AppointmentID=" + participant.getAppointment().getAppointmentId() + " AND Username=" + participant.getUser().getUsername());
		participants.add(participant);
	}
	
	void deleteParticipant(Participant participant) {
		executeSQL("DELETE FROM Participants WHERE AppointmentID=" + participant.getAppointment().getAppointmentId() + " AND Username=" + participant.getUser().getUsername());
		participants.remove(participant);
	}
	
	Map<String, Room> getRooms() {
		return rooms;
	}
	
	ArrayList<Subgroup> getSubgroups() {
		return subgroups;
	}
	
	Map<String, User> getUsers() {
		return users;
	}

}
