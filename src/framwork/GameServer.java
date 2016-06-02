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
public class GameServer implements Runnable{
	private ServerSocket serverSocket;
	private List<ConnectionThread> connections = new ArrayList<ConnectionThread>();
	private final static int port = 8000;
	private int playerNum;	///available , not playing client number
	
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
				//connThread.ip = connectionToClient.getInetAddress().getHostAddress();
				playerNum++;
				connThread.start();
				// add the connection thread to a ArrayList, so that we can access it afteresrd.
				this.connections.add(connThread);
				System.out.println(playerNum);
					
				//System.out.println("Server is waiting...");
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
		private String ip;
		private ConnectionThread target;
		
		public ConnectionThread(Socket socket) {
			this.socket = socket;
			
			try {
				this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				start = oneP = twoP = multiP = false;
				target = null;
			} catch (IOException e){
				e.printStackTrace();
			}
		}
		public void run() {
			while(true) {
				try {
					//ObjectInputStream objReader;
					String line = new String(this.reader.readLine());
					System.out.println(line);	
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
					} else if(line.equals("attack")){
						if(twoP) target.sendMessage("attack");
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
		Thread t = new Thread(server);
		t.start();
		server.runForever();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			if (playerNum>=1) {
				for (ConnectionThread conn: connections){
					if (!conn.start) {
						if (conn.oneP) {
							conn.sendMessage("one player start");
							conn.oneP = true;
							playerNum --;
							break;
						}
					}
				}
			}
			if (playerNum>=2) {
				ConnectionThread ct1 = null, ct2 = null;
				int twop_num = 0;
				for (ConnectionThread conn: connections){
					if (!conn.start){
						if (conn.twoP==true) {
							if (ct1==null) ct1 = conn;
							else if(ct2==null) ct2 = conn;
							twop_num++;
							System.out.println(twop_num);
						}
						
						if(twop_num==2){
							ct1.sendMessage("two players start");
							System.out.println("two players start");
							ct1.start = true;
							ct1.target = ct2;
							ct2.sendMessage("two players start");
							System.out.println("two players start");
							ct2.start = true;
							ct2.target = ct1;
							playerNum = playerNum-2;
							twop_num = 0;
							ct1 = null;
							ct2 = null;
						}
					}
				}
				
			}
			//else {
				//
			//}
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
