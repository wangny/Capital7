package framwork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
//6 plates, 
public class GameServer {
	private ServerSocket serverSocket;
	private List<ConnectionThread> connections = new ArrayList<ConnectionThread>();
	private final static int port = 8000;
	private int playerNum;
	
	public GameServer(int portNum) {
		try {
			this.serverSocket = new ServerSocket(portNum);
			System.out.printf("Server starts listening on port %d.\n", portNum);
			playerNum = 0;
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
				//
				while (!connThread.start){
					for (ConnectionThread conn: connections){
						//System.out.println("for loop");
						if (conn.oneP){
							System.out.println("one player start");
							conn.sendMessage("one player start");
							conn.start = true;
						} else if (conn.twoP) {
							playerNum++;
							if (playerNum==2){
								for (ConnectionThread c: connections){
									if (!c.start){
										c.sendMessage("two players start");
										c.start = true;
									}
								}
								playerNum = 0;
							}
							System.out.println("in two player");
							break;
						} else {
							//multi player
						}
					}
				}
				System.out.println("Server is waiting...");
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
		private boolean start, oneP, twoP, multiP;
		public ConnectionThread(Socket socket) {
			this.socket = socket;
			try {
				this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				oneP = false; twoP = false; multiP = false;
			} catch (IOException e){
				e.printStackTrace();
			}
		}
		public void run() {
			while(true) {
				try {
					//ObjectInputStream objReader;
					String line = new String(this.reader.readLine());
						//do something here
						if (line.equals("click one player")){
							oneP = true;
							//sendMessage("one player start");
						} else if (line.equals("click two players")){
							twoP = true;
							//sendMessage("two players start");
						} else if (line.equals("click multi players")){
							multiP = true;
							//sendMessage("multi players start");
						}
				} /*else if ( (objReader = new ObjectInputStream(socket.getInputStream())) != null ){
						Cube c = (Cube)objReader.readObject();
						System.out.println(c.getState());
						c.setState(3);
					}*/
				 catch (Exception e){
					e.printStackTrace();
				}
			}
		}
		public void sendMessage(String message) {
			this.writer.println(message);
			this.writer.flush();
		}
		
		/*public void sendObject(Object o){
			try {
				ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());
				writer.writeObject(o);
			} catch (Exception e){
				e.printStackTrace();
			}
		}*/
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
