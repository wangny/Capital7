package framwork;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import de.looksgood.ani.Ani;
import processing.core.PFont;

public class Plate implements Runnable{
	private ArrayList<Cube> cubes;
	public ArrayList<Cube> cubeDB;
	private ArrayList<Bound> bounds;
	private myApplet parent;
	private ProgressBar pbar;
	private Cube dragCube;
	private Random random;
	private boolean GameOver;
	private boolean mute=false;
	private boolean playBgMusic=true;
	private int score;
	boolean addbound;
	private static int inix=130, iniy=myApplet.height-150, finalx=inix+540 ,finaly=180; 
	
	public Plate(myApplet applet){
		this.parent = applet;
		cubes = new ArrayList<Cube>();
		cubeDB = new ArrayList<Cube>();
		bounds = new ArrayList<Bound>();
		dragCube = new Cube();
		random = new Random();
		
		pbar = new ProgressBar(parent);
	
		GameOver = false;
		addbound = false;
		score = 0;
	}
	
	public void display(){	
		
		if((myApplet.playBgM.position()==myApplet.playBgM.length()) && GameOver==false && !mute && playBgMusic){
			myApplet.playBgM.rewind();
			myApplet.playBgM.play();
		}else if(mute && GameOver==false){
			myApplet.playBgM.pause();
		}else if(GameOver==false){
			myApplet.dieM.pause();
		}else if(GameOver==true){
			myApplet.playBgM.pause();
		}
		
		
		for(int i=0; i<cubes.size(); i++){
			cubes.get(i).display();
		}
		
		for(int i=0; i<bounds.size(); i++) bounds.get(i).display();
		
		parent.image(parent.img_play,0,0);	///background
		parent.stroke(200);
		parent.strokeWeight(5);
		parent.noFill();
		parent.rect(80, 150, myApplet.width-260, myApplet.height-220, 15);//(50, 20, myApplet.width-100, myApplet.height-80, 15);
		parent.noStroke();
		
		pbar.display();	
		
		parent.fill(myApplet.unhex("FF0000CD"));	///score
		parent.textSize(30);
		PFont font;
		font = parent.createFont("Cooper Black",40);
		parent.textFont(font);
		parent.text(((Integer)score).toString() , 700 , 100);
		font = parent.createFont("Franklin Gothic Medium Cond", 18);
		parent.textFont(font);
	}

	
	private void addCube(){		
		System.out.println("add cube");
		
		int x = 0;
		for(int i=0; i<10; i++){
			//random.setSeed(random.nextLong());
			Cube c = new Cube(parent, 0, cubeDB.get(random.nextInt(cubeDB.size())), inix+x, iniy+70);
			cubes.add(c);
			x = x + myApplet.cubewidth + 10;
		}
		
		for(Cube c : cubes){
			if(!c.isDragged()){
				int tmp = c.getY();
				
				//if(GameOver) Ani.to(c, (float)0.3, "y", tmp-40, Ani.LINEAR);
				Ani.to(c, (float)0.5, "y", tmp-myApplet.cubeheight-10, Ani.LINEAR);
				//if(tmp-myApplet.cubeheight-10 < finaly) GameOver=true;
			}
		}
		
	}

	public void mousePressed(){
		//System.out.println("mouse pressed");
		for (Cube c: cubes){
			if ( (parent.mouseX>c.getX()&&parent.mouseX<c.getX()+myApplet.cubewidth) && (parent.mouseY>c.getY()&&parent.mouseY<c.getY()+myApplet.cubeheight)){
				dragCube = c;
				dragCube.setDrag(true);
			}
		}
	}
	
	public void mouseDragged(){
		
		
		boolean hitx = false, hity=false;
		int diffx = parent.mouseX-parent.pmouseX;
		int diffy = parent.mouseY-parent.pmouseY;
		
		for (int i = 0; i < cubes.size(); i++){
			Cube c = cubes.get(i);
			if (c!=dragCube){
				//out of boundary
				if(dragCube.getX()+diffx>=finalx || dragCube.getX()+diffx<=inix ) hitx = true;
				if(dragCube.getY()+diffy>=iniy+10 || dragCube.getY()+diffy<=finaly) hity = true;
				//judge if hit the match cube
				if(Math.abs(dragCube.getX()+diffx-c.getX())<myApplet.cubewidth && Math.abs(dragCube.getY()+diffy-c.getY())<myApplet.cubeheight){
					//judge if match
					if(c.getTarget().equals(dragCube.getName())){/// is match, do merging animation
						System.out.println("match");
						merge(c, dragCube);
						cubes.remove(dragCube);  
						dragCube = new Cube();	//important! not to re-judge
					}else{	///else don't move
						if(Math.abs(dragCube.getX()+diffx-c.getX())<myApplet.cubewidth) hitx = true;
						if(Math.abs(dragCube.getY()+diffy-c.getY())<myApplet.cubeheight) hity = true;
					}
				}
			}
		}
		
		
		if(!hitx)dragCube.addX(diffx);
		if(!hity)dragCube.addY(diffy);
		
	}
	
