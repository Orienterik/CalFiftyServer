package server;

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
	}
	
	void WaitForConnections() {
		
	}

}
