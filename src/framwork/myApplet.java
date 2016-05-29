package framwork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import controlP5.ControlP5;
import de.looksgood.ani.Ani;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;

@SuppressWarnings("serial")
public class myApplet extends PApplet{
	
	Plate currentp;
	Ani ani;
	StartWindow startwindow;
	PImage img;
	PImage img_play;
	public final static int width = 800, height = 700 , cubewidth = 50, cubeheight = 60;
	int gamePhase ; /// 0 : startwindow, 1 : single, 2 : two player, 3 : multi , 4 :replay; 5:pause
	
	public ControlP5 cp5;
	//for server communication
	private String destinationIPAddr = "192.168.12.1";
	private int destinationPortNum = 8000;
	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;

	public void setup(){
		
		size(width, height);
		
		background(240);
		smooth();
		
		Ani.init(this);
		
		startwindow = new StartWindow(this);
		
		currentp = new Plate(this);
		loadData();
		
		gamePhase = 0;
		
		img = loadImage("g.png"); 
		img_play = loadImage("g2.png");
		
		cp5=new ControlP5(this);
		cp5.addButton("Replay")
			.setLabel("R e p l a y")
			.setPosition(myApplet.width-525, myApplet.height-260)
			.setSize(250,50);
		cp5.addButton("Home")
			.setLabel("H o m e")
			.setPosition(myApplet.width-525, myApplet.height-190)
			.setSize(250,50);
		cp5.getController("Replay")
	       .getCaptionLabel()
	       .setSize(22);
		cp5.getController("Home")
	       .getCaptionLabel()
	       .setSize(22);
		cp5.getController("Replay").setVisible(false);
		cp5.getController("Home").setVisible(false);
		
		cp5 .addButton("Pause")
			.setLabel("I I")
			.setPosition(myApplet.width-65, 10)
			.setSize(40,40);
		cp5 .getController("Pause")
			.getCaptionLabel()
			.setSize(25);
		cp5.getController("Pause").setVisible(false);
		
		cp5	.addButton("Resume")
			.setLabel("R e s u m e")//setimages
			.setPosition(myApplet.width-525, myApplet.height-190)
			.setSize(250,50);
		cp5 .getController("Resume")
			.getCaptionLabel()
			.setSize(22);
		//cp5.getController("Resume").setVisible(true);	
		//connect to server
		//this.connect();
		//this.sendObject(currentp.cubeDB.get(0));
	}
	
	public void draw(){
		//plate background and frame
		image(img,0,0); 
		
		
		System.out.println(cp5.getController("Pause").isVisible());
		
		if(gamePhase==0){
			//image(img,0,0);
			startwindow.display();
			cp5.getController("Replay").setVisible(false);
			cp5.getController("Home").setVisible(false);
			cp5.getController("Resume").setVisible(false);
			cp5.getController("Pause").setVisible(false);
		}
		else /*if(gamePhase==1)*/{
			
			fill(255);
			rect(49, 20, width-101, height-82, 15);
			startwindow.cp5.getController("OnePlayer").setVisible(false);
			startwindow.cp5.getController("TwoPlayer").setVisible(false);
			startwindow.cp5.getController("MultiPlayer").setVisible(false);
			if(gamePhase<=3){
				//cp5.getController("Resume").setVisible(false);
				cp5.getController("Pause").setVisible(true);
			}
			if(gamePhase==5){
				cp5.getController("Resume").setVisible(true);
				//cp5.getController("Pause").setVisible(false);
			}
			//this.redraw();
			currentp.display();
			
			
			//image(img_play,0,0);
		}
	}
	
	public void mousePressed(){
		if(gamePhase<=3) currentp.mousePressed();
	}
	
	public void mouseDragged(){
		if(gamePhase<=3) currentp.mouseDragged();
	}
	
	public void mouseReleased(){
		if(gamePhase<=3) currentp.mouseReleased();
	}
	
