package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client implements Runnable {
	
	private Socket socket;
	private Server server;
	
	private BufferedReader reader;
	private OutputStreamWriter writer;
	
	Client(Socket clientSocket, Server server) {
		socket = clientSocket;
	}
	
	public void run() {
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new OutputStreamWriter(socket.getOutputStream());
			while (!socket.isClosed()) {
				String msg = reader.readLine();
				String cmd = msg.substring(0, 6);
				String xml = msg.substring(6);
				if (cmd == "select") {
					server.selectObjects(this);
				} else if (cmd == "insert") {
					server.insertObjects(ConvertXML.XmlToObjects(xml, server.getDatabase()));
				} else if (cmd == "update") {
					server.updateObjects(ConvertXML.XmlToObjects(xml, server.getDatabase()));
				} else if (cmd == "delete") {
					server.deleteObjects(ConvertXML.XmlToObjects(xml, server.getDatabase()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			server.removeClient(this);
		}
	}
	
	void sendMessage(String msg) {
		try {
			writer.write(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
