package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import data.*;

public class Server {
	
	private Database db;
	private ArrayList<Client> clients;
	
	public static void main(String[] args) {
		Server server = new Server();
		while (true) {
			server.WaitForConnections();
		}
	}
	
	Server() {
		db = new Database();
		clients = new ArrayList<Client>();
		ObjectsToXml(new ArrayList<Object>(db.getAllAppointments().values()));
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
	
	String HandleRequest(String request) {
		String cmd = request.substring(0, 6);
		String data = request.substring(6);
		if (cmd == "select") {
			
		} else if (cmd == "insert") {
			
		} else if (cmd == "update") {
			
		} else if (cmd == "delete") {
			
		}
		return null;
	}
	
	String ObjectsToXml(ArrayList<Object> objects) {
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			for (int i = 0; i < objects.size(); i++) {
				if (objects.get(i) instanceof Appointment) {
					Appointment appointment = (Appointment) objects.get(i);
					Element xmlAppointment = doc.createElement("appointment");
					xmlAppointment.setAttribute("appointmentId", String.valueOf(appointment.getAppointmentId()));
					xmlAppointment.setAttribute("title", appointment.getTitle());
					xmlAppointment.setAttribute("descpiption", appointment.getDescription());
					xmlAppointment.setAttribute("startTime", String.valueOf(appointment.getStartTime().getTimeInMillis()));
					xmlAppointment.setAttribute("finishTime", String.valueOf(appointment.getFinishTime().getTimeInMillis()));
					xmlAppointment.setAttribute("room", appointment.getRoom().getRoomNumber());
					xmlAppointment.setAttribute("owner", appointment.getOwner().getUsername());
					doc.appendChild(xmlAppointment);
				}
			}
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.transform(new DOMSource(doc), new StreamResult(System.out));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	ArrayList<Object> XmlToObjects(String xml) {
		return null;
	}
	
	void removeClient(Client client) {
		clients.remove(client);
	}

}
