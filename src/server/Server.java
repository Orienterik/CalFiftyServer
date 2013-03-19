package server;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import data.Appointment;
import data.Participant;

public class Server {
	
	private Database database;
	private ArrayList<Client> clients;
	
	private int nextPort = 50040;
	
	public static void main(String[] args) {
		Server server = new Server();
		new Thread(server.new PortHandler()).start();
		while (true) {
			server.WaitForConnections();
		}
	}
	
	Server() {
		database = new Database();
		clients = new ArrayList<Client>();
	}
	
	private class PortHandler implements Runnable {
		
		public void run() {
			while (true) {
				Socket clientSocket = null;
				try {
					clientSocket = new ServerSocket(50039).accept();
					PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
					writer.println(nextPort++);
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
		}
	
	}
	
	void WaitForConnections() {
		Socket clientSocket = null;
		try {
			clientSocket = new ServerSocket(nextPort).accept();
			Client client = new Client(clientSocket, this);
			clients.add(client);
			new Thread(client).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	Database getDatabase() {
		return database;
	}
	
	void selectObjects(Client client) {
		client.sendMessage("select" + ConvertXML.ObjectsToXml(new ArrayList<Object>(database.getRooms().values())));
		client.sendMessage("select" + ConvertXML.ObjectsToXml(new ArrayList<Object>(database.getUsers().values())));
		client.sendMessage("select" + ConvertXML.ObjectsToXml(new ArrayList<Object>(database.getAppointments().values())));
		client.sendMessage("select" + ConvertXML.ObjectsToXml(new ArrayList<Object>(database.getGroups().values())));
		client.sendMessage("select" + ConvertXML.ObjectsToXml(new ArrayList<Object>(database.getMembers())));
		client.sendMessage("select" + ConvertXML.ObjectsToXml(new ArrayList<Object>(database.getParticipants())));
		client.sendMessage("select" + ConvertXML.ObjectsToXml(new ArrayList<Object>(database.getSubgroups())));
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
