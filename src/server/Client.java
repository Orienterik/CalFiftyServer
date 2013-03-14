package server;

import java.io.BufferedReader;
import java.io.IOException;
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
				server.HandleRequest(reader.readLine());
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
	
	void sendUpdate(String message) {
		try {
			writer.write(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
