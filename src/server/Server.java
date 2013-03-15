package server;

import java.io.StringWriter;
import java.io.Writer;
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
//		while (true) {
//			server.WaitForConnections();
//		}
	}
	
	Server() {
		db = new Database();
		clients = new ArrayList<Client>();
		System.out.println(ObjectsToXml(new ArrayList<Object>(db.getAllAppointments().values())));
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
			Element root = doc.createElement("objects");
			for (int i = 0; i < objects.size(); i++) {
				if (objects.get(i) instanceof Appointment) {
					Appointment appointment = (Appointment) objects.get(i);
					Element element = doc.createElement("appointment");
					element.setAttribute("appointmentId", String.valueOf(appointment.getAppointmentId()));
					element.setAttribute("title", appointment.getTitle());
					element.setAttribute("descpiption", appointment.getDescription());
					element.setAttribute("startTime", String.valueOf(appointment.getStartTime().getTimeInMillis()));
					element.setAttribute("finishTime", String.valueOf(appointment.getFinishTime().getTimeInMillis()));
					element.setAttribute("room", (appointment.getRoom() != null) ? appointment.getRoom().getRoomNumber() : "null");
					element.setAttribute("owner", (appointment.getOwner() != null) ? appointment.getOwner().getUsername() : "null");
					root.appendChild(element);
				}
			}
			doc.appendChild(root);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			Writer writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			return writer.toString();
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
