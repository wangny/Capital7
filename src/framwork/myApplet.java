package framwork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import java.util.Random;


@SuppressWarnings("serial")
public class myApplet extends PApplet{
	
	Plate currentp;
	Ani ani;
	StartWindow startwindow;
	HowToPlay howtoplay;
	PImage img;
	PImage img_play;
	public final static int width = 960, height = 840 , cubewidth = 50, cubeheight = 60;
	int readmeX=20,readmeY=30;
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
	
	public boolean readmeDisplay=false;
    
    Random ran = new Random();

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
		howtoplay = new HowToPlay(this);
		currentp = new Plate(this);
		loadData();
		
		gamePhase = 0;
		
		img = loadImage("g1.png"); 
		img_play = loadImage("g2.png");
		
		
		cp5=new ControlP5(this);
		cp5.addButton("Replay")
			.setLabel("R e p l a y")
			.setPosition( (myApplet.width-300)/2, 300)
			.setImage(loadImage("replay.png"))
			.updateSize();
			//.setSize(250,50);
		cp5.addButton("Home")
			.setLabel("H o m e")
			.setPosition( (myApplet.width-300)/2, 600)
			.setImage(loadImage("homebtn.png"))
			.updateSize();
			//.setSize(250,50);
		
		cp5.getController("Replay").hide();
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
		this.connect();
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
			
			
			howtoplay.display();
			
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
			if(currentp.getMusicPlay() && !currentp.getMusicMute()){
				cp5.getController("playMusic").hide();
				cp5.getController("mute").show();
			}else if(currentp.getMusicMute() && !currentp.getMusicPlay()){
				cp5.getController("playMusic").show();
				cp5.getController("mute").hide();
			}
			
			if(gamePhase==1){
				cp5.getController("Resume").hide();
				cp5.getController("Pause").show();
				
			}
			if(gamePhase==5){
				cp5.getController("Resume").show();
				cp5.getController("Pause").hide();
			}
			//this.redraw();
			currentp.display();
			
			
			//image(img_play,0,0);
		}
	}
	
	public void mousePressed(){
		if(gamePhase==1 || gamePhase==2) currentp.mousePressed();
	}
	
	public void mouseDragged(){
		if(gamePhase==1 || gamePhase==2) currentp.mouseDragged();
	}
	
	public void mouseReleased(){
		if(gamePhase==1 || gamePhase==2) currentp.mouseReleased();
	}
	
	public void loadData(){
        JSONObject dataCo = loadJSONObject("resources/cubeColor.json");
        JSONObject dataOb = loadJSONObject("resources/cubeObject.json");
        JSONArray cubes = dataCo.getJSONArray("cubeColor");
        JSONArray cubes1 = dataOb.getJSONArray("cubeObject");
        
        int record[] = new int[8];
        
        for (int i = 0; i < 8; i++){
            //System.out.println("n:"+n);
        	int n = 0;
        	boolean repeat=true;
        	while(repeat){
        		n = ran.nextInt(24);
        		repeat=false;
        		for(int j=0;j<i;j++){
        			if(record[j]==n){
            			repeat=true;
            		}
        		}
        	}
        	record[i] = n;
            JSONObject cubeO = cubes1.getJSONObject(n);
            String name = cubeO.getString("name");
            String target = cubeO.getString("target");
            
            
            JSONObject cube = cubes.getJSONObject(i);
            String colours = cube.getString("colour");
            //
            String[] colour = new String[7];
            colour = colours.split(",");
            //
            Cube c1 = new Cube(name,target,colour);	//???
            currentp.cubeDB.add(c1);
            
            Cube c2 = new Cube(target,name,colour);	//???
            currentp.cubeDB.add(c2);

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
			
			//changePhase(1);
			System.out.println("click one player");
			this.sendMessage("click one player");
			//Thread t = new Thread(currentp);
			//t.start();
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
			
			readmeDisplay = !readmeDisplay;
			
			if(readmeDisplay){
				Ani.to(howtoplay, (float)0.5, "y", 20, Ani.LINEAR);
				cp5.getController("OnePlayer").hide();
				cp5.getController("TwoPlayer").hide();
				cp5.getController("ok").show();
			}
			else if(!readmeDisplay){
				Ani.to(howtoplay, (float)0.5, "y", height+10, Ani.LINEAR);
				cp5.getController("ok").hide();
			}
		}
	}
	
	public void Ok() {
		if(startwindow.cp5.getController("Ok").isVisible()){
			System.out.println("click ok");
			if(bigBtnM.position()==bigBtnM.length()){
				bigBtnM.rewind();
				bigBtnM.play();
			}else{
				bigBtnM.play();
			}
			
			readmeDisplay = !readmeDisplay;
			
			if(readmeDisplay){
				Ani.to(howtoplay, (float)0.5, "y", 20, Ani.LINEAR);
				cp5.getController("OnePlayer").hide();
				cp5.getController("TwoPlayer").hide();
				cp5.getController("ReadMe").hide();
				cp5.getController("ok").show();
			}
			else if(!readmeDisplay){
				Ani.to(howtoplay, (float)0.5, "y", height+10, Ani.LINEAR);
				cp5.getController("ok").hide();
			}
		}
	}
	
	public void returnMenu(){
		if(gamePhase==1) cp5.getController("Replay").show();
		else cp5.getController("Replay").hide();
		cp5.getController("Home").show();
		changePhase(4);
	}
	
	
	public void Replay(){	
		//if(cp5.getController("Replay").isVisible()){
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
			cp5.getController("Replay").hide();
			changePhase(1);
			System.out.println( cp5.getController("Pause").getInfo() );
		//}
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
			sendMessage("return home");
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
					//ObjectInputStream objReader;
					if ( (line = new String(this.reader.readLine()) )!=""){
						System.out.println(line);
						//do something here
						if (line.equals("one player start") && gamePhase==0 ){
							changePhase(1);
							Thread t = new Thread(currentp);
							t.start();
						} else if (line.equals("two players start")  && gamePhase==0 ){
							
							changePhase(2);
							Thread t = new Thread(currentp);
							t.start();
						} else if (line.equals("multi players start")  && gamePhase==0 ){
							//
						} else if (line.equals("attack")){
							if(gamePhase==2){
								currentp.addbound = true;
							}
						} else if (line.equals("youwin")){
							if(gamePhase==2){
								currentp.iswin = true;
							}
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