	public void loadData(){
		JSONObject data = loadJSONObject("resources/cube.json");
		JSONArray cubes = data.getJSONArray("cube");
		for (int i = 0; i < cubes.size(); i++){
			JSONObject cube = cubes.getJSONObject(i);
			String name = cube.getString("name");
			String target = cube.getString("target");
			String colours = cube.getString("colour");
			//
			String[] colour = new String[7];
			colour = colours.split(",");
			/*for (int j = 0; j < 7; j++){
				colour[j] = colours.substring(9*j, 9*j+8);
			}*/
			//
			Cube c = new Cube(name,target,colour);	//???
			currentp.cubeDB.add(c);
		}
	}
	
	public void changePhase(int phase){
		gamePhase = phase;
	}
	
	public void OnePlayer(){
		if(startwindow.cp5.getController("OnePlayer").isVisible()){	
			changePhase(1);
			System.out.println("click one player");
			//this.sendMessage("click one player");
			Thread t = new Thread(currentp);
			t.start();
		}
	}
	
	public void TwoPlayer(){
		if (startwindow.cp5.getController("TwoPlayer").isVisible()){
			System.out.println("click two players");
			this.sendMessage("click two players");
		}
	}
	
	public void returnMenu(){
		cp5.getController("Replay").setVisible(true);
		cp5.getController("Home").setVisible(true);
		changePhase(4);
	}
	
	
	public void Replay(){	
		if(cp5.getController("Replay").isVisible()){
			currentp.reset();
			Thread t = new Thread(currentp);
			t.start();
			System.out.println("click Replay");
			cp5.getController("Replay").setVisible(false);
			cp5.getController("Home").setVisible(false);
			changePhase(1);
		}
	}
	
	public void Home(){	
		//if(gamePhase==4){
			currentp.reset();
			cp5.getController("Replay").setVisible(false);
			cp5.getController("Home").setVisible(false);
			this.clear();	
			changePhase(0);
			System.out.println("click home");
		//}
	}
	
	public void Resume(){
		System.out.println("click resume");
		if(cp5.getController("Resume").isVisible()){
			cp5.getController("Resume").setVisible(false);
			//cp5.getController("Pause").setVisible(true);
			changePhase(1);
			//redraw();
		}
		System.out.println(gamePhase);
		
	}
	
	public void Pause(){
		System.out.println("click pause");
		
		if(cp5.getController("Pause").isVisible()){
			//cp5.getController("Resume").setVisible(true);
			//cp5.getController("Pause").setVisible(false);
			changePhase(5);
			//redraw();
		}
		//System.out.println(gamePhase);
		
	}
	
	//server communication classes
	public void connect() {
		// Create socket & thread, remember to start your thread
		try {
			//create socket
			socket = new Socket(this.destinationIPAddr, this.destinationPortNum);
			//create thread
			writer = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			ClientThread connection = new ClientThread(reader);
			connection.start();
			/*connection = new ClientThread(reader);
			connection.start();*/
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (ConnectException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	class ClientThread extends Thread {
		private BufferedReader reader;
		public ClientThread(BufferedReader reader) {
			this.reader = new BufferedReader(reader);
		}
		public void run() {
			while(true) {
				try {
					String line = new String(this.reader.readLine());
					
					if ( (line = new String(this.reader.readLine()) )!=""){
						//do something here
						if (line.equals("one player start")){
							changePhase(1);
							Thread t = new Thread(currentp);
							t.start();
						} else if (line.equals("two players start")){
							changePhase(1);
							Thread t = new Thread(currentp);
							t.start();
						} else if (line.equals("multi players start")){
							//
						}
					} /*else if ( (objReader = new ObjectInputStream(socket.getInputStream())) != null ){
						Cube c = (Cube)objReader.readObject();
						System.out.println(c.getState());
					}*/
					
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public void sendMessage(String message) {
//		System.out.println(SwingUtilities.isEventDispatchThread());
		this.writer.println(message);
		this.writer.flush();
	}
	
	public void sendObject(Object o){
		try {
			ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());
			writer.writeObject(o);
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
}
