package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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
		return null;
	}
	
	void removeClient(Client client) {
		clients.remove(client);
	}

}
