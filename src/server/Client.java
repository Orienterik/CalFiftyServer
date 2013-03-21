package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {
	
	private Socket socket;
	private Server server;
	
	private BufferedReader reader;
	private PrintWriter writer;
	
	Client(Socket clientSocket, Server server) {
		socket = clientSocket;
		this.server = server;
	}
	
	public void run() {
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream(), true);
			while (!socket.isClosed()) {
				String msg = reader.readLine();
				String cmd = msg.substring(0, 6);
				String xml = msg.substring(6);
				if (cmd.equals("select")) {
					server.selectObjects(this, socket);
				} else if (cmd.equals("insert")) {
					server.insertObjects(ConvertXML.XmlToObjects(xml, server.getDatabase()), socket);
				} else if (cmd.equals("update")) {
					server.updateObjects(ConvertXML.XmlToObjects(xml, server.getDatabase()), socket);
				} else if (cmd.equals("delete")) {
					server.deleteObjects(ConvertXML.XmlToObjects(xml, server.getDatabase()), socket);
				}
			}
		} catch (Exception e) {
//			e.printStackTrace();
		} finally {
			try {
				socket.close();
				reader.close();
				writer.close();
			} catch (Exception e) {
//				e.printStackTrace();
			}
			server.removeClient(this, socket);
		}
	}
	
	void sendMessage(String msg) {
		writer.println(msg);
	}

}