	private void merge(Cube a, Cube b){ ///will always remove b
		//judge which to remain/remove
		if (a.getState()>b.getState()){
			//a is lighter
			System.out.println(a.getState());
			a.setState(a.getState()+b.getState()+1);
			System.out.println(a.getState());
			cubes.remove(b);
			myApplet.mixM.rewind();
			myApplet.mixM.play();
		} else {
			//b is lighter
			System.out.println(a.getState());
			a.setState(b.getState()+b.getState()+1);
			System.out.println(a.getState());
			cubes.remove(b);
			myApplet.mixM.rewind();
			myApplet.mixM.play();
		}
		
		if(a.getState()>=7){
			cubes.remove(a);
			score++;
			System.out.println(score);
			//parent.sendMessage("attack");
			myApplet.disappearM.rewind();
			myApplet.disappearM.play();
		}
		//myApplet.mixM.pause();
		//myApplet.disappearM.pause();
		
	}
	
	public void mouseReleased(){
		//set dragCube in position
		
		///adjust to right x position
		int tmp = (dragCube.getX()-inix)/(myApplet.cubewidth+10);
		if( (dragCube.getX()-inix)%(myApplet.cubewidth+10) > ((myApplet.cubewidth+10))/2 ) dragCube.setX(inix+(tmp+1)*(myApplet.cubewidth+10) );
		else if(  (dragCube.getX()-inix)%(myApplet.cubewidth+10) <= ((myApplet.cubewidth+10))/2 ) dragCube.setX(inix+tmp*(myApplet.cubewidth+10) );
		
		///adjust to right y position (for all cubes)
		int higest = myApplet.height-80;
		for (Cube c: cubes) if(c.getX()==dragCube.getX() && c.getY()<higest && c!=dragCube && c.getY()>dragCube.getY()) higest = c.getY();
		higest = higest - 10 - myApplet.cubeheight;
		Ani.to(dragCube, (float)0.3, "y", higest, Ani.LINEAR);
		
		dragCube.setDrag(false);
		//dragCube = new Cube();
	}
	
	public void reset(){	///for restarting the game
		cubes.clear();
		bounds.clear();
		dragCube = new Cube();
		random = new Random();
		pbar = new ProgressBar(parent);
	
		GameOver = false;
		addbound = false;
		score = 0;
		iniy=myApplet.height-150;
		
		if(GameOver==false) myApplet.dieM.cue(0);
	}
	
	private void addBound(){
		
		Bound  b = new Bound(parent,inix-50, iniy+10);
		bounds.add(b);
		
		iniy -= 70;
		
		for(Cube c : cubes){
			if(c!=dragCube){
				c.setY(c.getY()-70);
			}
		}
		
	}
	
	public void sortCubes(){
		//sort cubes:
		cubes.sort(new Comparator<Cube>(){
			@Override
			public int compare(Cube c1, Cube c2) {
				if (c1.getY()<c2.getY())
					return 1;
				else if (c1.getY()==c2.getY()){
					if (c1.getX()>c2.getX())
						return 0;
					else return -1;
				}
				else
					return -1;
			}
		});
		/*for (Cube c : cubes){
			System.out.println(c.getX()+", "+c.getY()+", "+c.getName());
		}
		System.out.println("-----------------");*/
		//judge if can merge with the lower cube
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int x=0;
		for(int i=0; i<10; i++){
			//random.setSeed(random.nextLong());
			Cube c = new Cube(parent, 0, cubeDB.get(random.nextInt(cubeDB.size())), inix+x, iniy);
			cubes.add(c);
			x = x + myApplet.cubewidth + 10;
		}
		Thread t = new Thread(pbar);	///progressBar start counting
		t.start();
		
		
		while(true){
			
			System.out.print("");
			if(parent.gamePhase==5) continue;
			
			for (int i = 0; i < cubes.size(); i++){		///cubes will always been dragged to the lowest position they can
				Cube ch = cubes.get(i);
				
				Cube highest=null;
				for (int j=0; j<cubes.size(); j++){
					Cube c = cubes.get(j);
					if( ((c.getX()<=ch.getX() && c.getX()+myApplet.cubewidth>ch.getX())|| (c.getX()<ch.getX()+myApplet.cubewidth && c.getX()+myApplet.cubewidth>ch.getX()+myApplet.cubewidth)) 
						&& (highest==null||c.getY()<highest.getY()) && c!=ch && c.getY()>=ch.getY() ) highest = c;
				}
				
				int high ;
				if(highest==null) high = iniy+70;
				else high= highest.getY();
				high = high - 10 - myApplet.cubeheight;
				if(!ch.isDragged() && ch.getY()<high)ch.setY(ch.getY()+1);
				else if(ch.getY()>high && (highest==null||highest.isDragged()==false)/*&& ch==dragCube*/ )ch.setY(ch.getY()-1);
				
				
				if(ch.getY()>=high && highest!=null && ch.getX()==highest.getX()) if(ch.getTarget().equals(highest.getName())) merge(highest,ch);
				
				
				if(ch.getY()<finaly) GameOver=true;
			}
			
			if(pbar.isdone()==true) {	///add a new line of cubes if progressBar achieve it's goal
				addCube();
				pbar.undone();
			}else if(addbound==true){
				//addBound();
				addbound = false;
			}
			
			
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(GameOver){
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pbar.stop();	///stop progressBar
				myApplet.dieM.play();
				break;	
			}
			
		}
		parent.returnMenu(); ///call replay and home button
		parent.cp5.getController("Replay").setVisible(true);
		parent.cp5.getController("Home").setVisible(true);
	}
	
	public boolean getGameCondition(){
		return GameOver;
	}
	
	public void setMusicMute() {
		mute = true;
		playBgMusic = false;
	}
	
	public boolean getMusicMute() {
		return mute;
	}
	
	public void setMusicPlay() {
		playBgMusic = true;
		mute = false;
	}
	
	public boolean getMusicPlay() {
		return playBgMusic;
	}
}
