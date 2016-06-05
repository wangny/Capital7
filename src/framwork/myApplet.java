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
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
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
	PImage explan;
	public final static int width = 960, height = 840 , cubewidth = 50, cubeheight = 60;
	int readmeX=20,readY=30;
	int gamePhase ; /// 0 : startwindow, 1 : single, 2 : two player, 3 : multi , 4 :replay; 5:pause
	
	public ControlP5 cp5;
	//for server communication
	private String destinationIPAddr = "192.168.12.1";
	private int destinationPortNum = 8000;
	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;
	
	Minim minim;
	static AudioPlayer homeBgM,playBgM,dieM,disappearM,mixM,bigBtnM,throwLineM,littleBtnM;

	public void setup(){
		
		size(width, height);
		
		//music
		minim = new Minim(this);
		homeBgM = minim.loadFile("bg1.wav");
		playBgM = minim.loadFile("bg2.wav",2048);
		dieM = minim.loadFile("die.wav");
		disappearM = minim.loadFile("disappear.wav");
		mixM = minim.loadFile("mix.wav");
		bigBtnM = minim.loadFile("replay.wav");
		throwLineM = minim.loadFile("throwline.wav");
		littleBtnM = minim.loadFile("btnselect3.wav");
		
		background(240);
		smooth();
		
		Ani.init(this);
		
		startwindow = new StartWindow(this);
		
		currentp = new Plate(this);
		loadData();
		
		gamePhase = 0;
		
		img = loadImage("g1.png"); 
		img_play = loadImage("g2.png");
		explan = loadImage("explantation.png");
		
		
		cp5=new ControlP5(this);
		cp5.addButton("Replay")
			.setLabel("R e p l a y")
			.setPosition( (myApplet.width-300)/2, 300)
			.setImage(loadImage("replay.png"))
			.updateSize();
			//.setSize(250,50);
		cp5.addButton("Home")
			.setLabel("H o m e")
			.setPosition( (myApplet.width-300)/2, 400)
			.setImage(loadImage("homebtn.png"))
			.updateSize();
			//.setSize(250,50);
		
		cp5.getController("Replay").setVisible(false);
		cp5.getController("Home").setVisible(false);
		
		cp5 .addButton("Pause")
			.setPosition(830, 150)
			.setImage(loadImage("pauseBtn.png"))
			.updateSize();
			
		
		cp5	.addButton("Resume")
			.setPosition( (myApplet.width-300)/2, 300)
			.setImage(loadImage("resumeBtn.png"))
			.updateSize();
			
		
		cp5 .addButton("playMusic")
			.setPosition(830,230)
			.setImage(loadImage("musicBtn.png"))
			.updateSize();
		
		cp5 .addButton("mute")
			.setPosition(830,230)
			.setImage(loadImage("muteBtn.png"))
			.updateSize();
		//cp5.getController("Resume").setVisible(true);	
		//connect to server
		//this.connect();
		//this.sendObject(currentp.cubeDB.get(0));
	}
	
	public void draw(){
		//plate background and frame
		//image(img,0,0); 
	
		
		
		//System.out.println(cp5.getController("Pause").isVisible());
		
		if(gamePhase==0){
			image(img,0,0);
			playBgM.pause();
			playBgM.rewind();
			homeBgM.play();
			startwindow.display();
			cp5.getController("Replay").hide();
			cp5.getController("Home").hide();
			cp5.getController("Resume").hide();
			cp5.getController("Pause").hide();
			cp5.getController("playMusic").hide();
			cp5.getController("mute").hide();
		}
		else /*if(gamePhase==1)*/{
			
			fill(255);
			rect(80, 150, width-260, height-220, 15);//(49, 20, width-101, height-82, 15);
			
			homeBgM.pause();homeBgM.rewind();
			if(currentp.getGameCondition()) playBgM.pause();
			else if(currentp.getMusicMute()) playBgM.pause();
			else if(currentp.getMusicPlay()) playBgM.play();
			else playBgM.play();
			
			startwindow.cp5.getController("OnePlayer").hide();
			startwindow.cp5.getController("TwoPlayer").hide();
			startwindow.cp5.getController("ReadMe").hide();
			
			if(gamePhase<=3){
				cp5.getController("Resume").hide();
				cp5.getController("Pause").show();
				if(currentp.getMusicPlay() && !currentp.getMusicMute()){
					cp5.getController("playMusic").setVisible(false);
					cp5.getController("mute").setVisible(true);
				}else if(currentp.getMusicMute() && !currentp.getMusicPlay()){
					cp5.getController("playMusic").setVisible(true);
					cp5.getController("mute").setVisible(false);
				}
				
			}
			if(gamePhase==5){
				cp5.getController("Resume").setVisible(true);
				if(currentp.getMusicPlay() && !currentp.getMusicMute()){
					cp5.getController("playMusic").setVisible(false);
					cp5.getController("mute").setVisible(true);
				}else if(currentp.getMusicMute() && !currentp.getMusicPlay()){
					cp5.getController("playMusic").setVisible(true);
					cp5.getController("mute").setVisible(false);
				}
				//cp5.getController("Pause").getValueLabel().setVisible(false);
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
			if(bigBtnM.position()==bigBtnM.length()){
				bigBtnM.rewind();
				bigBtnM.play();
			}else{
				bigBtnM.play();
			}
			
			changePhase(1);
			System.out.println("click one player");
			//this.sendMessage("click one player");
			Thread t = new Thread(currentp);
			t.start();
		}
	}
	
	public void TwoPlayer(){
		if (startwindow.cp5.getController("TwoPlayer").isVisible()){
			if(bigBtnM.position()==bigBtnM.length()){
				bigBtnM.rewind();
				bigBtnM.play();
			}else{
				bigBtnM.play();
			}
			
			System.out.println("click two players");
			this.sendMessage("click two players");
		}
	}
	
	public void ReadMe() {
		if(startwindow.cp5.getController("ReadMe").isVisible()){
			System.out.println("click read me");
			if(bigBtnM.position()==bigBtnM.length()){
				bigBtnM.rewind();
				bigBtnM.play();
			}else{
				bigBtnM.play();
			}
			//Ani.to(explan, 500000, "readmeY", Ani.SINE_IN);
		}
	}
	
	public void returnMenu(){
		cp5.getController("Replay").show();
		cp5.getController("Home").show();
		changePhase(4);
	}
	
	
	public void Replay(){	
		if(cp5.getController("Replay").isVisible()){
			if(bigBtnM.position()==bigBtnM.length()){
				bigBtnM.rewind();
				bigBtnM.play();
			}else{
				bigBtnM.play();
			}
			
			
			currentp.reset();
			Thread t = new Thread(currentp);
			t.start();
			System.out.println("click Replay");
			//cp5.getController("Replay").getValueLabel().hide();
			cp5.getController("Home").hide();
			this.clear();
			changePhase(1);
			System.out.println( cp5.getController("Pause").getInfo() );
		}
	}
	
	public void Home(){	
		//if(gamePhase==4){
			currentp.reset();
			if(bigBtnM.position()==bigBtnM.length()){
				bigBtnM.rewind();
				bigBtnM.play();
			}else{
				bigBtnM.play();
			}
			cp5.getController("Replay").hide();
			cp5.getController("Home").hide();
			this.clear();
			changePhase(0);
			System.out.println("click home");
		//}
	}
	
	public void Resume(){
		System.out.println("click resume");
		if(bigBtnM.position()==bigBtnM.length()){
			bigBtnM.rewind();
			bigBtnM.play();
		}else{
			bigBtnM.play();
		}
		if(cp5.getController("Resume").isVisible()){
			cp5.getController("Resume").getValueLabel().hide();

			changePhase(1);
		}
		System.out.println(gamePhase);
		
	}
	
	public void Pause(){
		System.out.println("click pause");		
		littleBtnM.rewind();
		littleBtnM.play();
		
		if(cp5.getController("Pause").getValueLabel().isVisible()){
			cp5.getController("Resume").show();
			cp5.getController("Pause").hide();
			
			changePhase(5);
		}
		
	}
	
	public void playMusic() {
		System.out.println("click playMusic");
		littleBtnM.rewind();
		littleBtnM.play();
		
		if(cp5.getController("playMusic").isVisible()){
			currentp.setMusicPlay();
		}else if(cp5.getController("mute").isVisible()){
			currentp.setMusicMute();
		}
		
	}
	
	public void mute() {
		System.out.println("click mute");
		littleBtnM.rewind();
		littleBtnM.play();
		
		if(cp5.getController("playMusic").isVisible()){
			currentp.setMusicPlay();
		}else if(cp5.getController("mute").isVisible()){
			currentp.setMusicMute();
		}
		
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
					String line;
					ObjectInputStream objReader;
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
					} else if ( (objReader = new ObjectInputStream(socket.getInputStream())) != null ){
						Cube c = (Cube)objReader.readObject();
						System.out.println(c.getState());
					}
					
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
