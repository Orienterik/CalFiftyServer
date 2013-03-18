package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import data.Appointment;
import data.Participant;

public class Server {
	
	private Database database;
	private ArrayList<Client> clients;
	
	public static void main(String[] args) {
		Server server = new Server();
		while (true) {
			server.WaitForConnections();
		}
	}
	
	Server() {
		database = new Database();
		clients = new ArrayList<Client>();
	}
	
	void WaitForConnections() {
		Socket clientSocket = null;
		try {
			clientSocket = new ServerSocket(50039).accept();
			Client client = new Client(clientSocket, this);
			clients.add(client);
			new Thread(client).run();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				clientSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	Database getDatabase() {
		return database;
	}
	
	void selectObjects(Client client) {
		ArrayList<Object> objects = new ArrayList<Object>();
		objects.addAll(database.getAppointments().values());
		objects.addAll(database.getGroups().values());
		objects.addAll(database.getMembers());
		objects.addAll(database.getParticipants());
		objects.addAll(database.getRooms().values());
		objects.addAll(database.getSubgroups());
		objects.addAll(database.getUsers().values());
		String msg = ConvertXML.ObjectsToXml(objects);
		client.sendMessage(msg);
	}
	
	void insertObjects(ArrayList<Object> objects) {
		for (int i = 0; i < objects.size(); i++) {
			if (objects.get(i) instanceof Appointment) {
				database.insertAppointment((Appointment) objects.get(i));
			} else if (objects.get(i) instanceof Participant) {
				database.insertParticipant((Participant) objects.get(i));
			}
		}
		String msg = "insert" + ConvertXML.ObjectsToXml(objects);
		for (int i = 0; i < clients.size(); i++) {
			clients.get(i).sendMessage(msg);
		}
	}
	
	void updateObjects(ArrayList<Object> objects) {
		for (int i = 0; i < objects.size(); i++) {
			if (objects.get(i) instanceof Appointment) {
				database.updateAppointment((Appointment) objects.get(i));
			} else if (objects.get(i) instanceof Participant) {
				database.updateParticipant((Participant) objects.get(i));
			}
		}
		String msg = "update" + ConvertXML.ObjectsToXml(objects);
		for (int i = 0; i < clients.size(); i++) {
			clients.get(i).sendMessage(msg);
		}
	}
	
	void deleteObjects(ArrayList<Object> objects) {
		for (int i = 0; i < objects.size(); i++) {
			if (objects.get(i) instanceof Appointment) {
				database.deleteAppointment((Appointment) objects.get(i));
			} else if (objects.get(i) instanceof Participant) {
				database.deleteParticipant((Participant) objects.get(i));
			}
		}
		String msg = "delete" + ConvertXML.ObjectsToXml(objects);
		for (int i = 0; i < clients.size(); i++) {
			clients.get(i).sendMessage(msg);
		}
	}
	
	void removeClient(Client client) {
		clients.remove(client);
	}

}
