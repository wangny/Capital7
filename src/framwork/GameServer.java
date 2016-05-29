package framwork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameServer {
	private ServerSocket serverSocket;
	private List<ConnectionThread> connections = new ArrayList<ConnectionThread>();
	private final static int port = 8000;
	
	public GameServer(int portNum) {
		try {
			this.serverSocket = new ServerSocket(portNum);
			System.out.printf("Server starts listening on port %d.\n", portNum);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void runForever() {
		System.out.println("Server starts waiting for client.");
		while(true) {
			try {
				Socket connectionToClient = this.serverSocket.accept();
				System.out.println("Get connection from client "
				+ connectionToClient.getInetAddress() + ":"
				+ connectionToClient.getPort());
				// new served client thread start
				ConnectionThread connThread = new ConnectionThread(connectionToClient);
				connThread.start();
				// add the connection thread to a ArrayList, so that we can access it afteresrd.
				this.connections.add(connThread);
			} catch (BindException e){
				e.printStackTrace();
			} catch (IOException e){
				e.printStackTrace();
			}
		}	
	}
	
	public class ConnectionThread extends Thread {
		private Socket socket;
		private BufferedReader reader;
		private PrintWriter writer;
		public ConnectionThread(Socket socket) {
			this.socket = socket;
			try {
				this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			} catch (IOException e){
				e.printStackTrace();
			}
		}
		public void run() {
			while(true) {
				try {
					String line = new String(this.reader.readLine());
					broadcast(line);
					System.out.println(line); // line is message from server
					if (line.equals("click one player")){
						sendMessage("one player start");
					} else if (line.equals("click two players")){
						
					}
				} catch (IOException e){
					e.printStackTrace();
				}
			}
		}
		public void sendMessage(String message) {
			this.writer.println(message);
			this.writer.flush();
		}
	}
	
	private void broadcast(String message) {
		for (ConnectionThread connection: connections) {
			connection.sendMessage(message);
		}
	}
	
	public static void main(String[] args) {
		GameServer server = new GameServer(port);
		server.runForever();
	}
}
